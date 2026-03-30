package com.riski.pradana.credit.simulator;

import com.riski.pradana.credit.simulator.command.CalculateInstallmentCommand;
import com.riski.pradana.credit.simulator.command.LoadInstallmentCommand;
import com.riski.pradana.credit.simulator.command.impl.CalculateInstallmentCommandImpl;
import com.riski.pradana.credit.simulator.command.impl.LoadInstallmentCommandImpl;
import com.riski.pradana.credit.simulator.model.CreditSimulatorModel;
import com.riski.pradana.credit.simulator.model.DefaultCreditSimulatorModel;
import com.riski.pradana.credit.simulator.ui.ConsoleCreditSimulatorView;
import com.riski.pradana.credit.simulator.ui.CreditSimulatorController;
import com.riski.pradana.credit.simulator.ui.CreditSimulatorInput;
import com.riski.pradana.credit.simulator.ui.CreditSimulatorView;
import com.riski.pradana.credit.simulator.ui.ScannerCreditSimulatorInput;
import com.riski.pradana.credit.simulator.ui.menu.CreditSimulatorMenuCommandFactory;

import java.util.Scanner;

/**
 * Composition root: wires default command implementations and MVC stack. Use the all-args constructor
 * in tests to inject mocks or alternate implementations.
 */
public final class CreditSimulatorApplication {

  private final LoadInstallmentCommand loadInstallmentCommand;
  private final CalculateInstallmentCommand calculateInstallmentCommand;
  private final CreditSimulatorModel model;

  public CreditSimulatorApplication() {
    this(new LoadInstallmentCommandImpl(), new CalculateInstallmentCommandImpl());
  }

  public CreditSimulatorApplication(
      LoadInstallmentCommand loadInstallmentCommand,
      CalculateInstallmentCommand calculateInstallmentCommand) {
    this.loadInstallmentCommand = loadInstallmentCommand;
    this.calculateInstallmentCommand = calculateInstallmentCommand;
    this.model = new DefaultCreditSimulatorModel(loadInstallmentCommand, calculateInstallmentCommand);
  }

  public LoadInstallmentCommand loadInstallmentCommand() {
    return loadInstallmentCommand;
  }

  public CalculateInstallmentCommand calculateInstallmentCommand() {
    return calculateInstallmentCommand;
  }

  public CreditSimulatorModel model() {
    return model;
  }

  public CreditSimulatorController createController(Scanner scanner, boolean fromFile) {
    CreditSimulatorView view = new ConsoleCreditSimulatorView();
    CreditSimulatorInput input = new ScannerCreditSimulatorInput(scanner, fromFile, view);
    CreditSimulatorMenuCommandFactory menuFactory =
        new CreditSimulatorMenuCommandFactory(model, view, input, fromFile);
    return new CreditSimulatorController(menuFactory, view, input, fromFile);
  }
}
