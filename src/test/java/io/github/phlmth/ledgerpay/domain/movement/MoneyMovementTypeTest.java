package io.github.phlmth.ledgerpay.domain.movement;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MoneyMovementTypeTest {

  @Test
  void shouldContainAllExpectedMovementTypes() {
    assertThat(MoneyMovementType.values())
        .hasSize(2)
        .containsExactlyInAnyOrder(
            MoneyMovementType.TREASURY_FUNDING, MoneyMovementType.PEER_TRANSFER);
  }
}
