package com.riski.pradana.credit.simulator.service;

import com.riski.pradana.credit.simulator.command.impl.CalculateInstallmentCommandImpl;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandRequest;
import com.riski.pradana.credit.simulator.command.model.CalculateInstallmentCommandResponse;
import com.riski.pradana.credit.simulator.constant.BaseInterestRate;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculateInstallmentServiceTest {

  private final CalculateInstallmentCommandImpl command = new CalculateInstallmentCommandImpl();

  private static final int CURRENT_YEAR = Year.now().getValue();
  private static final double MONTHLY_DELTA = 0.01;
  private static final double RATE_DELTA = 0.001;

  private static double expectedMonthlyInstallment(double totalLoan, int tenure, double interestRate) {
    double baseInstallment = totalLoan / (tenure * 12);
    double monthlyInterest = (totalLoan * interestRate / 100) / 12;
    return baseInstallment + monthlyInterest;
  }

  private static void assertInstallmentDetail(CalculateInstallmentCommandResponse.Installment installment,
      int expectedYear,
      double expectedInterestRate,
      double expectedMonthlyInstallment) {
    assertEquals(expectedYear, installment.year(), "year");
    assertEquals(expectedInterestRate, installment.interestRate(), RATE_DELTA, "interestRate");
    assertEquals(expectedMonthlyInstallment, installment.monthlyInstallment(), MONTHLY_DELTA, "monthlyInstallment");
  }

  @Test
  void shouldCreateCreditSuccessfully_MobilBaru() {
    int year = CURRENT_YEAR;
    double totalLoan = 100_000_000;
    int tenure = 5;
    double dp = 35_000_000; // 35%
    CalculateInstallmentCommandRequest commandRequest =
        new CalculateInstallmentCommandRequest("mobil", "NEW", year, totalLoan, tenure, dp);
    CalculateInstallmentCommandResponse response = command.execute(commandRequest);
    assertNotNull(response);
    assertFalse(response.installments().isEmpty());
    List<CalculateInstallmentCommandResponse.Installment> installments = response.installments();
    assertEquals(tenure, installments.size());
    assertInstallmentDetail(installments.get(0), 1, 8, expectedMonthlyInstallment(totalLoan, tenure, 8));
    assertInstallmentDetail(installments.get(1), 2, 8.1, expectedMonthlyInstallment(totalLoan, tenure, 8.1));
    assertInstallmentDetail(installments.get(2), 3, 8.6, expectedMonthlyInstallment(totalLoan, tenure, 8.6));
    assertInstallmentDetail(installments.get(3), 4, 8.7, expectedMonthlyInstallment(totalLoan, tenure, 8.7));
    assertInstallmentDetail(installments.get(4), 5, 9.2, expectedMonthlyInstallment(totalLoan, tenure, 9.2));
  }

  @Test
  void shouldCreateCreditSuccessfully_MotorBaru() {
    int year = CURRENT_YEAR;
    double totalLoan = 20_000_000;
    int tenure = 3;
    double dp = 5_000_000; // 25%
    CalculateInstallmentCommandRequest commandRequest =
        new CalculateInstallmentCommandRequest("Motor", "NEW", year, totalLoan, tenure, dp);
    CalculateInstallmentCommandResponse response = command.execute(commandRequest);
    assertNotNull(response);
    List<CalculateInstallmentCommandResponse.Installment> installments = response.installments();
    assertEquals(3, installments.size());
    assertInstallmentDetail(installments.get(0), 1, 9, expectedMonthlyInstallment(totalLoan, tenure, 9));
    assertInstallmentDetail(installments.get(1), 2, 9.1, expectedMonthlyInstallment(totalLoan, tenure, 9.1));
    assertInstallmentDetail(installments.get(2), 3, 9.6, expectedMonthlyInstallment(totalLoan, tenure, 9.6));
  }

  @Test
  void shouldCreateCreditSuccessfully_MobilBekas() {
    int year = CURRENT_YEAR - 5;
    double totalLoan = 50_000_000;
    int tenure = 2;
    double dp = 5_000_000; // 10%, no minimum for Bekas
    CalculateInstallmentCommandRequest commandRequest =
        new CalculateInstallmentCommandRequest("Mobil", "USED", year, totalLoan, tenure, dp);
    CalculateInstallmentCommandResponse response = command.execute(commandRequest);
    assertNotNull(response);
    List<CalculateInstallmentCommandResponse.Installment> installments = response.installments();
    assertEquals(2, installments.size());
    assertInstallmentDetail(installments.get(0), 1, 8, expectedMonthlyInstallment(totalLoan, tenure, 8));
    assertInstallmentDetail(installments.get(1), 2, 8.1, expectedMonthlyInstallment(totalLoan, tenure, 8.1));
  }

  @Test
  void shouldCreateCreditSuccessfully_MotorBekas() {
    double totalLoan = 15_000_000;
    CalculateInstallmentCommandRequest commandRequest =
        new CalculateInstallmentCommandRequest("motor", "USED", CURRENT_YEAR - 2, totalLoan, 1, 1_000_000);
    CalculateInstallmentCommandResponse response = command.execute(commandRequest);
    assertNotNull(response);
    List<CalculateInstallmentCommandResponse.Installment> installments = response.installments();
    assertEquals(1, installments.size());
    assertInstallmentDetail(installments.get(0),
        1,
        BaseInterestRate.motorcycle,
        expectedMonthlyInstallment(totalLoan, 1, BaseInterestRate.motorcycle));
  }

  @Test
  void shouldUseBaseRateForFirstYearOnlyWhenTenureOne() {
    double totalLoan = 12_000_000;
    int tenure = 1;
    CalculateInstallmentCommandRequest commandRequest =
        new CalculateInstallmentCommandRequest("Mobil", "USED", CURRENT_YEAR - 1, totalLoan, tenure, 0);
    CalculateInstallmentCommandResponse response = command.execute(commandRequest);
    List<CalculateInstallmentCommandResponse.Installment> installments = response.installments();
    assertEquals(1, installments.size());
    double expectedMonthly = expectedMonthlyInstallment(totalLoan, tenure, BaseInterestRate.car);
    assertInstallmentDetail(installments.get(0), 1, BaseInterestRate.car, expectedMonthly);
  }

  @Test
  void shouldApplyInterestProgressionForMultipleYears() {
    double totalLoan = 24_000_000;
    int tenure = 3;
    CalculateInstallmentCommandRequest commandRequest =
        new CalculateInstallmentCommandRequest("Motor", "USED", CURRENT_YEAR - 1, totalLoan, tenure, 0);
    CalculateInstallmentCommandResponse response = command.execute(commandRequest);
    List<CalculateInstallmentCommandResponse.Installment> installments = response.installments();
    assertEquals(3, installments.size());
    assertInstallmentDetail(installments.get(0), 1, 9, expectedMonthlyInstallment(totalLoan, tenure, 9));
    assertInstallmentDetail(installments.get(1), 2, 9.1, expectedMonthlyInstallment(totalLoan, tenure, 9.1));
    assertInstallmentDetail(installments.get(2), 3, 9.6, expectedMonthlyInstallment(totalLoan, tenure, 9.6));
  }

  @Test
  void shouldAcceptTenureOneAndSix() {
    double totalLoan = 10_000_000;
    CalculateInstallmentCommandRequest req1 =
        new CalculateInstallmentCommandRequest("Mobil", "USED", CURRENT_YEAR - 1, totalLoan, 1, 0);
    List<CalculateInstallmentCommandResponse.Installment> one = command.execute(req1).installments();
    assertEquals(1, one.size());
    assertInstallmentDetail(one.get(0), 1, 8, expectedMonthlyInstallment(totalLoan, 1, 8));

    CalculateInstallmentCommandRequest req6 =
        new CalculateInstallmentCommandRequest("Mobil", "USED", CURRENT_YEAR - 1, totalLoan, 6, 0);
    List<CalculateInstallmentCommandResponse.Installment> six = command.execute(req6).installments();
    assertEquals(6, six.size());
    assertInstallmentDetail(six.get(0), 1, 8, expectedMonthlyInstallment(totalLoan, 6, 8));
    assertInstallmentDetail(six.get(1), 2, 8.1, expectedMonthlyInstallment(totalLoan, 6, 8.1));
    assertInstallmentDetail(six.get(2), 3, 8.6, expectedMonthlyInstallment(totalLoan, 6, 8.6));
    assertInstallmentDetail(six.get(3), 4, 8.7, expectedMonthlyInstallment(totalLoan, 6, 8.7));
    assertInstallmentDetail(six.get(4), 5, 9.2, expectedMonthlyInstallment(totalLoan, 6, 9.2));
    assertInstallmentDetail(six.get(5), 6, 9.3, expectedMonthlyInstallment(totalLoan, 6, 9.3));
  }

  @Test
  void shouldAcceptLoanAtBoundaries() {
    CalculateInstallmentCommandRequest minLoan =
        new CalculateInstallmentCommandRequest("Mobil", "USED", CURRENT_YEAR - 1, 1, 1, 0);
    assertNotNull(command.execute(minLoan));
    CalculateInstallmentCommandRequest maxLoan =
        new CalculateInstallmentCommandRequest("Mobil", "USED", CURRENT_YEAR - 1, 1_000_000_000, 1, 0);
    assertNotNull(command.execute(maxLoan));
  }

  @Test
  void shouldThrowWhenVehicleTypeInvalid() {
    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest("Truck", "NEW", CURRENT_YEAR, 100_000_000, 5, 35_000_000);
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> command.execute(request));
    assertEquals("Jenis Kendaraan harus Motor atau Mobil", ex.getMessage());
  }

  @Test
  void shouldThrowWhenVehicleConditionInvalid() {
    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest("Mobil", "Rusak", CURRENT_YEAR, 100_000_000, 5, 35_000_000);
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> command.execute(request));
    assertEquals("Kondisi Kendaraan harus NEW atau USED", ex.getMessage());
  }

  @Test
  void shouldThrowWhenBaruVehicleYearTooOld() {
    int tooOldYear = CURRENT_YEAR - 2;
    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest("Mobil", "NEW", tooOldYear, 100_000_000, 5, 35_000_000);
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> command.execute(request));
    assertEquals("Kendaraan Baru tidak boleh lebih tua dari tahun " + (CURRENT_YEAR - 1), ex.getMessage());
  }

  @Test
  void shouldThrowWhenTenureLessThanOne() {
    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest("Mobil", "USED", CURRENT_YEAR - 1, 100_000_000, 0, 0);
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> command.execute(request));
    assertEquals("Tenor harus 1-6 tahun", ex.getMessage());
  }

  @Test
  void shouldThrowWhenTenureGreaterThanSix() {
    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest("Mobil", "USED", CURRENT_YEAR - 1, 100_000_000, 7, 0);
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> command.execute(request));
    assertEquals("Tenor harus 1-6 tahun", ex.getMessage());
  }

  @Test
  void shouldThrowWhenTotalLoanZeroOrNegative() {
    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest("Mobil", "USED", CURRENT_YEAR - 1, 0, 5, 0);
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> command.execute(request));
    assertEquals("Jumlah Pinjaman harus <= 1.000.000.000", ex.getMessage());
  }

  @Test
  void shouldThrowWhenTotalLoanExceedsOneBillion() {
    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest("Mobil", "USED", CURRENT_YEAR - 1, 1_000_000_001, 5, 0);
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> command.execute(request));
    assertEquals("Jumlah Pinjaman harus <= 1.000.000.000", ex.getMessage());
  }

  @Test
  void shouldThrowWhenMobilBaruDpBelow35Percent() {
    double totalLoan = 100_000_000;
    double dpBelow35 = 34_999_999; // 34.999999%
    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest("Mobil", "NEW", CURRENT_YEAR, totalLoan, 5, dpBelow35);
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> command.execute(request));
    assertEquals("DP Mobil Baru minimal 35% dari total pinjaman", ex.getMessage());
  }

  @Test
  void shouldAcceptMobilBaruWhenDpExactly35Percent() {
    double totalLoan = 100_000_000;
    int tenure = 5;
    double dp35 = 35_000_000;
    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest("Mobil", "NEW", CURRENT_YEAR, totalLoan, tenure, dp35);
    CalculateInstallmentCommandResponse response = command.execute(request);
    assertNotNull(response);
    List<CalculateInstallmentCommandResponse.Installment> installments = response.installments();
    assertEquals(5, installments.size());
    assertInstallmentDetail(installments.get(0), 1, 8, expectedMonthlyInstallment(totalLoan, tenure, 8));
    assertInstallmentDetail(installments.get(1), 2, 8.1, expectedMonthlyInstallment(totalLoan, tenure, 8.1));
    assertInstallmentDetail(installments.get(2), 3, 8.6, expectedMonthlyInstallment(totalLoan, tenure, 8.6));
    assertInstallmentDetail(installments.get(3), 4, 8.7, expectedMonthlyInstallment(totalLoan, tenure, 8.7));
    assertInstallmentDetail(installments.get(4), 5, 9.2, expectedMonthlyInstallment(totalLoan, tenure, 9.2));
  }

  @Test
  void shouldThrowWhenMotorBaruDpBelow25Percent() {
    double totalLoan = 20_000_000;
    double dpBelow25 = 4_999_999; // 24.999995%
    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest("Motor", "NEW", CURRENT_YEAR, totalLoan, 3, dpBelow25);
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> command.execute(request));
    assertEquals("DP Motor Baru minimal 25% dari total pinjaman", ex.getMessage());
  }

  @Test
  void shouldAcceptMotorBaruWhenDpExactly25Percent() {
    double totalLoan = 20_000_000;
    int tenure = 3;
    double dp25 = 5_000_000;
    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest("Motor", "NEW", CURRENT_YEAR, totalLoan, tenure, dp25);
    CalculateInstallmentCommandResponse response = command.execute(request);
    assertNotNull(response);
    List<CalculateInstallmentCommandResponse.Installment> installments = response.installments();
    assertEquals(3, installments.size());
    assertInstallmentDetail(installments.get(0), 1, 9, expectedMonthlyInstallment(totalLoan, tenure, 9));
    assertInstallmentDetail(installments.get(1), 2, 9.1, expectedMonthlyInstallment(totalLoan, tenure, 9.1));
    assertInstallmentDetail(installments.get(2), 3, 9.6, expectedMonthlyInstallment(totalLoan, tenure, 9.6));
  }

  @Test
  void shouldReturnInstallmentsWithIncreasingRatesForTenureFour() {
    double totalLoan = 60_000_000;
    int tenure = 4;
    CalculateInstallmentCommandRequest request =
        new CalculateInstallmentCommandRequest("Mobil", "USED", CURRENT_YEAR - 1, totalLoan, tenure, 0);
    CalculateInstallmentCommandResponse response = command.execute(request);
    List<CalculateInstallmentCommandResponse.Installment> installments = response.installments();
    assertEquals(4, installments.size());
    assertInstallmentDetail(installments.get(0), 1, 8, expectedMonthlyInstallment(totalLoan, tenure, 8));
    assertInstallmentDetail(installments.get(1), 2, 8.1, expectedMonthlyInstallment(totalLoan, tenure, 8.1));
    assertInstallmentDetail(installments.get(2), 3, 8.6, expectedMonthlyInstallment(totalLoan, tenure, 8.6));
    assertInstallmentDetail(installments.get(3), 4, 8.7, expectedMonthlyInstallment(totalLoan, tenure, 8.7));
  }
}
