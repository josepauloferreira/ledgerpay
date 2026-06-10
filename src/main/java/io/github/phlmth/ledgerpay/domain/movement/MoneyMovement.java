package io.github.phlmth.ledgerpay.domain.movement;

import io.github.phlmth.ledgerpay.domain.exception.InvalidMoneyMovementAmountException;
import io.github.phlmth.ledgerpay.domain.money.Money;
import java.time.Instant;
import java.util.Objects;

public record MoneyMovement(
    MoneyMovementId id,
    MoneyMovementType type,
    MoneyMovementParticipant source,
    MoneyMovementParticipant destination,
    Money amount,
    Instant occurredAt) {
  public MoneyMovement {
    Objects.requireNonNull(id);
    Objects.requireNonNull(type);
    Objects.requireNonNull(source);
    Objects.requireNonNull(destination);
    Objects.requireNonNull(amount);
    Objects.requireNonNull(occurredAt);

    if (!amount.isPositive()) {
      throw new InvalidMoneyMovementAmountException("invalid money movement amount");
    }
  }
}
