package ch.hesso.aigs.game.events;

import ch.hesso.aigs.game.Cell;

import java.util.Set;

/**
 *
 */
public final class GameOverEvent extends PlayedEvent {

  /**
   * @param affected
   */
  public GameOverEvent ( final Set<Cell> affected ) {
    super ( affected, 0 );
  }

}
