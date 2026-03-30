package com.riski.pradana.credit.simulator.ui.menu;

import com.riski.pradana.credit.simulator.model.CreditSimulatorModel;
import com.riski.pradana.credit.simulator.ui.CreditSimulatorInput;
import com.riski.pradana.credit.simulator.ui.CreditSimulatorView;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry of menu actions (command factory): maps menu choice to the handler that runs that option.
 */
public final class CreditSimulatorMenuCommandFactory {

  private final Map<Integer, SimulatorMenuAction> actionsByChoice;
  private final SimulatorMenuAction invalidAction;

  public CreditSimulatorMenuCommandFactory(
      CreditSimulatorModel model,
      CreditSimulatorView view,
      CreditSimulatorInput input,
      boolean fromFile) {
    this.invalidAction = new InvalidMenuChoiceAction(view);
    Map<Integer, SimulatorMenuAction> map = new HashMap<>();
    map.put(1, new LoadExistingCreditMenuAction(model, view));
    map.put(2, new CreateCreditMenuAction(model, view, input, fromFile));
    map.put(3, new ExitSimulatorMenuAction());
    this.actionsByChoice = Map.copyOf(map);
  }

  public SimulatorMenuAction getAction(int menuChoice) {
    return actionsByChoice.getOrDefault(menuChoice, invalidAction);
  }
}
