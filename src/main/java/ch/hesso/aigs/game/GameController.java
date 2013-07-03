package ch.hesso.aigs.game;

import ch.hesso.aigs.ai.AI;
import ch.hesso.aigs.ai.annotations.AIBooleanParameter;
import ch.hesso.aigs.ai.annotations.AIDigitParameters;
import ch.hesso.aigs.game.events.*;
import ch.hesso.aigs.ui.components.AIBooleanParameterJComponent;
import ch.hesso.aigs.ui.components.AIDigitParameterJComponent;
import ch.hesso.aigs.ui.components.AIJComponent;
import ch.hesso.aigs.util.ClassAIFinder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ch.hesso.aigs.Constants.*;
import static ch.hesso.aigs.ai.AI.Kind.PLAYER;

/**
 *
 */
public final class GameController {

  //
  private final EventBus _bus;

  //
  private DrenchModel _model;

  //
  private final int _objective;

  //
  private Set<Cell> _affected;

  //
  private final GameAILoader _loader;

  //
  private AI _ai;

  //
  private String _aiName;

  /**
   *
   */
  public GameController () {
    this ( GRID_DEFAULT_WIDTH, GRID_DEFAULT_HEIGHT, TURNS_DEFAULT );
  }

  //
  private GameController ( final int width, final int height, final int turns ) {
    _model = DrenchModel.Generator.createRandomModel ( width, height, turns );
    _objective = width * height;
    _bus = new EventBus ();
    _loader = new GameAILoader ();
    _ai = _loader.defaultAI ();
    _aiName = _loader.defaultAIName ();
    _bus.register ( this );
  }

  /**
   * @return
   */
  public DrenchModel model () {
    return _model;
  }

  /**
   * @return
   */
  public EventBus bus () {
    return _bus;
  }

  /**
   * @param event
   */
  @Subscribe
  public void nextAI ( final NextEvent event ) {
    Paint paintAI = _ai.play ( _model );
    _bus.post ( new PlayEvent ( paintAI ) );
  }

  /**
   * @param event
   */
  @Subscribe
  public void play ( final PlayEvent event ) {
    _affected = _model.paint ( event.paint () );
    if ( won () ) {
      _bus.post ( new GameWonEvent ( _affected, _model.turns () ) );
      _model = DrenchModel.Generator.createRandomModel ( _model.width (), _model.height (), _model.maxTurns () - 1 );
      for ( AI ai : _loader._ais.values () ) {
        ai.initialize ( this );
      }
    } else if ( lost () ) {
      _bus.post ( new GameOverEvent ( _affected ) );
      _model = DrenchModel.Generator.createRandomModel ( _model.width (), _model.height (), _model.maxTurns () );
    } else {
      _bus.post ( new PlayedEvent ( _affected, _model.turns () ) );
    }
  }

  /**
   * @return
   */
  private boolean lost () {
    return _model.turns () <= 0;
  }

  /**
   * @return
   */
  private boolean won () {
    return _affected.size () == _objective;
  }

  /**
   * @return
   */
  public ImmutableSet<String> aiNames () {
    return _loader.names ();
  }

  /**
   * @return
   */
  public AI selectedAI () {
    return _ai;
  }

  /**
   * @return
   */
  public String selectedAIName () {
    return _aiName;
  }

  /**
   * @param ai
   */
  public void setAI ( final String ai ) {
    _aiName = ai;
    _ai = _loader._ais.get ( ai );
  }

  /**
   *
   */
  private final class GameAILoader {

    //
    private AI _default;

    //
    private String _defaultAIName;

    //
    private final ClassAIFinder _finder;

    //
    private final ImmutableSet<Class<? extends AI>> _classes;

    //
    private ImmutableMap<String, AI> _ais;

    // Logger java
    private final Logger LOG = Logger.getLogger ( GameController.GameAILoader.class.getName () );

    /**
     *
     */
    public GameAILoader () {
      _finder = new ClassAIFinder ();
      _classes = _finder.getAIClasses ( AI.class, "ch.hesso" );
      ImmutableMap.Builder<String, AI> builder = ImmutableMap.builder ();
      for ( Class<? extends AI> ai : _classes ) {
        try {
          AI instance = ai.newInstance ();
          instance.initialize ( GameController.this );
          if ( instance.kind ().equals ( PLAYER ) ) {
            _default = instance;
            _defaultAIName = instance.name ();
          }
          builder.put ( instance.name (), instance );
          initClassFields ( ai, instance );
        } catch ( InstantiationException | IllegalAccessException e ) {
          LOG.log ( Level.SEVERE, "[GameController.GameAILoader.GameAILoader]: " + e.getMessage () );
        }
      }
      _ais = builder.build ();
      if ( _default == null && _ais.size () > 0 ) {
        _default = _ais.values ().asList ().get ( 0 );
        _defaultAIName = _ais.keySet ().asList ().get ( 0 );
      }
    }

    /**
     * TODO Refactor, instanceof code smell.
     * TODO Add new annotation in here for the moment
     *
     * @param ai
     * @param instance
     */
    private void initClassFields ( final Class<? extends AI> ai, final AI instance ) throws IllegalAccessException {
      for ( Field field : ai.getDeclaredFields () ) {
        for ( Annotation annotation : field.getDeclaredAnnotations () ) {
          if ( annotation instanceof AIDigitParameters ) {
            initClassFieldDigitParameter ( ai, instance, field, ( AIDigitParameters ) annotation );
          } else if ( annotation instanceof AIBooleanParameter ) {
            initClassFieldBooleanParameter ( ai, instance, field, ( AIBooleanParameter ) annotation );
          }
          // else if ( annotation instanceof WhatEverFieldAnnotation ) { } -> put it here!
        }
      }
    }

    /**
     * @param ai
     * @param instance
     * @param field
     * @param parameter
     * @throws IllegalAccessException
     */
    private void initClassFieldDigitParameter ( final Class<? extends AI> ai, final AI instance, final Field field, final AIDigitParameters parameter ) throws IllegalAccessException {
      AIJComponent uiComponent = null;
      if ( field.getType ().equals ( int.class ) ) {
        uiComponent = new AIDigitParameterJComponent<Integer> ( ( int ) parameter.value (), ( int ) parameter.min (), ( int ) parameter.max (), ai, instance, field );
        field.setInt ( instance, ( int ) parameter.value () );
      } else if ( field.getType ().equals ( double.class ) ) {
        uiComponent = new AIDigitParameterJComponent<Double> ( parameter.value (), parameter.min (), parameter.max (), ai, instance, field );
        field.setDouble ( instance, parameter.value () );
      }
      if ( uiComponent != null ) {
        uiComponent.addComponents ( instance.component () );
      }
    }

    /**
     * @param ai
     * @param instance
     * @param field
     * @param annotation
     */
    private void initClassFieldBooleanParameter ( final Class<? extends AI> ai, final AI instance, final Field field, final AIBooleanParameter annotation ) throws IllegalAccessException {
      AIJComponent uiComponent = new AIBooleanParameterJComponent ( annotation.value (), ai, instance, field );
      field.setBoolean ( instance, annotation.value () );
      uiComponent.addComponents ( instance.component () );
    }

    /**
     * @return
     */
    public AI defaultAI () {
      return _default;
    }

    /**
     * @return
     */
    public ImmutableSet<String> names () {
      return _ais.keySet ();
    }

    /**
     * @return
     */
    public String defaultAIName () {
      return _defaultAIName;
    }
  }
}
