package com.riski.pradana.credit.simulator.service;

public interface Command<RQ, RS> {
  RS execute(RQ request);
}
