package io.github.phlmth.ledgerpay.domain.funding;

import io.github.phlmth.ledgerpay.domain.exception.InvalidMoneyMovementAmountException;
import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovement;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementHistory;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementId;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementParticipant;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementType;
import io.github.phlmth.ledgerpay.domain.treasury.SystemTreasury;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;
import java.time.Instant;
import java.util.Objects;

public class TreasuryFunding {
  public void execute(
      SystemTreasury treasury,
      Wallet destination,
      Money amount,
      MoneyMovementHistory movementHistory,
      Instant occurredAt) {

    Objects.requireNonNull(treasury);
    Objects.requireNonNull(destination);
    Objects.requireNonNull(amount);
    Objects.requireNonNull(movementHistory);
    Objects.requireNonNull(occurredAt);

    if (!amount.isPositive()) {
      throw new InvalidMoneyMovementAmountException("invalid money movement amount");
    }

    treasury.debit(amount);
    destination.credit(amount);

    MoneyMovement movement =
        new MoneyMovement(
            MoneyMovementId.newId(),
            MoneyMovementType.TREASURY_FUNDING,
            MoneyMovementParticipant.systemTreasury(),
            MoneyMovementParticipant.wallet(destination.id()),
            amount,
            occurredAt);

    movementHistory.record(movement);
  }
}
