package com.riski.pradana.credit.simulator.ui;

import java.time.Year;
import java.util.Scanner;

public final class ScannerCreditSimulatorInput implements CreditSimulatorInput {

  private final Scanner scanner;
  private final boolean fromFile;
  private final CreditSimulatorView view;

  public ScannerCreditSimulatorInput(Scanner scanner, boolean fromFile, CreditSimulatorView view) {
    this.scanner = scanner;
    this.fromFile = fromFile;
    this.view = view;
  }

  @Override
  public boolean hasNextLine() {
    return scanner.hasNextLine();
  }

  @Override
  public int readChoice() {
    String line = scanner.nextLine().trim();
    try {
      return Integer.parseInt(line);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  @Override
  public String readOption(String prompt, String... options) {
    if (!fromFile) {
      view.prompt(prompt);
    }
    while (true) {
      if (!scanner.hasNextLine()) {
        return options[0];
      }
      String input = scanner.nextLine().trim();
      for (String option : options) {
        if (input.equalsIgnoreCase(option)) {
          return input;
        }
      }
      if (!fromFile) {
        view.println("Input salah. Pilih: " + String.join("/", options));
        view.prompt(prompt);
      } else {
        view.printErrorLn("Input salah. Pilih: " + String.join("/", options) + " (got: " + input + ")");
        return options[0];
      }
    }
  }

  @Override
  public int readVehicleYear(String prompt, String vehicleCondition) {
    int currentYear = Year.now().getValue();
    while (true) {
      if (!fromFile) {
        view.prompt(prompt);
      }
      if (!scanner.hasNextLine()) {
        return currentYear;
      }
      String line = scanner.nextLine().trim();

      if (!line.matches("\\d{4}")) {
        if (fromFile) {
          view.printErrorLn("Input salah. Masukkan 4 digit angka: " + line);
          return currentYear;
        }
        view.println("Input salah. Masukkan 4 digit angka.");
        continue;
      }

      int year;
      try {
        year = Integer.parseInt(line);
      } catch (NumberFormatException e) {
        if (fromFile) {
          return currentYear;
        }
        view.println("Input bukan angka yang valid.");
        continue;
      }

      if ("NEW".equalsIgnoreCase(vehicleCondition) && year < currentYear - 1) {
        if (fromFile) {
          view.printErrorLn("Kendaraan Baru tidak boleh lebih tua dari tahun " + (currentYear - 1));
          return currentYear - 1;
        }
        view.println("Kendaraan Baru tidak boleh lebih tua dari tahun " + (currentYear - 1));
        continue;
      }

      return year;
    }
  }

  @Override
  public int readInt(String prompt, int min, int max) {
    if (!fromFile) {
      view.prompt(prompt);
    }
    while (true) {
      if (!scanner.hasNextLine()) {
        return min;
      }
      try {
        int val = Integer.parseInt(scanner.nextLine().trim());
        if (val >= min && val <= max) {
          return val;
        }
      } catch (NumberFormatException ignored) {
      }
      if (!fromFile) {
        view.printf("Input salah. Masukkan angka antara %d-%d\n", min, max);
        view.prompt(prompt);
      } else {
        return min;
      }
    }
  }

  @Override
  public double readDouble(String prompt, double min, double max) {
    if (!fromFile) {
      view.prompt(prompt);
    }
    while (true) {
      if (!scanner.hasNextLine()) {
        return min;
      }
      try {
        double val = Double.parseDouble(scanner.nextLine().trim());
        if (val >= min && val <= max) {
          return val;
        }
      } catch (NumberFormatException ignored) {
      }
      if (!fromFile) {
        view.printf("Input salah. Masukkan angka antara %.0f-%.0f\n", min, max);
        view.prompt(prompt);
      } else {
        return min;
      }
    }
  }
}
