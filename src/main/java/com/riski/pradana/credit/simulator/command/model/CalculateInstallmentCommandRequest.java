package com.riski.pradana.credit.simulator.command.model;

public record CalculateInstallmentCommandRequest(String vehicleType, String condition, String modelYear,
                                                 double totalLoan, int tenure, double totalDownPayment) {}
