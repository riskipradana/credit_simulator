package com.riski.pradana.credit.simulator.command.impl;

import com.riski.pradana.credit.simulator.CreditSimulator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreditSimulatorFileInputTest {

  private static final int CURRENT_YEAR = Year.now().getValue();

  private PrintStream originalOut;
  private ByteArrayOutputStream capturedOut;

  @BeforeEach
  void captureStdout() {
    originalOut = System.out;
    capturedOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(capturedOut, true, StandardCharsets.UTF_8));
  }

  @AfterEach
  void restoreStdout() {
    System.setOut(originalOut);
  }

  @Test
  void shouldReadInputFromFileAndPrintInstallments(@TempDir Path tempDir) throws Exception {
    // File content: menu 2 (Create Credit), Mobil, NEW, year, 100M, 5 yr, 35M DP, then 3 (Exit)
    String content = String.join("\n",
        "2",
        "Mobil",
        "NEW",
        String.valueOf(CURRENT_YEAR),
        "100000000",
        "5",
        "35000000",
        "3"
    );
    Path inputFile = tempDir.resolve("file_inputs.txt");
    Files.writeString(inputFile, content, StandardCharsets.UTF_8);

    assertDoesNotThrow(() -> CreditSimulator.main(new String[] { inputFile.toAbsolutePath().toString() }));

    String output = capturedOut.toString(StandardCharsets.UTF_8);
    assertFalse(output.contains("Input Error:"), "Expected no validation error; output: " + output);
    assertTrue(output.contains("=== Credit Simulator ==="), "Should show menu");
    assertTrue(output.contains("tahun "), "Should print installment lines (contains 'tahun '). Output: " + output);
    assertTrue(output.contains("suku bunga "), "Should print interest rate (contains 'suku bunga ')");
    assertTrue(output.contains("/bln"), "Should print per-month amount");
    // Car rate 8% for year 1, 9.2% for year 5
    assertTrue(output.contains("8") && output.contains("9.2"), "Should show interest rates 8% and 9.2%");
  }

  @Test
  void shouldReadMotorBaruFromFileAndPrintInstallments(@TempDir Path tempDir) throws Exception {
    String content = String.join("\n",
        "2",
        "Motor",
        "NEW",
        String.valueOf(CURRENT_YEAR),
        "20000000",
        "3",
        "5000000",
        "3"
    );
    Path inputFile = tempDir.resolve("motor_inputs.txt");
    Files.writeString(inputFile, content, StandardCharsets.UTF_8);

    assertDoesNotThrow(() -> CreditSimulator.main(new String[] { inputFile.toAbsolutePath().toString() }));

    String output = capturedOut.toString(StandardCharsets.UTF_8);
    assertTrue(output.contains("tahun "), "Should print installment lines");
    assertTrue(output.contains("suku bunga "), "Should print interest rate");
    assertTrue(output.contains("9 ") || output.contains("9.0"), "Should show motorcycle base rate 9%");
    assertTrue(output.contains("9.6"), "Should show rate 9.6% for year 3");
  }

  @Test
  void shouldProcessMultipleCreditsFromFile(@TempDir Path tempDir) throws Exception {
    // First Create Credit (Mobil USED), then Exit
    String content = String.join("\n",
        "2",
        "Mobil",
        "USED",
        String.valueOf(CURRENT_YEAR - 2),
        "50000000",
        "2",
        "5000000",
        "3"
    );
    Path inputFile = tempDir.resolve("multi_inputs.txt");
    Files.writeString(inputFile, content, StandardCharsets.UTF_8);

    assertDoesNotThrow(() -> CreditSimulator.main(new String[] { inputFile.toAbsolutePath().toString() }));

    String output = capturedOut.toString(StandardCharsets.UTF_8);
    assertTrue(output.contains("tahun "), "Should print installments for first credit");
    assertTrue(output.contains("suku bunga "), "Should print interest rate");
    assertTrue(output.contains("8") || output.contains("8.0"), "Mobil Bekas uses car rate 8%");
  }

  @Test
  void shouldExitWhenFileContainsExitChoice(@TempDir Path tempDir) throws Exception {
    String content = String.join("\n", "3");
    Path inputFile = tempDir.resolve("exit_only.txt");
    Files.writeString(inputFile, content, StandardCharsets.UTF_8);

    assertDoesNotThrow(() -> CreditSimulator.main(new String[] { inputFile.toAbsolutePath().toString() }));

    String output = capturedOut.toString(StandardCharsets.UTF_8);
    assertTrue(output.contains("=== Credit Simulator ==="));
    assertTrue(output.contains("3. Exit"));
  }
}