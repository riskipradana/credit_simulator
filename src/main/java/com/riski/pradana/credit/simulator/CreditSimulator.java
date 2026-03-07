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
          System.out.print("Enter Vehicle (Motor|Mobil): ");
          String vehicleType = scanner.next();
//          System.out.print("Enter condition (Baru|Bekas): ");
//          String condition = scanner.next();
//          System.out.print("Enter Model Year:  ");
//          String modelYear = scanner.next();
          System.out.print("Enter total loan:  ");
          double totalLoan = scanner.nextDouble();
          System.out.print("Enter tenure:  ");
          int tenure = scanner.nextInt();
          System.out.print("Enter total down payment:  ");
          double totalDownPayment = scanner.nextDouble();
          CalculateInstallmentCommandRequest request = new CalculateInstallmentCommandRequest(vehicleType, "baru",
              "2022", totalLoan, tenure, totalDownPayment);
          CalculateInstallmentCommandResponse response = calculateInstallmentCommand.execute(request);

          Locale indonesian = Locale.of("id", "ID");
          NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(indonesian);

          for (CalculateInstallmentCommandResponse.Installment inst : response.installments()) {
            String formattedMonthly = rupiahFormat.format(inst.monthlyInstallment());
            System.out.printf("tahun %d : %s /bln, suku bunga : %.2f %%\n",
                inst.year(), formattedMonthly, inst.interestRate());
          }
        }
        case 3 -> running = false;
        default -> System.out.println("invalid option. choose correct option.");
      }
    }

  }
}
