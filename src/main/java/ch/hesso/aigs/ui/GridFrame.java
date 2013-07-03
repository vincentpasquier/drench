package ch.hesso.aigs.ui;

import ch.hesso.aigs.game.Cell;
import ch.hesso.aigs.game.DrenchModel;
import ch.hesso.aigs.game.events.PlayedEvent;
import com.google.common.eventbus.Subscribe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class GridFrame {

  //
  private final DrenchModel _model;

  //
  private final JPanel _cellPanel;

  //
  private final GridLayout _cellLayout;

  //
  private final List<List<JButton>> _cells;

  public GridFrame ( final DrenchModel model ) {
    _model = model;
    _cellLayout = new GridLayout ( _model.height (), _model.width (), 0, 0 );
    _cellPanel = new JPanel ( _cellLayout );
    _cells = new ArrayList<> ( _model.height () );
    for ( int i = 0; i < _model.height (); ++i ) {
      _cells.add ( new ArrayList<JButton> ( _model.width () ) );
    }
    int i = 0;
    for ( List<Cell> row : _model.cells () ) {
      for ( Cell cell : row ) {
        JButton bt = new JButton ();
        bt.setBackground ( cell.paint ().color () );
        _cellPanel.add ( bt );
        _cells.get ( i ).add ( bt );
        bt.setEnabled ( false );
        bt.setBorder ( new EmptyBorder ( 0, 0, 0, 0 ) );
      }
      ++i;
    }
  }

  @Subscribe
  public void updateCells ( final PlayedEvent event ) {
    for ( Cell cell : event.affected () ) {
      JButton bt = _cells.get ( cell.x () ).get ( cell.y () );
      bt.setBackground ( cell.paint ().color () );
    }
  }

  public JComponent component () {
    return _cellPanel;
  }
}
