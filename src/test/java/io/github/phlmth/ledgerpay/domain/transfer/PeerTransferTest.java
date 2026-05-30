package io.github.phlmth.ledgerpay.domain.transfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;
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
        .isInstanceOf(IllegalArgumentException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("20.00"));
  }

  @Test
  void shouldRejectNegativeTransfer() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = new Wallet(Money.of("20.00"));

    PeerTransfer transfer = new PeerTransfer();

    assertThatThrownBy(() -> transfer.execute(source, destination, Money.of("-10.00")))
        .isInstanceOf(IllegalArgumentException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("20.00"));
  }

  @Test
  void shouldRejectTransferWhenSourceHasInsufficientBalance() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = new Wallet(Money.of("20.00"));

    PeerTransfer transfer = new PeerTransfer();

    assertThatThrownBy(() -> transfer.execute(source, destination, Money.of("150.00")))
        .isInstanceOf(IllegalStateException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("20.00"));
  }

  @Test
  void shouldRejectTransferFromSameWallet() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = source;

    PeerTransfer transfer = new PeerTransfer();

    assertThatThrownBy(() -> transfer.execute(source, destination, Money.of("40.00")))
        .isInstanceOf(IllegalArgumentException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("100.00"));
  }
}
