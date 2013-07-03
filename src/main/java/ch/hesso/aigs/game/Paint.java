package ch.hesso.aigs.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public enum Paint {
  /**
   *
   */
  RED ( new Color ( 255, 75, 67 ), 'R' ),
  /**
   *
   */
  GREEN ( new Color ( 102, 204, 0 ), 'G' ),
  /**
   *
   */
  BLUE ( new Color ( 60, 62, 244 ), 'B' ),
  /**
   *
   */
  PURPLE ( new Color ( 153, 153, 255 ), 'P' ),
  /**
   *
   */
  WHITE ( new Color ( 187, 255, 205 ), 'W' ),
  /**
   *
   */
  YELLOW ( new Color ( 255, 204, 0 ), 'Y' );

  //
  private final Color _color;

  //
  private final char _text;

  /**
   * @param color
   */
  private Paint ( final Color color, final char text ) {
    _color = color;
    _text = text;
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
  public static Paint getPaint ( final char match ) {
    for ( Paint paint : Paint.values () ) {
      if ( paint._text == match ) {
        return paint;
      }
    }
    return null;
  }

  public static List<Paint> getPaintExclude ( final Paint exclude ) {
    List<Paint> paints = new ArrayList<> ( Paint.values ().length );
    for ( Paint paint : Paint.values () ) {
      if ( !paint.equals ( exclude ) ) {
        paints.add ( paint );
      }
    }
    return paints;
  }
}
