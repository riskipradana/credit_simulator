package com.riski.pradana.credit.simulator.command;

import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.service.Command;

public interface CalculateInstallmentCommand extends Command<CalculateInstallmentCommandRequest, CalculateInstallmentCommandResponse> {
}
