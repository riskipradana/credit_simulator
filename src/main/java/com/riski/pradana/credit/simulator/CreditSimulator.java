package com.riski.pradana.credit.simulator;

import com.riski.pradana.credit.simulator.client.model.InstallmentClientResponse;
import com.riski.pradana.credit.simulator.client.model.Response;
import com.riski.pradana.credit.simulator.command.CalculateInstallmentCommand;
import com.riski.pradana.credit.simulator.command.LoadInstallmentCommand;
import com.riski.pradana.credit.simulator.command.impl.CalculateInstallmentCommandImpl;
import com.riski.pradana.credit.simulator.command.impl.LoadInstallmentCommandImpl;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.command.model.LoadInstallmentCommandRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.Year;
import java.util.Locale;
import java.util.Scanner;

public class CreditSimulator {

  public static void main(String[] args) {
    boolean fromFile = args.length > 0;
    InputStream inputStream = System.in;
    if (fromFile) {
      try {
        inputStream = new FileInputStream(args[0]);
      } catch (FileNotFoundException e) {
        System.err.println("File not found: " + args[0]);
        System.exit(1);
      }
    }

    try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
      run(scanner, fromFile);
    }
  }

  private static void run(Scanner scanner, boolean fromFile) {

    CalculateInstallmentCommand calculateInstallmentCommand = new CalculateInstallmentCommandImpl();
    LoadInstallmentCommand loadInstallmentCommand = new LoadInstallmentCommandImpl();

    boolean running = true;

    while (running) {
      System.out.println("\n=== Credit Simulator ===");
      System.out.println("1. Load Existing Credit");
      System.out.println("2. Create Credit");
      System.out.println("3. Exit");
      System.out.print("Choose: ");

      if (!scanner.hasNextLine()) {
        break;
      }

      int choice = readChoice(scanner, fromFile);
      if (fromFile) {
        System.out.println(choice);
      }

      switch (choice) {
        case 1 -> {
          Response<InstallmentClientResponse> response = loadInstallmentCommand.execute(new LoadInstallmentCommandRequest());
          if (response.success()) {
            System.out.printf("Monthly Payment: %s%n", response.data().getDisplayValue());
          } else {
            System.out.println("Error = " + response.error());
          }
        }
        case 2 -> {
          String vehicleType = readOption(scanner, fromFile, "Jenis Kendaraan (Motor/Mobil): ", "Motor", "Mobil");
          String vehicleCondition = readOption(scanner, fromFile, "Kondisi Kendaraan (NEW/USED): ", "NEW", "USED");
          int vehicleYear = readVehicleYear(scanner, fromFile, "Tahun Kendaraan (4 digit): ", vehicleCondition);
          double totalLoan = readDouble(scanner, fromFile, "Jumlah Pinjaman (<=1,000,000,000): ", 1, 1_000_000_000);
          int tenor = readInt(scanner, fromFile, "Tenor Pinjaman (1-6 tahun): ", 1, 6);
          double dp = readDouble(scanner, fromFile, "Jumlah DP: ", 0, totalLoan);

          CalculateInstallmentCommandRequest request =
              new CalculateInstallmentCommandRequest(vehicleType, vehicleCondition, vehicleYear, totalLoan, tenor, dp);
          try {
            CalculateInstallmentCommandResponse response = calculateInstallmentCommand.execute(request);
            Locale indonesian = Locale.of("id", "ID");
            NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(indonesian);
            rupiahFormat.setMaximumFractionDigits(0);
            rupiahFormat.setMinimumFractionDigits(0);

            if (fromFile) {
              System.out.println();
            }
            for (CalculateInstallmentCommandResponse.Installment inst : response.installments()) {
              String formattedMonthly = rupiahFormat.format(inst.monthlyInstallment());
              System.out.printf("tahun %d : %s/bln, suku bunga : %.1f %%\n",
                  inst.year(),
                  formattedMonthly,
                  inst.interestRate());
            }
          } catch (IllegalArgumentException ex) {
            System.out.println("Input Error: " + ex.getMessage());
          }
        }
        case 3 -> running = false;
        default -> System.out.println("invalid option. choose correct option.");
      }
    }
  }

  private static int readChoice(Scanner scanner, boolean fromFile) {
    String line = scanner.nextLine().trim();
    try {
      return Integer.parseInt(line);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  private static String readOption(Scanner scanner, boolean fromFile, String prompt, String... options) {
    if (!fromFile) {
      System.out.print(prompt);
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
        System.out.println("Input salah. Pilih: " + String.join("/", options));
        System.out.print(prompt);
      } else {
        System.err.println("Input salah. Pilih: " + String.join("/", options) + " (got: " + input + ")");
        return options[0];
      }
    }
  }

  public static int readVehicleYear(Scanner scanner, boolean fromFile, String prompt, String vehicleCondition) {
    int currentYear = Year.now().getValue();
    while (true) {
      if (!fromFile) {
        System.out.print(prompt);
      }
      if (!scanner.hasNextLine()) {
        return currentYear;
      }
      String line = scanner.nextLine().trim();

      if (!line.matches("\\d{4}")) {
        if (fromFile) {
          System.err.println("Input salah. Masukkan 4 digit angka: " + line);
          return currentYear;
        }
        System.out.println("Input salah. Masukkan 4 digit angka.");
        continue;
      }

      int year;
      try {
        year = Integer.parseInt(line);
      } catch (NumberFormatException e) {
        if (fromFile) {
          return currentYear;
        }
        System.out.println("Input bukan angka yang valid.");
        continue;
      }

      if ("NEW".equalsIgnoreCase(vehicleCondition) && year < currentYear - 1) {
        if (fromFile) {
          System.err.println("Kendaraan Baru tidak boleh lebih tua dari tahun " + (currentYear - 1));
          return currentYear - 1;
        }
        System.out.println("Kendaraan Baru tidak boleh lebih tua dari tahun " + (currentYear - 1));
        continue;
      }

      return year;
    }
  }

  public static int readInt(Scanner scanner, boolean fromFile, String prompt, int min, int max) {
    if (!fromFile) {
      System.out.print(prompt);
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
        System.out.printf("Input salah. Masukkan angka antara %d-%d\n", min, max);
        System.out.print(prompt);
      } else {
        return min;
      }
    }
  }

  private static double readDouble(Scanner scanner, boolean fromFile, String prompt, double min, double max) {
    if (!fromFile) {
      System.out.print(prompt);
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
        System.out.printf("Input salah. Masukkan angka antara %.0f-%.0f\n", min, max);
        System.out.print(prompt);
      } else {
        return min;
      }
    }
  }
}
