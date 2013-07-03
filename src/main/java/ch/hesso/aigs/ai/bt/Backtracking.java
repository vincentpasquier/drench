package ch.hesso.aigs.ai.bt;

import ch.hesso.aigs.ai.AI;
import ch.hesso.aigs.ai.annotations.AIBooleanParameter;
import ch.hesso.aigs.ai.annotations.AIDigitParameters;
import ch.hesso.aigs.game.DrenchModel;
import ch.hesso.aigs.game.GameController;
import ch.hesso.aigs.game.Paint;

import java.util.*;

/**
 *
 */
public final class Backtracking extends AI {

  /**
   *
   */
  private Map<Integer, List<Paint>> _paintsResults;

  /**
   *
   */
  private List<Paint> _paints;

  /**
   *
   */
  @AIBooleanParameter (value = false)
  public boolean reevaluate;

  /**
   *
   */
  @AIDigitParameters (max = 10, min = 0, value = 3)
  public int max_depth;

  /**
   *
   */
  @AIDigitParameters (max = 1000, min = 0, value = 25)
  public int stop_threshold;

  /**
   * @param controller
   */
  @Override
  public void initialize ( final GameController controller ) {
    super.initialize ( controller );
    _paints = new ArrayList<> ();
  }

  /**
   * TODO Improve: give possibility to re-evaluate after each iteration
   *
   * @param model
   * @return
   */
  @Override
  public Paint play ( final DrenchModel model ) {
    /*ImmutableMap<Paint, Integer> neighbours = model.neighbourArea ();
    for ( Map.Entry<Paint, Integer> paintIntegerEntry : neighbours.entrySet () ) {
      System.out.println ( paintIntegerEntry.getKey () + " : " + paintIntegerEntry.getValue () );
    }
    model.colorsDownMost ();
    model.colorsRightMost ();
    */
    if ( _paints.isEmpty () || reevaluate ) { // Yup, we depleted the responses, or reevaluate each time
      _paintsResults = play ( model, Paint.getPaintExclude ( model.currentPaint () ), max_depth );
      _paints = bestPermutation ( _paintsResults );
    }
    Paint toReturn = _paints.get ( 0 );
    _paints.remove ( 0 );
    return toReturn;
  }

  private final Random _random = new Random ();

  /**
   *
   */
  private Map<Integer, List<Paint>> play ( final DrenchModel model, final List<Paint> paints, final int iterations ) {
    Collections.shuffle ( paints );
    int covered = model.numberRemainingCells ();
    Map<Integer, List<Paint>> paintScores = new TreeMap<> ();
    if ( iterations < 0 || model.turns () <= 0 ) {
      return paintScores; // This is the end, my only friend the end!
    }
    for ( Paint fill : paints ) {
      DrenchModel simulate = DrenchModel.Simulator.simulate ( model, fill );
      int score = covered - simulate.numberRemainingCells ();
      Map<Integer, List<Paint>> recursiveScores = play ( simulate, Paint.getPaintExclude ( fill ), iterations - 1 );
      List<Paint> recursivePaints = new ArrayList<> ();
      recursivePaints.add ( fill );
      recursivePaints.addAll ( bestPermutation ( recursiveScores ) );
      if ( !paintScores.containsKey ( score ) ) {
        paintScores.put ( score, recursivePaints );
      }
      if ( score >= stop_threshold ) {
        break;
      }
    }

    return paintScores;
  }

  /**
   * @param paints
   * @return
   */
  private List<Paint> bestPermutation ( final Map<Integer, List<Paint>> paints ) {
    List<Paint> bestScoringPermutation = new ArrayList<> ();
    if ( paints.size () > 0 ) {
      List<Integer> scores = new ArrayList<> ( paints.keySet () );
      Collections.reverse ( scores );
      bestScoringPermutation.addAll ( paints.get ( scores.get ( 0 ) ) ); // Best scores
    }
    return bestScoringPermutation;
  }

  /**
   * @return
   */
  @Override
  public String author () {
    return "Gambin, Pasquier";
  }

}
