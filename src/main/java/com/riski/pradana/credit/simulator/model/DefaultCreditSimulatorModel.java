package com.riski.pradana.credit.simulator.model;

import com.riski.pradana.credit.simulator.client.model.InstallmentClientResponse;
import com.riski.pradana.credit.simulator.client.model.Response;
import com.riski.pradana.credit.simulator.command.CalculateInstallmentCommand;
import com.riski.pradana.credit.simulator.command.LoadInstallmentCommand;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.command.model.LoadInstallmentCommandRequest;

public final class DefaultCreditSimulatorModel implements CreditSimulatorModel {

  private final LoadInstallmentCommand loadInstallmentCommand;
  private final CalculateInstallmentCommand calculateInstallmentCommand;

  public DefaultCreditSimulatorModel(
      LoadInstallmentCommand loadInstallmentCommand,
      CalculateInstallmentCommand calculateInstallmentCommand) {
    this.loadInstallmentCommand = loadInstallmentCommand;
    this.calculateInstallmentCommand = calculateInstallmentCommand;
  }

  @Override
  public Response<InstallmentClientResponse> loadExistingCredit() {
    return loadInstallmentCommand.execute(new LoadInstallmentCommandRequest());
  }

  @Override
  public CalculateInstallmentCommandResponse calculateCredit(CalculateInstallmentCommandRequest request) {
    return calculateInstallmentCommand.execute(request);
  }
}
