package io.github.phlmth.ledgerpay.domain.transfer;

import static org.assertj.core.api.Assertions.assertThat;

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
}
