package com.riski.pradana.credit.simulator.client;

import com.riski.pradana.credit.simulator.client.model.InstallmentClientResponse;
import com.riski.pradana.credit.simulator.client.model.Response;

public interface InstallmentClient {

  Response<InstallmentClientResponse> fetchInstallment();
}
