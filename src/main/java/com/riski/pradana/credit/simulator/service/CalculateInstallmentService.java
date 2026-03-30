package com.riski.pradana.credit.simulator.service;

import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;

import java.util.List;

/**
 * Strategy for computing the installment schedule (per-year rate and monthly payment) from base rate,
 * principal, and tenure. Swappable for tests and alternate product rules.
 *
 * @see com.riski.pradana.credit.simulator.service.strategy.SteppedInterestInstallmentStrategy
 * @see com.riski.pradana.credit.simulator.service.strategy.FlatInterestInstallmentStrategy
 * @see com.riski.pradana.credit.simulator.service.strategy.InstallmentStrategyResolver
 */
public interface CalculateInstallmentService {
  List<CalculateInstallmentCommandResponse.Installment> calculateMonthlyInstallments(double baseRate,
      double totalLoan, int tenure);
}
