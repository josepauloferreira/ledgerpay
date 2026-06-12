package io.github.phlmth.ledgerpay.domain.transfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.phlmth.ledgerpay.domain.exception.InsufficientBalanceException;
import io.github.phlmth.ledgerpay.domain.exception.InvalidMoneyMovementAmountException;
import io.github.phlmth.ledgerpay.domain.exception.SameWalletTransferException;
import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovement;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementHistory;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementType;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;
import io.github.phlmth.ledgerpay.domain.wallet.WalletId;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class PeerTransferTest {

  @Test
  void shouldTransferMoneyBetweenWallets() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = new Wallet();
    MoneyMovementHistory history = new MoneyMovementHistory();
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    PeerTransfer transfer = new PeerTransfer();

    transfer.execute(source, destination, Money.of("40.00"), history, occurredAt);

    assertThat(source.balance()).isEqualTo(Money.of("60.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("40.00"));
  }

  @Test
  void shouldRejectZeroTransfer() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = new Wallet(Money.of("20.00"));
    MoneyMovementHistory history = new MoneyMovementHistory();
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    PeerTransfer transfer = new PeerTransfer();

    assertThatThrownBy(
            () -> transfer.execute(source, destination, Money.of("0.00"), history, occurredAt))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("20.00"));
    assertThat(history.movements()).isEmpty();
  }

  @Test
  void shouldRejectNegativeTransfer() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = new Wallet(Money.of("20.00"));
    MoneyMovementHistory history = new MoneyMovementHistory();
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    PeerTransfer transfer = new PeerTransfer();

    assertThatThrownBy(
            () -> transfer.execute(source, destination, Money.of("-10.00"), history, occurredAt))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("20.00"));
    assertThat(history.movements()).isEmpty();
  }

  @Test
  void shouldRejectTransferWhenSourceHasInsufficientBalance() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = new Wallet(Money.of("20.00"));
    MoneyMovementHistory history = new MoneyMovementHistory();
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    PeerTransfer transfer = new PeerTransfer();

    assertThatThrownBy(
            () -> transfer.execute(source, destination, Money.of("150.00"), history, occurredAt))
        .isInstanceOf(InsufficientBalanceException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("20.00"));
    assertThat(history.movements()).isEmpty();
  }

  @Test
  void shouldRejectTransferWhenWalletIdsAreEqual() {
    WalletId id = WalletId.of("wallet-1");
    Wallet source = new Wallet(id, Money.of("100.00"));
    Wallet destination = new Wallet(id, Money.of("100.00"));
    MoneyMovementHistory history = new MoneyMovementHistory();
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    PeerTransfer transfer = new PeerTransfer();

    assertThatThrownBy(
            () -> transfer.execute(source, destination, Money.of("40.00"), history, occurredAt))
        .isInstanceOf(SameWalletTransferException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("100.00"));
    assertThat(history.movements()).isEmpty();
  }

  @Test
  void shouldRecordMoneyMovementAfterSuccessfulPeerTransfer() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = new Wallet(Money.of("100.00"));
    MoneyMovementHistory history = new MoneyMovementHistory();
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");
    Money amount = Money.of("40.00");
    PeerTransfer transfer = new PeerTransfer();

    transfer.execute(source, destination, amount, history, occurredAt);

    assertThat(history.movements()).hasSize(1);

    MoneyMovement registeredMovement = history.movements().getFirst();

    assertThat(registeredMovement.type()).isEqualTo(MoneyMovementType.PEER_TRANSFER);
    assertThat(registeredMovement.source().walletId()).isEqualTo(source.id());
    assertThat(registeredMovement.destination().walletId()).isEqualTo(destination.id());
    assertThat(registeredMovement.amount()).isEqualTo(amount);
    assertThat(registeredMovement.occurredAt()).isEqualTo(occurredAt);
  }
}
