package ch.hesso.aigs.ai.player;

import ch.hesso.aigs.ai.AI;
import ch.hesso.aigs.game.DrenchModel;
import ch.hesso.aigs.game.GameController;
import ch.hesso.aigs.game.Paint;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public final class Player extends AI {

  //
  private PlayerPanel _playerPanel;

  /**
   * @param model
   * @return
   */
  @Override
  public Paint play ( final DrenchModel model ) {
    return null; // Intended, this one does not implements play as it reacts to
  }

  /**
   * @param controller
   */
  @Override
  public void initialize ( final GameController controller ) {
    super.initialize ( controller );
    if ( _playerPanel == null ) {
      _playerPanel = new PlayerPanel ();
      _panel.add ( _playerPanel.component () );
    }
  }

  /**
   * @return
   */
  @Override
  public String author () {
    return "Pasquier";
  }

  /**
   * @return
   */
  @Override
  public Kind kind () {
    return Kind.PLAYER;
  }

  /**
   *
   */
  private final class PlayerPanel {

    //
    private final JPanel _buttonPanel;
    //
    private final GridLayout _buttonLayout;

    /**
     *
     */
    private PlayerPanel () {
      _buttonLayout = new GridLayout ( 3, 2, 1, 1 );
      _buttonPanel = new JPanel ( _buttonLayout );
      for ( Paint paint : Paint.values () ) {
        JButton bt = new JButton ();
        bt.setBackground ( paint.color () );
        _buttonPanel.add ( bt );
        Dimension buttonSize = new Dimension ( 40, 40 );
        bt.setMinimumSize ( buttonSize );
        bt.setMaximumSize ( buttonSize );
        bt.setBorder ( BorderFactory.createLineBorder ( Color.GRAY ) );
        bt.addActionListener ( new ColorButtonController ( paint, _controller.bus () ) );
      }
    }

    /**
     * @return
     */
    public JComponent component () {
      return _buttonPanel;
    }
  }
}
