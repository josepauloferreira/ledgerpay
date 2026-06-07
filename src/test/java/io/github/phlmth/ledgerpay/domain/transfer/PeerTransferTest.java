package io.github.phlmth.ledgerpay.domain.transfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.phlmth.ledgerpay.domain.exception.InsufficientBalanceException;
import io.github.phlmth.ledgerpay.domain.exception.InvalidMoneyMovementAmountException;
import io.github.phlmth.ledgerpay.domain.exception.SameWalletTransferException;
import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;
import io.github.phlmth.ledgerpay.domain.wallet.WalletId;
import org.junit.jupiter.api.Test;

class PeerTransferTest {

  @Test
  void shouldTransferMoneyBetweenWallets() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = new Wallet();

    PeerTransfer transfer = new PeerTransfer();

    transfer.execute(source, destination, Money.of("40.00"));

    assertThat(source.balance()).isEqualTo(Money.of("60.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("40.00"));
  }

  @Test
  void shouldRejectZeroTransfer() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = new Wallet(Money.of("20.00"));

    PeerTransfer transfer = new PeerTransfer();

    assertThatThrownBy(() -> transfer.execute(source, destination, Money.of("0.00")))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("20.00"));
  }

  @Test
  void shouldRejectNegativeTransfer() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = new Wallet(Money.of("20.00"));

    PeerTransfer transfer = new PeerTransfer();

    assertThatThrownBy(() -> transfer.execute(source, destination, Money.of("-10.00")))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("20.00"));
  }

  @Test
  void shouldRejectTransferWhenSourceHasInsufficientBalance() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = new Wallet(Money.of("20.00"));

    PeerTransfer transfer = new PeerTransfer();

    assertThatThrownBy(() -> transfer.execute(source, destination, Money.of("150.00")))
        .isInstanceOf(InsufficientBalanceException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("20.00"));
  }

  @Test
  void shouldRejectTransferWhenWalletIdsAreEqual() {
    WalletId id = WalletId.of("wallet-1");
    Wallet source = new Wallet(id, Money.of("100.00"));
    Wallet destination = new Wallet(id, Money.of("100.00"));

    PeerTransfer transfer = new PeerTransfer();

    assertThatThrownBy(() -> transfer.execute(source, destination, Money.of("40.00")))
        .isInstanceOf(SameWalletTransferException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("100.00"));
  }
}
