package com.riski.pradana.credit.simulator.command;

import com.riski.pradana.credit.simulator.client.model.InstallmentClientResponse;
import com.riski.pradana.credit.simulator.client.model.Response;
import com.riski.pradana.credit.simulator.command.model.LoadInstallmentCommandRequest;

public interface LoadInstallmentCommand extends Command<LoadInstallmentCommandRequest, Response<InstallmentClientResponse>> {
}
