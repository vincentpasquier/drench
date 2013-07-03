package ch.hesso.aigs.game;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;

import java.io.File;
import java.nio.charset.Charset;
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
  private final int _maxTurns;

  //
  private final List<List<Cell>> _cells;

  //
  private int _turns;

  //
  private int _cellsAffected;

  //
  private boolean _simulated;

  /**
   * @param gridWidth
   * @param gridHeight
   * @param turns
   */
  private DrenchModel ( final int gridWidth, final int gridHeight, final int turns ) {
    _gridWidth = gridWidth;
    _gridHeight = gridHeight;
    _turns = turns;
    _maxTurns = turns;
    _cellsAffected = 0;
    _simulated = false;
    _cells = new ArrayList<> ( _gridWidth );
    for ( int i = 0; i < _gridWidth; ++i ) {
      List<Cell> cols = new ArrayList<> ( _gridHeight );
      _cells.add ( cols );
    }
  }

  /**
   * @param copy
   */
  private DrenchModel ( final DrenchModel copy ) {
    this ( copy._gridWidth, copy._gridHeight, copy.turns () );

    for ( int i = 0; i < _gridWidth; ++i ) {
      for ( int j = 0; j < _gridHeight; ++j ) {
        _cells.get ( i ).add ( new Cell ( copy._cells.get ( i ).get ( j ) ) );
      }
    }
  }

  /**
   *
   */
  private void initModel () {
    Set<Cell> cells = new HashSet<> ();
    cover ( 0, 0, _cells.get ( 0 ).get ( 0 ).paint (), _cells.get ( 0 ).get ( 0 ).paint (), cells, false ); // Recursive call
    _cellsAffected = cells.size ();
  }

  /**
   * @param newPaint
   * @return
   */
  protected Set<Cell> paint ( final Paint newPaint ) {
    Set<Cell> cells = new HashSet<> ();
    Paint leftCornerPaint = _cells.get ( 0 ).get ( 0 ).paint ();
    if ( leftCornerPaint.equals ( newPaint ) || _turns <= 0 ) {
      return cells;
    }
    --_turns;
    cover ( 0, 0, leftCornerPaint, newPaint, cells, false ); // Recursive call
    _cellsAffected = cells.size ();
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
  private void cover ( final int x, final int y, final Paint leftCornerPaint, final Paint newPaint, final Set<Cell> cells, final boolean stopPropagate ) {
    if ( !isInside ( x, y ) ) {
      return;
    }
    Cell affected = _cells.get ( y ).get ( x );
    if ( cells.contains ( affected ) ) {
      return;
    }
    Paint cellPaint = affected.paint ();
    // This stops the propagation when entering newPaint
    boolean nextStopPropagate = cellPaint.equals ( newPaint );
    // The newPaint has already been covered, stop propagation on the same color as leftCorner
    if ( stopPropagate && leftCornerPaint.equals ( cellPaint ) ) {
      return;
    }
    // In case the left corner paint is the same as the current or it's the same as the new color, should be affected and propagate
    if ( leftCornerPaint.equals ( cellPaint ) || nextStopPropagate ) {
      cells.add ( affected );
      cover ( x + 1, y, leftCornerPaint, newPaint, cells, nextStopPropagate );
      cover ( x, y + 1, leftCornerPaint, newPaint, cells, nextStopPropagate );
      cover ( x - 1, y, leftCornerPaint, newPaint, cells, nextStopPropagate );
      cover ( x, y - 1, leftCornerPaint, newPaint, cells, nextStopPropagate );
    }
  }

  /**
   * @param x
   * @param y
   * @return
   */
  private boolean isInside ( final int x, final int y ) {
    boolean inside = true;
    inside &= x >= 0;
    inside &= y >= 0;
    inside &= x < _gridWidth;
    inside &= y < _gridHeight;
    return inside;
  }

  /**
   * @return
   */
  public ImmutableList<ImmutableList<Cell>> cells () {
    ImmutableList.Builder<ImmutableList<Cell>> builder = ImmutableList.builder ();
    for ( List<Cell> cells : _cells ) {
      builder.add ( ImmutableList.copyOf ( cells ) );
    }
    return builder.build ();
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
  public int maxTurns () {
    return _maxTurns;
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

  /**
   * TODO
   *
   * @return
   */
  public int numberNonAccessibleColors () {
    return 0;
  }

  /**
   * @return
   */
  public int numberRemainingCells () {
    return ( _gridWidth * _gridHeight ) - _cellsAffected;
  }

  /**
   * @return
   */
  public int numberAffectedCells () {
    return _cellsAffected;
  }

  /**
   * TODO
   *
   * @return
   */
  public ImmutableList<Paint> colorsRightMost () {
    ImmutableList.Builder<Paint> builder = ImmutableList.builder ();
    Cell cell = _cells.get ( 0 ).get ( 0 );
    outer:
    for ( int i = _gridWidth - 1; i >= 0; i-- ) {
      for ( int j = _gridHeight - 1; j >= 0; j-- ) {
        cell = _cells.get ( j ).get ( i );
        if ( cell.isActive () ) {
          break outer;
        }
      }
    }
    int row = cell.x ();

    Paint change = cell.paint ();
    for ( int i = cell.y () + 1; i < _gridWidth; i++ ) {
      Paint cellPaint = _cells.get ( row ).get ( i ).paint ();
      if ( !cellPaint.equals ( change ) ) {
        builder.add ( cellPaint );
        change = cellPaint;
      }
    }
    return builder.build ();
  }

  /**
   * TODO
   *
   * @return
   */
  public ImmutableList<Paint> colorsDownMost () {
    ImmutableList.Builder<Paint> builder = ImmutableList.builder ();
    Cell cell = _cells.get ( 0 ).get ( 0 );
    outer:
    for ( int i = _gridHeight - 1; i >= 0; i-- ) {
      for ( int j = _gridWidth - 1; j >= 0; j-- ) {
        cell = _cells.get ( i ).get ( j );
        if ( cell.isActive () ) {
          break outer;
        }
      }
    }
    int col = cell.y ();

    Paint change = cell.paint ();
    for ( int i = cell.x () + 1; i < _gridHeight; i++ ) {
      Paint cellPaint = _cells.get ( i ).get ( col ).paint ();
      if ( !cellPaint.equals ( change ) ) {
        builder.add ( cellPaint );
        change = cellPaint;
      }
    }
    return builder.build ();
  }

  /**
   * @return
   */
  public Paint currentPaint () {
    return _cells.get ( 0 ).get ( 0 ).paint ();
  }

  /**
   * @return
   */
  public ImmutableMap<Paint, Integer> neighbourArea () {
    ImmutableMap.Builder<Paint, Integer> neighbour = ImmutableMap.builder ();
    for ( Paint p : Paint.values () ) {
      if ( !p.equals ( currentPaint () ) ) {
        DrenchModel model = DrenchModel.Simulator.simulate ( this, p );
        int around = model._cellsAffected - _cellsAffected;
        neighbour.put ( p, around );
      }
    }
    return neighbour.build ();
  }

  /**
   *
   */
  public static final class Generator {

    /**
     * @param gridWidth
     * @param gridHeight
     * @param turns
     * @return
     */
    public static DrenchModel createRandomModel ( final int gridWidth, final int gridHeight, final int turns ) {
      DrenchModel model = new DrenchModel ( gridWidth, gridHeight, turns );
      Random rand = new Random ();
      Paint[] paints = Paint.values ();
      for ( int i = 0; i < gridWidth; ++i ) {
        for ( int j = 0; j < gridHeight; ++j ) {
          model._cells.get ( i ).add ( new Cell ( i, j, paints[ rand.nextInt ( paints.length ) ] ) );
        }
      }
      model.initModel ();
      return model;
    }
  }

  /**
   *
   */
  public static final class FileLoader {

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public static DrenchModel loadFromFile ( final File file ) throws Exception {
      List<String> lines = Files.readLines ( file, Charset.defaultCharset () );
      Iterator<String> dims = Splitter.on ( "x" ).trimResults ().omitEmptyStrings ().split ( lines.get ( 0 ) ).iterator ();
      int width = Integer.parseInt ( dims.next () );
      int height = Integer.parseInt ( dims.next () );
      int turns = Integer.parseInt ( dims.next () );
      DrenchModel model = new DrenchModel ( width, height, turns );
      for ( int i = 0; i < height; ++i ) {
        String line = lines.get ( i + 1 );
        for ( int j = 0; j < width; ++j ) {
          model._cells.get ( i ).add ( new Cell ( j, i, Preconditions.checkNotNull ( Paint.getPaint ( line.charAt ( j ) ) ) ) );
        }
      }
      model.initModel ();
      return model;
    }
  }

  /**
   *
   */
  public static final class Simulator {

    /**
     * @param base
     * @param paint
     * @return
     */
    public static DrenchModel simulate ( final DrenchModel base, final Paint paint ) {
      DrenchModel model = base;
      if ( !base._simulated ) {
        model = new DrenchModel ( base );
      }
      model.paint ( paint );
      return model;
    }
  }
}
