package ch.hesso.aigs.model;

import java.awt.*;

/**
 *
 */
public enum Paint {
  RED ( Color.RED ),
  GREEN ( Color.GREEN ),
  BLUE ( Color.BLUE ),
  BLACK ( Color.BLACK ),
  WHITE ( Color.WHITE ),
  YELLOW ( Color.YELLOW );

  //
  private final Color _color;

  /**
   * @param color
   */
  private Paint ( final Color color ) {
    _color = color;
  }

  /**
   * @return
   */
  public Color color () {
    return _color;
  }

  /**
   * @param match
   * @return
   */
  public static Paint getPaint ( final Paint match ) {
    for ( Paint paint : Paint.values () ) {
      if ( paint.equals ( match ) ) {
        return paint;
      }
    }
    return null;
  }
}
