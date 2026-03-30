package com.riski.pradana.credit.simulator.ui;

import com.riski.pradana.credit.simulator.ui.menu.CreditSimulatorMenuCommandFactory;

public final class CreditSimulatorController {

  private final CreditSimulatorMenuCommandFactory menuCommandFactory;
  private final CreditSimulatorView view;
  private final CreditSimulatorInput input;
  private final boolean fromFile;

  public CreditSimulatorController(
      CreditSimulatorMenuCommandFactory menuCommandFactory,
      CreditSimulatorView view,
      CreditSimulatorInput input,
      boolean fromFile) {
    this.menuCommandFactory = menuCommandFactory;
    this.view = view;
    this.input = input;
    this.fromFile = fromFile;
  }

  public void run() {
    boolean running = true;

    while (running) {
      view.showMenu();
      view.prompt("Choose: ");

      if (!input.hasNextLine()) {
        break;
      }

      int choice = input.readChoice();
      if (fromFile) {
        view.echoMenuChoice(choice);
      }

      running = menuCommandFactory.getAction(choice).execute();
    }
  }
}
