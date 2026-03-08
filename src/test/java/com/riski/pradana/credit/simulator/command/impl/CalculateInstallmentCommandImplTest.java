package com.riski.pradana.credit.simulator.command.impl;

import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.model.BaseInterestRate;
import com.riski.pradana.credit.simulator.service.CalculateInstallmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculateInstallmentCommandImplTest {

  private static final int CURRENT_YEAR = Year.now().getValue();

  @Mock
  private CalculateInstallmentService loanService;

  private CalculateInstallmentCommandImpl command;

  @BeforeEach
  void setUp() {
    command = new CalculateInstallmentCommandImpl(loanService);
  }

  @Test
  void shouldDelegateToServiceAndReturnResponse() {
    double totalLoan = 100_000_000;
    int tenure = 5;
    CalculateInstallmentCommandRequest request = new CalculateInstallmentCommandRequest(
        "Mobil", "USED", CURRENT_YEAR - 1, totalLoan, tenure, 0);

    List<CalculateInstallmentCommandResponse.Installment> expectedInstallments = List.of(
        new CalculateInstallmentCommandResponse.Installment(1, 2_000_000, 8.0),
        new CalculateInstallmentCommandResponse.Installment(2, 2_000_000, 8.1)
    );
    when(loanService.calculateMonthlyInstallments(
        eq((double) BaseInterestRate.car),
        eq(totalLoan),
        eq(tenure)))
        .thenReturn(expectedInstallments);

    CalculateInstallmentCommandResponse response = command.execute(request);

    assertNotNull(response);
    assertEquals(expectedInstallments, response.installments());
    verify(loanService).calculateMonthlyInstallments(
        (double) BaseInterestRate.car,
        totalLoan,
        tenure);
  }

  @Test
  void noArgConstructorCreatesWorkingCommand() {
    CalculateInstallmentCommandImpl defaultCommand = new CalculateInstallmentCommandImpl();
    CalculateInstallmentCommandRequest request = new CalculateInstallmentCommandRequest(
        "Motor", "USED", CURRENT_YEAR - 1, 10_000_000, 1, 0);
    CalculateInstallmentCommandResponse response = defaultCommand.execute(request);
    assertNotNull(response);
    assertNotNull(response.installments());
    assertEquals(1, response.installments().size());
  }
}
