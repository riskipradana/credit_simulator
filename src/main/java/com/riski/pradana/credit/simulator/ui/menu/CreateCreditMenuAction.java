package com.riski.pradana.credit.simulator.ui.menu;

import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.model.CreditSimulatorModel;
import com.riski.pradana.credit.simulator.ui.CreditSimulatorInput;
import com.riski.pradana.credit.simulator.ui.CreditSimulatorView;

public final class CreateCreditMenuAction implements SimulatorMenuAction {

  private final CreditSimulatorModel model;
  private final CreditSimulatorView view;
  private final CreditSimulatorInput input;
  private final boolean fromFile;

  public CreateCreditMenuAction(
      CreditSimulatorModel model,
      CreditSimulatorView view,
      CreditSimulatorInput input,
      boolean fromFile) {
    this.model = model;
    this.view = view;
    this.input = input;
    this.fromFile = fromFile;
  }

  @Override
  public boolean execute() {
    String vehicleType = input.readOption("Jenis Kendaraan (Motor/Mobil): ", "Motor", "Mobil");
    String vehicleCondition = input.readOption("Kondisi Kendaraan (NEW/USED): ", "NEW", "USED");
    int vehicleYear = input.readVehicleYear("Tahun Kendaraan (4 digit): ", vehicleCondition);
    double totalLoan = input.readDouble("Jumlah Pinjaman (<=1,000,000,000): ", 1, 1_000_000_000);
    int tenor = input.readInt("Tenor Pinjaman (1-6 tahun): ", 1, 6);
    double dp = input.readDouble("Jumlah DP: ", 0, totalLoan);

    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest(vehicleType, vehicleCondition, vehicleYear, totalLoan, tenor, dp);
    try {
      CalculateInstallmentCommandResponse response = model.calculateCredit(request);
      if (fromFile) {
        view.blankLine();
      }
      for (CalculateInstallmentCommandResponse.Installment inst : response.installments()) {
        view.showInstallmentLine(inst);
      }
    } catch (IllegalArgumentException ex) {
      view.showInputError(ex.getMessage());
    }
    return true;
  }
}
