package io.github.josepauloferreira.ledgerpay.domain.treasury;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.josepauloferreira.ledgerpay.domain.money.Money;
import org.junit.jupiter.api.Test;

class SystemTreasuryTest {

  @Test
  void shouldStartWithInitialBalance() {
    SystemTreasury treasury = new SystemTreasury();

    assertThat(treasury.balance()).isEqualTo(Money.of("1000000.00"));
  }
}
