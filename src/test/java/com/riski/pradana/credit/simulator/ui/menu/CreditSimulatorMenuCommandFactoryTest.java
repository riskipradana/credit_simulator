package com.riski.pradana.credit.simulator.ui.menu;

import com.riski.pradana.credit.simulator.model.CreditSimulatorModel;
import com.riski.pradana.credit.simulator.ui.CreditSimulatorInput;
import com.riski.pradana.credit.simulator.ui.CreditSimulatorView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class CreditSimulatorMenuCommandFactoryTest {

  @Mock
  private CreditSimulatorModel model;
  @Mock
  private CreditSimulatorView view;
  @Mock
  private CreditSimulatorInput input;

  @Test
  void unknownChoiceRunsInvalidAction() {
    CreditSimulatorMenuCommandFactory factory =
        new CreditSimulatorMenuCommandFactory(model, view, input, false);

    assertTrue(factory.getAction(99).execute());

    verify(view).showInvalidMenuOption();
    verifyNoInteractions(model);
  }
}
