package com.riski.pradana.credit.simulator.command.impl;

import com.riski.pradana.credit.simulator.client.InstallmentClient;
import com.riski.pradana.credit.simulator.client.InstallmentClientConfig;
import com.riski.pradana.credit.simulator.client.InstallmentClientImpl;
import com.riski.pradana.credit.simulator.client.model.InstallmentClientResponse;
import com.riski.pradana.credit.simulator.client.model.Response;
import com.riski.pradana.credit.simulator.command.LoadInstallmentCommand;
import com.riski.pradana.credit.simulator.command.model.LoadInstallmentCommandRequest;

public class LoadInstallmentCommandImpl implements LoadInstallmentCommand {

  private final InstallmentClient installmentClient;

  public LoadInstallmentCommandImpl() {
    this(new InstallmentClientImpl(InstallmentClientConfig.defaultConfig()));
  }

  public LoadInstallmentCommandImpl(InstallmentClient installmentClient) {
    this.installmentClient = installmentClient;
  }

  @Override
  public Response<InstallmentClientResponse> execute(LoadInstallmentCommandRequest request) {
    return installmentClient.fetchInstallment();
  }
}
