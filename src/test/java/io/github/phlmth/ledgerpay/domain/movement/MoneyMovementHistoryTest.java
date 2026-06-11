package io.github.phlmth.ledgerpay.domain.movement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.wallet.WalletId;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class MoneyMovementHistoryTest {
  @Test
  void shouldStartEmpty() {
    MoneyMovementHistory moneyMovementHistory = new MoneyMovementHistory();

    assertThat(moneyMovementHistory.movements()).isEmpty();
  }

  @Test
  void shouldRecordMoneyMovement() {
    MoneyMovementHistory moneyMovementHistory = new MoneyMovementHistory();

    MoneyMovementId id = MoneyMovementId.newId();
    MoneyMovementType type = MoneyMovementType.TREASURY_FUNDING;
    MoneyMovementParticipant source = MoneyMovementParticipant.systemTreasury();
    MoneyMovementParticipant destination = MoneyMovementParticipant.wallet(WalletId.newId());
    Money amount = Money.of("100.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    MoneyMovement moneyMovement =
        new MoneyMovement(id, type, source, destination, amount, occurredAt);

    moneyMovementHistory.record(moneyMovement);

    assertThat(moneyMovementHistory.movements()).containsExactly(moneyMovement);
  }

  @Test
  void shouldRejectNullMoneyMovement() {
    MoneyMovementHistory moneyMovementHistory = new MoneyMovementHistory();

    assertThatThrownBy(() -> moneyMovementHistory.record(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldPreserveRecordedMovementOrder() {
    MoneyMovementHistory moneyMovementHistory = new MoneyMovementHistory();

    MoneyMovementType type = MoneyMovementType.TREASURY_FUNDING;
    MoneyMovementParticipant source = MoneyMovementParticipant.systemTreasury();
    MoneyMovementParticipant destination = MoneyMovementParticipant.wallet(WalletId.newId());
    Money amount = Money.of("100.00");

    MoneyMovement moneyMovement =
        new MoneyMovement(
            MoneyMovementId.of("movement-01"),
            type,
            source,
            destination,
            amount,
            Instant.parse("2026-06-10T15:00:00Z"));

    MoneyMovement anotherMoneyMovement =
        new MoneyMovement(
            MoneyMovementId.of("movement-02"),
            type,
            source,
            destination,
            amount,
            Instant.parse("2026-06-10T16:00:00Z"));

    moneyMovementHistory.record(moneyMovement);
    moneyMovementHistory.record(anotherMoneyMovement);

    assertThat(moneyMovementHistory.movements())
        .containsExactly(moneyMovement, anotherMoneyMovement);
  }

  @Test
  void shouldNotAllowExternalMutationOfMovements() {
    MoneyMovementHistory moneyMovementHistory = new MoneyMovementHistory();

    MoneyMovementId id = MoneyMovementId.newId();
    MoneyMovementType type = MoneyMovementType.TREASURY_FUNDING;
    MoneyMovementParticipant source = MoneyMovementParticipant.systemTreasury();
    MoneyMovementParticipant destination = MoneyMovementParticipant.wallet(WalletId.newId());
    Money amount = Money.of("100.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    MoneyMovement moneyMovement =
        new MoneyMovement(id, type, source, destination, amount, occurredAt);

    moneyMovementHistory.record(moneyMovement);

    assertThatThrownBy(() -> moneyMovementHistory.movements().clear())
        .isInstanceOf(UnsupportedOperationException.class);

    assertThat(moneyMovementHistory.movements()).containsExactly(moneyMovement);
  }
}
