package ch.hesso.aigs.ui;

import ch.hesso.aigs.ai.AI;
import ch.hesso.aigs.game.DrenchModel;
import ch.hesso.aigs.game.GameController;
import ch.hesso.aigs.game.events.GameOverEvent;
import ch.hesso.aigs.game.events.GameWonEvent;
import ch.hesso.aigs.game.events.NextEvent;
import ch.hesso.aigs.game.events.PlayedEvent;
import com.google.common.eventbus.Subscribe;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ch.hesso.aigs.Constants.FRAME_DEFAULT_HEIGHT;
import static ch.hesso.aigs.Constants.FRAME_DEFAULT_WIDTH;

/**
 *
 */
public final class GameFrame {

  //
  private GameController _controller;

  //
  private DrenchModel _model;

  //
  private final JFrame _frame;

  //
  private final BorderLayout _centerLayout;

  //
  private final JPanel _leftPanel;

  //
  private final JPanel _aiPanel;

  //
  private final JLabel _lbTurns;

  //
  private final JComboBox<String> _comboAI;

  //
  private final JButton _buttonNextAI;

  //
  private GridFrame _grid;

  /**
   * @param controller
   */
  public GameFrame ( final GameController controller ) {
    _controller = controller;
    _model = _controller.model ();
    _centerLayout = new BorderLayout ();
    _frame = new JFrame ();
    _frame.setLayout ( _centerLayout );
    _frame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
    _frame.setTitle ( "Drench - D. Gambin / V. Pasquier" );
    _frame.setSize ( FRAME_DEFAULT_WIDTH, FRAME_DEFAULT_HEIGHT );

    _leftPanel = new JPanel ( new MigLayout ( "flowY" ) );
    _aiPanel = new JPanel ( new GridLayout () );
    _lbTurns = new JLabel ();

    _comboAI = new JComboBox<> ();
    Dimension comboSize = new Dimension ( 320, 20 );
    _comboAI.setPreferredSize ( comboSize );
    _comboAI.setMaximumSize ( comboSize );
    _buttonNextAI = new JButton ( "Next" );
    _buttonNextAI.setEnabled ( _controller.selectedAI ().kind () == AI.Kind.AI );

    for ( String name : _controller.aiNames () ) {
      _comboAI.addItem ( name );
    }
    _aiPanel.add ( _controller.selectedAI ().component () );
    _comboAI.setSelectedItem ( _controller.selectedAIName () );

    _comboAI.addActionListener ( new ActionListener () {
      @Override
      public void actionPerformed ( final ActionEvent e ) {
        String ai = ( ( JComboBox<String> ) e.getSource () ).getSelectedItem ().toString ();
        _controller.setAI ( ai );
        _aiPanel.removeAll ();
        _aiPanel.add ( _controller.selectedAI ().component () );
        _buttonNextAI.setEnabled ( _controller.selectedAI ().kind () == AI.Kind.AI );

        _frame.repaint ();
        _frame.validate ();
      }
    } );

    _buttonNextAI.addActionListener ( new ActionListener () {
      @Override
      public void actionPerformed ( final ActionEvent e ) {
        _controller.bus ().post ( new NextEvent () );
      }
    } );

    _leftPanel.add ( _comboAI );
    _leftPanel.add ( _buttonNextAI );
    _leftPanel.add ( _aiPanel );
    _frame.getContentPane ().add ( _lbTurns, BorderLayout.NORTH );
    _frame.getContentPane ().add ( _leftPanel, BorderLayout.EAST );
  }

  /**
   *
   */
  public void init () {
    _model = _controller.model ();
    Component center = _centerLayout.getLayoutComponent ( BorderLayout.CENTER );
    if ( center != null ) {
      _frame.getContentPane ().remove ( center );
    }
    _grid = new GridFrame ( _model );
    _frame.getContentPane ().add ( _grid.component () );
    _controller.bus ().register ( _grid );
    _controller.bus ().register ( this );

    _frame.repaint ();
    _frame.validate ();
    _frame.setVisible ( true );
    _lbTurns.setText ( "Remaining turns: " + _model.turns () );
  }

  @Subscribe
  public void updateTurnsLabel ( final PlayedEvent event ) {
    if ( _model.turns () == event.turns () ) {
      _lbTurns.setText ( "Remaining turns: " + event.turns () );
    }
  }

  @Subscribe
  public void gameEnd ( final GameOverEvent event ) {
    init ();
  }

  @Subscribe
  public void wonTurn ( final GameWonEvent event ) {
    init ();
  }
}
