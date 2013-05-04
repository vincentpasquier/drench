package ch.hesso.aigs;

import ch.hesso.aigs.model.DrenchModel;
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
    DrenchModel model = DrenchModel.createRandomModel ( 15, 15, 30 );
    GameFrame frame = new GameFrame ( model );
    frame.init ();
  }
}
