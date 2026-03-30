package com.riski.pradana.credit.simulator.model;

import com.riski.pradana.credit.simulator.client.model.InstallmentClientResponse;
import com.riski.pradana.credit.simulator.client.model.Response;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;

/**
 * MVC Model façade: application operations used by the controller (delegates to commands).
 */
public interface CreditSimulatorModel {

  Response<InstallmentClientResponse> loadExistingCredit();

  CalculateInstallmentCommandResponse calculateCredit(CalculateInstallmentCommandRequest request);
}
