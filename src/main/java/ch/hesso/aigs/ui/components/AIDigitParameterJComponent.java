package ch.hesso.aigs.ui.components;

import ch.hesso.aigs.ai.AI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public final class AIDigitParameterJComponent<T extends Number & Comparable<T>> implements ActionListener, AIJComponent {

  private final T _value;
  private final T _min;
  private final T _max;
  private final Class<? extends AI> _ai;
  private final AI _instance;
  private final Field _field;
  private final JTextField _tf;

  // Logger java
  private static final Logger LOG = Logger.getLogger ( AIDigitParameterJComponent.class.getName () );

  public AIDigitParameterJComponent ( final T value, final T min, final T max, final Class<? extends AI> ai, final AI instance, final Field field ) {
    _value = value;
    _min = min;
    _max = max;
    _ai = ai;
    _instance = instance;
    _field = field;
    _tf = new JTextField ( _value.toString () );
    _tf.addActionListener ( this );
    _tf.setColumns ( 25 );
  }

  @Override
  public void addComponents ( final JComponent component ) {
    component.add ( new JLabel ( _field.getName () ) );
    component.add ( _tf );
  }

  /**
   * TODO So far we are only able to manage Double and Integer
   * @param event
   */
  @Override
  public void actionPerformed ( final ActionEvent event ) {
    _tf.setBackground ( Color.WHITE );
    boolean inRange = false;
    T tf = null;
    try {
      if ( _value instanceof Double ) {
        tf = ( T ) new Double ( Double.parseDouble ( _tf.getText () ) );
      } else if ( _value instanceof Integer ) {
        tf = ( T ) new Integer ( Integer.parseInt ( _tf.getText () ) );
      }
      inRange = tf.compareTo ( _min ) >= 0;
      inRange &= tf.compareTo ( _max ) <= 0;
    } catch ( NumberFormatException e ) {
      // Just log it, user was being mean by giving something bad.
      LOG.log ( Level.SEVERE, "[AIDigitParameterJComponent.actionPerformed]: " + e.getMessage () );
    }
    if ( inRange ) {
      try {
        _field.set ( _instance, tf );
      } catch ( IllegalAccessException e ) {
        // If this happens, we're screwed
        LOG.log ( Level.SEVERE, "[AIDigitParameterJComponent.actionPerformed]: " + e.getMessage () );
      }
    } else {
      _tf.setBackground ( Color.RED );
    }
  }
}
