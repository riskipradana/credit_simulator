package com.riski.pradana.credit.simulator.ui.menu;

import com.riski.pradana.credit.simulator.client.model.InstallmentClientResponse;
import com.riski.pradana.credit.simulator.client.model.Response;
import com.riski.pradana.credit.simulator.model.CreditSimulatorModel;
import com.riski.pradana.credit.simulator.ui.CreditSimulatorView;

public final class LoadExistingCreditMenuAction implements SimulatorMenuAction {

  private final CreditSimulatorModel model;
  private final CreditSimulatorView view;

  public LoadExistingCreditMenuAction(CreditSimulatorModel model, CreditSimulatorView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public boolean execute() {
    Response<InstallmentClientResponse> response = model.loadExistingCredit();
    if (response.success()) {
      view.showLoadSuccess(response.data().getDisplayValue());
    } else {
      view.showLoadError(response.error());
    }
    return true;
  }
}
