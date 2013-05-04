package ch.hesso.aigs.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class Cell {

  //
  private final int _x;

  //
  private final int _y;

  //
  private final List<Paint> _paints;

  /**
   * @param x
   * @param y
   * @param paint
   */
  public Cell ( final int x, final int y, final Paint paint ) {
    _x = x;
    _y = y;
    _paints = new ArrayList<> ( 5 );
    _paints.add ( paint );
  }

  /**
   * @return
   */
  public int x () {
    return _x;
  }

  /**
   * @return
   */
  public int y () {
    return _y;
  }

  /**
   * @return
   */
  public Paint paint () {
    return _paints.get ( _paints.size () - 1 );
  }

  /**
   * @param paint
   */
  public void addPaint ( final Paint paint ) {
    _paints.add ( paint );
  }

  /**
   * @return
   */
  public boolean isActive () {
    return _paints.size () > 1;
  }

  /**
   * @param obj
   * @return
   */
  @Override
  public boolean equals ( final Object obj ) {
    if ( this == obj ) {
      return true;
    }
    if ( !( obj instanceof Cell ) ) {
      return false;
    }
    Cell c = ( Cell ) obj;
    boolean equal = true;
    equal &= _x == c._x;
    equal &= _y == c._y;
    equal &= _paints.size () == c._paints.size ();
    // TODO: Check maybe paints ...
    return equal;
  }

  /**
   * @return
   */
  @Override
  public int hashCode () {
    int hash = 7;
    hash *= _x + 31;
    hash *= _y + 31;
    hash *= _paints.size () + 31;
    return hash;
  }
}
