package ch.hesso.aigs.game.events;

import ch.hesso.aigs.game.Paint;

/**
 *
 */
public final class PlayEvent {

  //
  private final Paint _paint;

  /**
   * @param paint
   */
  public PlayEvent ( final Paint paint ) {
    _paint = paint;
  }

  /**
   * @return
   */
  public Paint paint () {
    return _paint;
  }
}
