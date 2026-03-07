package com.riski.pradana.credit.simulator.command;

public interface Command<RQ, RS> {
  RS execute(RQ request);
}
