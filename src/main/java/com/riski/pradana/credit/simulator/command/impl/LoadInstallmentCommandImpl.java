package com.riski.pradana.credit.simulator.command.impl;

import com.riski.pradana.credit.simulator.client.InstallmentClient;
import com.riski.pradana.credit.simulator.client.model.Response;
import com.riski.pradana.credit.simulator.command.LoadInstallmentCommand;
import com.riski.pradana.credit.simulator.command.model.LoadInstallmentCommandRequest;

public class LoadInstallmentCommandImpl implements LoadInstallmentCommand {

  private final InstallmentClient installmentClient = new InstallmentClient();

  @Override
  public Response<String> execute(LoadInstallmentCommandRequest request) {
    return installmentClient.fetchInstallment();
  }
}
