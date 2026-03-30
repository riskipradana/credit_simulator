package com.riski.pradana.credit.simulator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
      new CreditSimulatorApplication().createController(scanner, fromFile).run();
    }
  }
}
