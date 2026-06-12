package io.github.phlmth.ledgerpay.domain.transfer;

import io.github.phlmth.ledgerpay.domain.exception.SameWalletTransferException;
import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovement;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementHistory;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementId;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementParticipant;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementType;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;
import java.time.Instant;
import java.util.Objects;

public class PeerTransfer {

  public void execute(
      Wallet source,
      Wallet destination,
      Money amount,
      MoneyMovementHistory movementHistory,
      Instant occurredAt) {

    Objects.requireNonNull(source);
    Objects.requireNonNull(destination);
    Objects.requireNonNull(amount);
    Objects.requireNonNull(movementHistory);
    Objects.requireNonNull(occurredAt);

    if (source.id().equals(destination.id())) {
      throw new SameWalletTransferException("same wallet transfer");
    }

    source.debit(amount);
    destination.credit(amount);

    MoneyMovement movement =
        new MoneyMovement(
            MoneyMovementId.newId(),
            MoneyMovementType.PEER_TRANSFER,
            MoneyMovementParticipant.wallet(source.id()),
            MoneyMovementParticipant.wallet(destination.id()),
            amount,
            occurredAt);

    movementHistory.record(movement);
  }
}
