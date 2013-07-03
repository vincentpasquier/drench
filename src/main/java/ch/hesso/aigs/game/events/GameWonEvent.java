package ch.hesso.aigs.game.events;

import ch.hesso.aigs.game.Cell;

import java.util.Set;

/**
 *
 */
public final class GameWonEvent extends PlayedEvent {

  /**
   * @param affected
   * @param turns
   */
  public GameWonEvent ( final Set<Cell> affected, final int turns ) {
    super ( affected, turns );
  }
}
