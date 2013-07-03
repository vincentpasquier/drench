package ch.hesso.aigs.game.events;

import ch.hesso.aigs.game.Cell;

import java.util.Set;

/**
 *
 */
public class PlayedEvent {

  //
  private final Set<Cell> _affected;

  //
  private final int _turns;

  /**
   * @param affected
   * @param turns
   */
  public PlayedEvent ( final Set<Cell> affected, final int turns ) {
    _affected = affected;
    _turns = turns;
  }

  /**
   * @return
   */
  public Set<Cell> affected () {
    return _affected;
  }

  /**
   * @return
   */
  public int turns () {
    return _turns;
  }
}