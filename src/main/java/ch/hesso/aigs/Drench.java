package ch.hesso.aigs;

import ch.hesso.aigs.game.GameController;
import ch.hesso.aigs.ui.GameFrame;

/**
 * TODO: decide which method to apply and how
 */
public final class Drench {

  /**
   * @param args
   */
  public static void main ( final String[] args ) {
    // TODO: Parameters (Grid file)
    GameController controller = new GameController ();
    GameFrame frame = new GameFrame ( controller );
    frame.init ();
  }
}
