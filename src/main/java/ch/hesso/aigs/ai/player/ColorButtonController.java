package ch.hesso.aigs.ai.player;

import ch.hesso.aigs.game.Paint;
import ch.hesso.aigs.game.events.PlayEvent;
import com.google.common.eventbus.EventBus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public final class ColorButtonController implements ActionListener {

  //
  private final Paint _paint;

  //
  private final EventBus _bus;

  /**
   * @param paint
   * @param bus
   */
  public ColorButtonController ( final Paint paint, final EventBus bus ) {
    _paint = paint;
    _bus = bus;
  }

  /**
   * @param event
   */
  @Override
  public void actionPerformed ( ActionEvent event ) {
    _bus.post ( new PlayEvent ( _paint ) );
  }
}
