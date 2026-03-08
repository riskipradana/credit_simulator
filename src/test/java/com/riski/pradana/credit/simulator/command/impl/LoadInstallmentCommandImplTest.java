package com.riski.pradana.credit.simulator.command.impl;

import com.riski.pradana.credit.simulator.client.InstallmentClient;
import com.riski.pradana.credit.simulator.client.model.InstallmentClientResponse;
import com.riski.pradana.credit.simulator.client.model.Response;
import com.riski.pradana.credit.simulator.command.model.LoadInstallmentCommandRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoadInstallmentCommandImplTest {

  private static final int SUCCESS_CODE = 200;
  private static final int ERROR_CODE = 500;

  @Mock
  private InstallmentClient installmentClient;

  private LoadInstallmentCommandImpl command;

  @BeforeEach
  void setUp() {
    command = new LoadInstallmentCommandImpl(installmentClient);
  }

  @Test
  void shouldReturnSuccessResponseWhenClientReturnsSuccess() {
    LoadInstallmentCommandRequest request = new LoadInstallmentCommandRequest();
    InstallmentClientResponse expectedData = new InstallmentClientResponse("2000000", null);
    when(installmentClient.fetchInstallment()).thenReturn(Response.success(expectedData));

    Response<InstallmentClientResponse> response = command.execute(request);

    assertNotNull(response);
    assertEquals(SUCCESS_CODE, response.code());
    assertTrue(response.success());
    assertEquals(expectedData, response.data());
    assertEquals("2000000", response.data().getDisplayValue());
    assertNull(response.error());
    verify(installmentClient).fetchInstallment();
  }

  @Test
  void shouldReturnFailureResponseWhenClientReturnsFailure() {
    LoadInstallmentCommandRequest request = new LoadInstallmentCommandRequest();
    String expectedError = "Connection failed: timeout";
    when(installmentClient.fetchInstallment()).thenReturn(Response.failure(expectedError));

    Response<InstallmentClientResponse> response = command.execute(request);

    assertNotNull(response);
    assertEquals(ERROR_CODE, response.code());
    assertFalse(response.success());
    assertNull(response.data());
    assertEquals(expectedError, response.error());
    verify(installmentClient).fetchInstallment();
  }

  @Test
  void shouldReturnFailureWithCustomCodeWhenClientReturnsFailureWithCode() {
    LoadInstallmentCommandRequest request = new LoadInstallmentCommandRequest();
    int customCode = 404;
    String expectedError = "Not found";
    when(installmentClient.fetchInstallment()).thenReturn(Response.failure(customCode, expectedError));

    Response<InstallmentClientResponse> response = command.execute(request);

    assertNotNull(response);
    assertEquals(customCode, response.code());
    assertFalse(response.success());
    assertNull(response.data());
    assertEquals(expectedError, response.error());
    verify(installmentClient).fetchInstallment();
  }

  @Test
  void shouldDelegateToClientAndIgnoreRequest() {
    LoadInstallmentCommandRequest request = new LoadInstallmentCommandRequest();
    when(installmentClient.fetchInstallment()).thenReturn(Response.success(new InstallmentClientResponse("ok", null)));

    command.execute(request);

    verify(installmentClient).fetchInstallment();
  }

  @Test
  void defaultConstructorCreatesWorkingCommand() {
    LoadInstallmentCommandImpl defaultCommand = new LoadInstallmentCommandImpl();
    LoadInstallmentCommandRequest request = new LoadInstallmentCommandRequest();

    Response<InstallmentClientResponse> response = defaultCommand.execute(request);

    assertNotNull(response);
    if (response.success()) {
      assertEquals(SUCCESS_CODE, response.code());
      assertNotNull(response.data());
      assertNull(response.error());
    } else {
      assertTrue(response.code() >= 400);
      assertNull(response.data());
      assertNotNull(response.error());
    }
  }
}
