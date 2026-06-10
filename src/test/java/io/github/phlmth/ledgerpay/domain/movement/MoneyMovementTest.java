package io.github.phlmth.ledgerpay.domain.movement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.phlmth.ledgerpay.domain.exception.InvalidMoneyMovementAmountException;
import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.wallet.WalletId;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class MoneyMovementTest {

  @Test
  void shouldCreateMoneyMovement() {
    MoneyMovementId id = MoneyMovementId.newId();
    MoneyMovementType type = MoneyMovementType.TREASURY_FUNDING;
    MoneyMovementParticipant source = MoneyMovementParticipant.systemTreasury();
    MoneyMovementParticipant destination = MoneyMovementParticipant.wallet(WalletId.newId());
    Money amount = Money.of("100.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    MoneyMovement moneyMovement =
        new MoneyMovement(id, type, source, destination, amount, occurredAt);

    assertThat(moneyMovement.id()).isEqualTo(id);
    assertThat(moneyMovement.type()).isEqualTo(type);
    assertThat(moneyMovement.source()).isEqualTo(source);
    assertThat(moneyMovement.destination()).isEqualTo(destination);
    assertThat(moneyMovement.amount()).isEqualTo(amount);
    assertThat(moneyMovement.occurredAt()).isEqualTo(occurredAt);
  }

  @Test
  void shouldRejectNullId() {
    MoneyMovementType type = MoneyMovementType.TREASURY_FUNDING;
    MoneyMovementParticipant source = MoneyMovementParticipant.systemTreasury();
    MoneyMovementParticipant destination = MoneyMovementParticipant.wallet(WalletId.newId());
    Money amount = Money.of("100.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    assertThatThrownBy(() -> new MoneyMovement(null, type, source, destination, amount, occurredAt))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldRejectNullType() {
    MoneyMovementId id = MoneyMovementId.newId();
    MoneyMovementParticipant source = MoneyMovementParticipant.systemTreasury();
    MoneyMovementParticipant destination = MoneyMovementParticipant.wallet(WalletId.newId());
    Money amount = Money.of("100.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    assertThatThrownBy(() -> new MoneyMovement(id, null, source, destination, amount, occurredAt))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldRejectNullSource() {
    MoneyMovementId id = MoneyMovementId.newId();
    MoneyMovementType type = MoneyMovementType.TREASURY_FUNDING;
    MoneyMovementParticipant destination = MoneyMovementParticipant.wallet(WalletId.newId());
    Money amount = Money.of("100.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    assertThatThrownBy(() -> new MoneyMovement(id, type, null, destination, amount, occurredAt))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldRejectNullDestination() {
    MoneyMovementId id = MoneyMovementId.newId();
    MoneyMovementType type = MoneyMovementType.TREASURY_FUNDING;
    MoneyMovementParticipant source = MoneyMovementParticipant.systemTreasury();
    Money amount = Money.of("100.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    assertThatThrownBy(() -> new MoneyMovement(id, type, source, null, amount, occurredAt))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldRejectNullAmount() {
    MoneyMovementId id = MoneyMovementId.newId();
    MoneyMovementType type = MoneyMovementType.TREASURY_FUNDING;
    MoneyMovementParticipant source = MoneyMovementParticipant.systemTreasury();
    MoneyMovementParticipant destination = MoneyMovementParticipant.wallet(WalletId.newId());
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    assertThatThrownBy(() -> new MoneyMovement(id, type, source, destination, null, occurredAt))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldRejectNullOccurredAt() {
    MoneyMovementId id = MoneyMovementId.newId();
    MoneyMovementType type = MoneyMovementType.TREASURY_FUNDING;
    MoneyMovementParticipant source = MoneyMovementParticipant.systemTreasury();
    MoneyMovementParticipant destination = MoneyMovementParticipant.wallet(WalletId.newId());
    Money amount = Money.of("100.00");

    assertThatThrownBy(() -> new MoneyMovement(id, type, source, destination, amount, null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldRejectZeroAmount() {
    MoneyMovementId id = MoneyMovementId.newId();
    MoneyMovementType type = MoneyMovementType.TREASURY_FUNDING;
    MoneyMovementParticipant source = MoneyMovementParticipant.systemTreasury();
    MoneyMovementParticipant destination = MoneyMovementParticipant.wallet(WalletId.newId());
    Money amount = Money.of("0.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    assertThatThrownBy(() -> new MoneyMovement(id, type, source, destination, amount, occurredAt))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);
  }

  @Test
  void shouldRejectNegativeAmount() {
    MoneyMovementId id = MoneyMovementId.newId();
    MoneyMovementType type = MoneyMovementType.TREASURY_FUNDING;
    MoneyMovementParticipant source = MoneyMovementParticipant.systemTreasury();
    MoneyMovementParticipant destination = MoneyMovementParticipant.wallet(WalletId.newId());
    Money amount = Money.of("-10.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    assertThatThrownBy(() -> new MoneyMovement(id, type, source, destination, amount, occurredAt))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);
  }
}
