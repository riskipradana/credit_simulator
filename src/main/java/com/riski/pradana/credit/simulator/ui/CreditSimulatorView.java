package com.riski.pradana.credit.simulator.ui;

import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;

/**
 * MVC View: console output only (passive view).
 */
public interface CreditSimulatorView {

  void showMenu();

  void prompt(String text);

  void echoMenuChoice(int choice);

  void showLoadSuccess(String displayValue);

  void showLoadError(String error);

  void blankLine();

  void showInstallmentLine(CalculateInstallmentCommandResponse.Installment installment);

  void showInputError(String message);

  void showInvalidMenuOption();

  void println(String line);

  void print(String text);

  void printErrorLn(String line);

  void printf(String format, Object... args);
}
