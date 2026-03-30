package com.riski.pradana.credit.simulator.ui;

import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;

import java.text.NumberFormat;
import java.util.Locale;

public final class ConsoleCreditSimulatorView implements CreditSimulatorView {

  private final NumberFormat rupiahFormat;

  public ConsoleCreditSimulatorView() {
    Locale indonesian = Locale.of("id", "ID");
    NumberFormat nf = NumberFormat.getCurrencyInstance(indonesian);
    nf.setMaximumFractionDigits(0);
    nf.setMinimumFractionDigits(0);
    this.rupiahFormat = nf;
  }

  @Override
  public void showMenu() {
    System.out.println();
    System.out.println("=== Credit Simulator ===");
    System.out.println("1. Load Existing Credit");
    System.out.println("2. Create Credit");
    System.out.println("3. Exit");
  }

  @Override
  public void prompt(String text) {
    System.out.print(text);
  }

  @Override
  public void echoMenuChoice(int choice) {
    System.out.println(choice);
  }

  @Override
  public void showLoadSuccess(String displayValue) {
    System.out.printf("Monthly Payment: %s%n", displayValue);
  }

  @Override
  public void showLoadError(String error) {
    System.out.println("Error = " + error);
  }

  @Override
  public void blankLine() {
    System.out.println();
  }

  @Override
  public void showInstallmentLine(CalculateInstallmentCommandResponse.Installment installment) {
    String formattedMonthly = rupiahFormat.format(installment.monthlyInstallment());
    System.out.printf("tahun %d : %s/bln, suku bunga : %.1f %%\n",
        installment.year(),
        formattedMonthly,
        installment.interestRate());
  }

  @Override
  public void showInputError(String message) {
    System.out.println("Input Error: " + message);
  }

  @Override
  public void showInvalidMenuOption() {
    System.out.println("invalid option. choose correct option.");
  }

  @Override
  public void println(String line) {
    System.out.println(line);
  }

  @Override
  public void print(String text) {
    System.out.print(text);
  }

  @Override
  public void printErrorLn(String line) {
    System.err.println(line);
  }

  @Override
  public void printf(String format, Object... args) {
    System.out.printf(format, args);
  }
}
