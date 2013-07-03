package ch.hesso.aigs.ai;

import ch.hesso.aigs.game.DrenchModel;
import ch.hesso.aigs.game.GameController;
import ch.hesso.aigs.game.Paint;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 *
 */
public abstract class AI {

  //
  protected final JPanel _panel;

  //
  protected GameController _controller;

  /**
   * @param model
   * @return
   */
  abstract public Paint play ( final DrenchModel model );

  /**
   * @return
   */
  abstract public String author ();

  /**
   *
   */
  public AI () {
    _panel = new JPanel ( new MigLayout ( "flowY" ) );
  }

  /**
   * @param controller
   */
  public void initialize ( final GameController controller ) {
    _controller = controller;
  }

  /**
   * @return
   */
  public String name () {
    return this.getClass ().getName ();
  }

  /**
   * @return
   */
  public Kind kind () {
    return Kind.AI;
  }

  /**
   * @return
   */
  public JComponent component () {
    return _panel;
  }

  /**
   *
   */
  public static enum Kind {
    AI, PLAYER
  }
}
