package io.github.phlmth.ledgerpay.application.transfer;

import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovement;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementHistory;
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

  public MoneyMovement transfer(Wallet source, Wallet destination, Money amount, Instant occurredAt) {
    int nextMovementIndex = history.movements().size();

    peerTransfer.execute(source, destination, amount, history, occurredAt);

    return history.movements().get(nextMovementIndex);
  }
}
