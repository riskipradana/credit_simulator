package com.riski.pradana.credit.simulator.ui.menu;

import com.riski.pradana.credit.simulator.ui.CreditSimulatorView;

public final class InvalidMenuChoiceAction implements SimulatorMenuAction {

  private final CreditSimulatorView view;

  public InvalidMenuChoiceAction(CreditSimulatorView view) {
    this.view = view;
  }

  @Override
  public boolean execute() {
    view.showInvalidMenuOption();
    return true;
  }
}
