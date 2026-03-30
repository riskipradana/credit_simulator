package com.riski.pradana.credit.simulator.ui.menu;

/**
 * One menu option's behavior. {@code false} means stop the application loop (exit).
 */
@FunctionalInterface
public interface SimulatorMenuAction {

  /**
   * @return {@code true} to show the menu again; {@code false} to exit the simulator.
   */
  boolean execute();
}
