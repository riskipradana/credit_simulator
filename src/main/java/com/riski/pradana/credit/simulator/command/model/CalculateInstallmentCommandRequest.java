package com.riski.pradana.credit.simulator.command.model;

public record CalculateInstallmentCommandRequest(
    String vehicleType,
    String vehicleCondition,
    int vehicleYear,
    double totalLoan,
    int tenure,
    double totalDownPayment) {
}
