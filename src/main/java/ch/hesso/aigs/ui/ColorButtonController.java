package ch.hesso.aigs.ui;

import ch.hesso.aigs.model.Cell;
import ch.hesso.aigs.model.DrenchModel;
import ch.hesso.aigs.model.Paint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

/**
 *
 */
public final class ColorButtonController implements ActionListener {

  //
  private final DrenchModel _model;

  //
  private final Paint _paint;

  //
  private final GameFrame _frame;

  //
  private final int _objective;

  //
  private final int _turns;

  /**
   * @param model
   * @param paint
   * @param frame
   */
  public ColorButtonController ( final DrenchModel model, final Paint paint, final GameFrame frame ) {
    _model = model;
    _paint = paint;
    _frame = frame;
    _objective = _model.width () * _model.height ();
    _turns = model.turns ();
  }

  /**
   * @param event
   */
  @Override
  public void actionPerformed ( ActionEvent event ) {
    Set<Cell> affected = _model.paint ( _paint );
    System.out.println ( affected.size () + " : " + _objective );
    if ( _model.turns () <= 0 ) {
      _frame.gameEnd ();
    } else if ( affected.size () == _objective ) {
      _frame.wonTurn ( _turns );
    } else {
      _frame.updateCells ( affected );
      _frame.updateTurnsLabel ( _model.turns () );
    }
  }
}
