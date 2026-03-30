package com.riski.pradana.credit.simulator.ui;

import com.riski.pradana.credit.simulator.client.model.InstallmentClientResponse;
import com.riski.pradana.credit.simulator.client.model.Response;
import com.riski.pradana.credit.simulator.model.CreditSimulatorModel;
import com.riski.pradana.credit.simulator.ui.menu.CreditSimulatorMenuCommandFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditSimulatorControllerTest {

  @Mock
  private CreditSimulatorModel model;
  @Mock
  private CreditSimulatorView view;
  @Mock
  private CreditSimulatorInput input;

  @Test
  void menuChoiceOneLoadsCreditAndShowsSuccess() {
    when(input.hasNextLine()).thenReturn(true, true);
    when(input.readChoice()).thenReturn(1, 3);
    when(model.loadExistingCredit()).thenReturn(Response.success(new InstallmentClientResponse("2000000", null)));

    CreditSimulatorMenuCommandFactory factory = new CreditSimulatorMenuCommandFactory(model, view, input, false);
    CreditSimulatorController controller = new CreditSimulatorController(factory, view, input, false);
    controller.run();

    verify(model).loadExistingCredit();
    verify(view).showLoadSuccess("2000000");
    verify(view, times(2)).showMenu();
  }

  @Test
  void fileModeEchoesMenuChoice() {
    when(input.hasNextLine()).thenReturn(true, true);
    when(input.readChoice()).thenReturn(1, 3);
    when(model.loadExistingCredit()).thenReturn(Response.success(new InstallmentClientResponse("x", null)));

    CreditSimulatorMenuCommandFactory factory = new CreditSimulatorMenuCommandFactory(model, view, input, true);
    CreditSimulatorController controller = new CreditSimulatorController(factory, view, input, true);
    controller.run();

    verify(view).echoMenuChoice(1);
  }
}
