package ch.hesso.aigs.model;

import java.awt.*;

/**
 *
 */
public enum Paint {
  RED ( Color.RED ),
  GREEN ( new Color ( 102, 204, 0 ) ),
  BLUE ( new Color ( 116, 62, 244 ) ),
  BLACK ( new Color ( 153, 153, 255 ) ),
  WHITE ( Color.WHITE ),
  YELLOW ( new Color ( 255, 204, 0 ) );

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
