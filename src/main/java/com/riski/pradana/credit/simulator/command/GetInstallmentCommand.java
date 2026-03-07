package com.riski.pradana.credit.simulator.command;

import com.riski.pradana.credit.simulator.command.model.GetInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.GetInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.service.Command;

public class GetInstallmentCommand implements Command<GetInstallmentCommandRequest, GetInstallmentCommandResponse> {

  @Override
  public GetInstallmentCommandResponse execute(GetInstallmentCommandRequest request) {
    return null;
  }
}
