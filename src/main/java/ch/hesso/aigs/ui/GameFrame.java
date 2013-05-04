package ch.hesso.aigs.ui;

import ch.hesso.aigs.model.Cell;
import ch.hesso.aigs.model.DrenchModel;
import ch.hesso.aigs.model.Paint;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
public final class GameFrame {

  //
  private DrenchModel _model;

  //
  private final JFrame _frame;

  //
  private final JPanel _cellPanel;

  //
  private final JPanel _buttonPanel;

  //
  private final GridLayout _cellLayout;

  //
  private final GridLayout _buttonLayout;

  //
  private final List<List<JButton>> _cells;

  //
  private final JLabel _lbTurns;

  /**
   * @param model
   */
  public GameFrame ( final DrenchModel model ) {
    _model = model;
    _frame = new JFrame ();
    _cellLayout = new GridLayout ( model.height (), model.width (), 1, 1 );
    _buttonLayout = new GridLayout ( 3, 2, 1, 1 );
    _cellPanel = new JPanel ( _cellLayout );
    _buttonPanel = new JPanel ( _buttonLayout );
    _cells = new ArrayList<> ( model.height () );
    _lbTurns = new JLabel ();
    _frame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
    _frame.setTitle ( "Drench - D. Gambin / V. Pasquier" );
    _frame.setSize ( 600, 600 );

    _frame.getContentPane ().add ( _lbTurns, BorderLayout.NORTH );
    JPanel borderLayoutFuckedUp = new JPanel ( new FlowLayout () );
    borderLayoutFuckedUp.add ( _buttonPanel );
    _frame.getContentPane ().add ( _cellPanel, BorderLayout.CENTER );
    _frame.getContentPane ().add ( borderLayoutFuckedUp, BorderLayout.EAST );

  }

  /**
   *
   */
  public void init () {

    _cells.clear ();
    for ( int i = 0; i < _model.height (); ++i ) {
      _cells.add ( new ArrayList<JButton> ( _model.width () ) );
    }
    _cellPanel.removeAll ();
    int i = 0;
    for ( List<Cell> row : _model.cells () ) {
      for ( Cell cell : row ) {
        JButton bt = new JButton ();
        bt.setBackground ( cell.paint ().color () );
        _cellPanel.add ( bt );
        _cells.get ( i ).add ( bt );
        bt.setEnabled ( false );
      }
      ++i;
    }

    _buttonPanel.removeAll ();
    for ( Paint paint : Paint.values () ) {
      JButton bt = new JButton ();
      bt.setBackground ( paint.color () );
      _buttonPanel.add ( bt );
      bt.setBorder ( new EmptyBorder ( 20, 20, 20, 20 ) );
      bt.addActionListener ( new ColorButtonController ( _model, paint, this ) );
    }

    _frame.repaint ();
    _frame.validate ();
    _frame.setVisible ( true );
    _lbTurns.setText ( "Remaining turns: " + _model.turns () );
  }

  /**
   * @param turns
   */
  public void updateTurnsLabel ( final int turns ) {
    _lbTurns.setText ( "Remaining turns: " + turns );
  }

  /**
   * @param affected
   */
  public void updateCells ( final Set<Cell> affected ) {
    for ( Cell cell : affected ) {
      JButton bt = _cells.get ( cell.y () ).get ( cell.x () );
      bt.setBackground ( cell.paint ().color () );
    }
  }

  /**
   *
   */
  public void gameEnd () {
    JOptionPane.showMessageDialog ( null, "Hay you lost, may the luck be with you, always ! ;)" );
    System.exit ( 0 );
  }

  /**
   * @param turns
   */
  public void wonTurn ( final int turns ) {
    _model = DrenchModel.createRandomModel ( _model.width (), _model.height (), turns - 1 );
    init ();
  }
}
