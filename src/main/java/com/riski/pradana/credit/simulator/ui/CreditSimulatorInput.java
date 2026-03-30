package com.riski.pradana.credit.simulator.ui;

/**
 * MVC input: reads from a character source (e.g. Scanner); uses View for interactive prompts.
 */
public interface CreditSimulatorInput {

  boolean hasNextLine();

  int readChoice();

  String readOption(String prompt, String... options);

  int readVehicleYear(String prompt, String vehicleCondition);

  int readInt(String prompt, int min, int max);

  double readDouble(String prompt, double min, double max);
}
