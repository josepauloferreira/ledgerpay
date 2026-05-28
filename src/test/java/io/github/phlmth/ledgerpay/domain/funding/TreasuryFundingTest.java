package io.github.phlmth.ledgerpay.domain.funding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;
import org.junit.jupiter.api.Test;

class TreasuryFundingTest {

  @Test
  void shouldFundWalletFromTreasury() {
    Wallet treasury = new Wallet(Money.of("1000000.00"));
    Wallet destination = new Wallet(Money.of("0.00"));
    Money amount = Money.of("100.00");
    TreasuryFunding funding = new TreasuryFunding();

    funding.execute(treasury, destination, amount);

    assertThat(treasury.balance()).isEqualTo(Money.of("999900.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldRejectZeroFundingWithoutChangingBalances() {
    Wallet treasury = new Wallet(Money.of("1000000.00"));
    Wallet destination = new Wallet(Money.of("0.00"));
    Money amount = Money.of("0.00");
    TreasuryFunding funding = new TreasuryFunding();

    assertThatThrownBy(() -> funding.execute(treasury, destination, amount))
        .isInstanceOf(IllegalArgumentException.class);

    assertThat(treasury.balance()).isEqualTo(Money.of("1000000.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("0.00"));
  }
}
