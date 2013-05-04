package ch.hesso.aigs.model;

import java.util.*;

/**
 *
 */
public final class DrenchModel {

  //
  private final int _gridWidth;

  //
  private final int _gridHeight;

  //
  private int _turns;

  //
  private final List<List<Cell>> _cells;

  //
  private DrenchModel ( final int gridWidth, final int gridHeight, final int turns ) {
    _gridWidth = gridWidth;
    _gridHeight = gridHeight;
    _turns = turns;
    _cells = new ArrayList<> ( gridWidth );
  }

  /**
   * @param newPaint
   * @return
   */
  public Set<Cell> paint ( final Paint newPaint ) {
    Set<Cell> cells = new HashSet<> ();
    Paint leftCornerPaint = _cells.get ( 0 ).get ( 0 ).paint ();
    if ( leftCornerPaint.equals ( newPaint ) || _turns <= 0 ) {
      return cells;
    }
    --_turns;
    cover ( 0, 0, leftCornerPaint, newPaint, cells ); // TODO: Check better than left-up corner ?
    for ( Cell cell : cells ) {
      cell.addPaint ( newPaint );
    }
    return cells;
  }

  /**
   * @param x
   * @param y
   * @param leftCornerPaint
   * @param newPaint
   * @param cells
   */
  public void cover ( final int x, final int y, final Paint leftCornerPaint, final Paint newPaint, final Set<Cell> cells ) {
    if ( !isInside ( x, y ) ) {
      return;
    }
    Cell affected = _cells.get ( y ).get ( x );
    if ( cells.contains ( affected ) ) {
      return;
    }
    Paint cellPaint = affected.paint ();
    if ( !newPaint.equals ( cellPaint ) && !leftCornerPaint.equals ( cellPaint ) ) {
      return;
    }
    cells.add ( affected );
    cover ( x + 1, y, leftCornerPaint, newPaint, cells );
    cover ( x, y + 1, leftCornerPaint, newPaint, cells );
    cover ( x - 1, y, leftCornerPaint, newPaint, cells );
    cover ( x, y - 1, leftCornerPaint, newPaint, cells );
  }

  /**
   * @param x
   * @param y
   * @return
   */
  public boolean isInside ( final int x, final int y ) {
    boolean inside = true;
    inside &= x >= 0;
    inside &= y >= 0;
    inside &= x < _gridWidth;
    inside &= y < _gridHeight;
    return inside;
  }

  /**
   * @param gridWidth
   * @param gridHeight
   */
  public static DrenchModel createRandomModel ( final int gridWidth, final int gridHeight, final int turns ) {
    DrenchModel model = new DrenchModel ( gridWidth, gridHeight, turns );
    Random rand = new Random ();
    Paint[] paints = Paint.values ();
    for ( int i = 0; i < gridHeight; ++i ) {
      List<Cell> row = new ArrayList<> ( gridHeight );
      for ( int j = 0; j < gridWidth; ++j ) {
        row.add ( new Cell ( j, i, paints[ rand.nextInt ( paints.length ) ] ) );
      }
      model._cells.add ( row );
    }
    return model;
  }

  /**
   * @return
   */
  public List<List<Cell>> cells () {
    return _cells;
  }

  /**
   * @return
   */
  public int turns () {
    return _turns;
  }

  /**
   * @return
   */
  public int width () {
    return _gridWidth;
  }

  /**
   * @return
   */
  public int height () {
    return _gridHeight;
  }
}
