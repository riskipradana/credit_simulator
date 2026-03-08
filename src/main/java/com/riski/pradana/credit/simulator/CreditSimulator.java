package com.riski.pradana.credit.simulator;

import com.riski.pradana.credit.simulator.client.model.Response;
import com.riski.pradana.credit.simulator.command.CalculateInstallmentCommand;
import com.riski.pradana.credit.simulator.command.LoadInstallmentCommand;
import com.riski.pradana.credit.simulator.command.impl.CalculateInstallmentCommandImpl;
import com.riski.pradana.credit.simulator.command.impl.LoadInstallmentCommandImpl;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.command.model.LoadInstallmentCommandRequest;

import java.text.NumberFormat;
import java.time.Year;
import java.util.Locale;
import java.util.Scanner;

public class CreditSimulator {

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    CalculateInstallmentCommand calculateInstallmentCommand = new CalculateInstallmentCommandImpl();
    LoadInstallmentCommand loadInstallmentCommand = new LoadInstallmentCommandImpl();

    boolean running = true;

    while (running) {
      System.out.println("\n=== Credit Simulator ===");
      System.out.println("1. Load Existing Credit");
      System.out.println("2. Create Credit");
      System.out.println("3. Exit");
      System.out.print("Choose: ");

      int choice = scanner.nextInt();
      scanner.nextLine();

      switch (choice) {
        case 1 -> {
          Response<String> response = loadInstallmentCommand.execute(new LoadInstallmentCommandRequest());
          if (response.success()) {
            System.out.printf("Monthly Paymment: ", response.data());
          } else {
            System.out.println("Error = " + response.error());
          }
        }
        case 2 -> {
          String vehicleType = readOption(scanner, "Jenis Kendaraan (Motor/Mobil): ", "Motor", "Mobil");
          String vehicleCondition = readOption(scanner, "Kondisi Kendaraan (NEW/USED): ", "NEW", "USED");
          int vehicleYear = readVehicleYear(scanner, "Tahun Kendaraan (4 digit): ", vehicleCondition);
          double totalLoan = readDouble(scanner, "Jumlah Pinjaman (<=1,000,000,000): ", 1, 1_000_000_000);
          int tenor = readInt(scanner, "Tenor Pinjaman (1-6 tahun): ", 1, 6);
          double dp = readDouble(scanner, "Jumlah DP: ", 0, totalLoan);

          CalculateInstallmentCommandRequest request =
              new CalculateInstallmentCommandRequest(vehicleType, vehicleCondition, vehicleYear, totalLoan, tenor, dp);
          try {
            CalculateInstallmentCommandResponse response = calculateInstallmentCommand.execute(request);
            Locale indonesian = Locale.of("id", "ID");
            NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(indonesian);
            rupiahFormat.setMaximumFractionDigits(0);
            rupiahFormat.setMinimumFractionDigits(0);

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

  private static String readOption(Scanner scanner, String prompt, String... options) {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine().trim();
      for (String option : options) {
        if (input.equalsIgnoreCase(option))
          return input;
      }
      System.out.println("Input salah. Pilih: " + String.join("/", options));
    }
  }

  public static int readVehicleYear(Scanner scanner, String prompt, String vehicleCondition) {
    int currentYear = Year.now().getValue();
    while (true) {
      System.out.print(prompt);
      String line = scanner.nextLine().trim();

      if (!line.matches("\\d{4}")) {
        System.out.println("Input salah. Masukkan 4 digit angka.");
        continue;
      }

      int year;
      try {
        year = Integer.parseInt(line);
      } catch (NumberFormatException e) {
        System.out.println("Input bukan angka yang valid.");
        continue;
      }

      if ("NEW".equalsIgnoreCase(vehicleCondition) && year < currentYear - 1) {
        System.out.println("Kendaraan Baru tidak boleh lebih tua dari tahun " + (currentYear - 1));
        continue;
      }

      return year;
    }
  }

  public static int readInt(Scanner scanner, String prompt, int min, int max) {
    while (true) {
      System.out.print(prompt);
      try {
        int val = Integer.parseInt(scanner.nextLine().trim());
        if (val >= min && val <= max) return val;
      } catch (NumberFormatException ignored) {}
      System.out.printf("Input salah. Masukkan angka antara %d-%d\n", min, max);
    }
  }

  private static double readDouble(Scanner scanner, String prompt, double min, double max) {
    while (true) {
      System.out.print(prompt);
      try {
        double val = Double.parseDouble(scanner.nextLine().trim());
        if (val >= min && val <= max)
          return val;
      } catch (NumberFormatException ignored) {
      }
      System.out.printf("Input salah. Masukkan angka antara %.0f-%.0f\n", min, max);
    }
  }
}
