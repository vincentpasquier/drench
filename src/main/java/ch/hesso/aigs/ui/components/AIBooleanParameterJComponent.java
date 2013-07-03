package ch.hesso.aigs.ui.components;

import ch.hesso.aigs.ai.AI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public final class AIBooleanParameterJComponent implements ActionListener, AIJComponent {

  //
  private final Class<? extends AI> _ai;

  //
  private final AI _instance;

  //
  private final Field _field;

  //
  private final JCheckBox _cb;

  //
  private boolean _value;

  // Logger java
  private static final Logger LOG = Logger.getLogger ( AIBooleanParameterJComponent.class.getName () );

  /**
   * @param value
   * @param ai
   * @param instance
   * @param field
   */
  public AIBooleanParameterJComponent ( final boolean value, final Class<? extends AI> ai, final AI instance, final Field field ) {
    _value = value;
    _ai = ai;
    _instance = instance;
    _field = field;
    _cb = new JCheckBox ();
    _cb.setSelected ( _value );
    _cb.addActionListener ( this );
  }

  /**
   * @param component
   */
  @Override
  public void addComponents ( final JComponent component ) {
    component.add ( new JLabel ( _field.getName () ) );
    component.add ( _cb );
  }

  /**
   * @param event
   */
  @Override
  public void actionPerformed ( final ActionEvent event ) {
    _value = _cb.isSelected ();
    try {
      _field.setBoolean ( _instance, _value );
    } catch ( IllegalAccessException e ) {
      LOG.log ( Level.SEVERE, "[AIBooleanParameterJComponent.actionPerformed]: " + e.getMessage () );
    }
  }
}