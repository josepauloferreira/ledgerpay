package io.github.phlmth.ledgerpay.application.transfer;

import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovement;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementHistory;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementId;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementParticipant;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementType;
import io.github.phlmth.ledgerpay.domain.transfer.PeerTransfer;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;
import java.time.Instant;
import java.util.Objects;

public class TransferMoneyUseCase {
  private final MoneyMovementHistory history;
  private final PeerTransfer peerTransfer;

  public TransferMoneyUseCase(MoneyMovementHistory history) {
    this.history = Objects.requireNonNull(history);
    this.peerTransfer = new PeerTransfer();
  }

  public MoneyMovement transfer(
      Wallet source, Wallet destination, Money amount, Instant occurredAt) {
    Objects.requireNonNull(occurredAt);

    peerTransfer.execute(source, destination, amount);

    MoneyMovement movement =
        new MoneyMovement(
            MoneyMovementId.newId(),
            MoneyMovementType.PEER_TRANSFER,
            MoneyMovementParticipant.wallet(source.id()),
            MoneyMovementParticipant.wallet(destination.id()),
            amount,
            occurredAt);

    history.record(movement);

    return movement;
  }
}
