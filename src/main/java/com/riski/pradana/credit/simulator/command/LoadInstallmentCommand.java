package com.riski.pradana.credit.simulator.command;

import com.riski.pradana.credit.simulator.command.model.LoadInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.LoadInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.service.Command;

public interface LoadInstallmentCommand extends Command<LoadInstallmentCommandRequest, LoadInstallmentCommandResponse> {
}
