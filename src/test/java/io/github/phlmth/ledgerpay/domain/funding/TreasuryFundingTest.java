package io.github.phlmth.ledgerpay.domain.funding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.phlmth.ledgerpay.domain.exception.InsufficientBalanceException;
import io.github.phlmth.ledgerpay.domain.exception.InvalidMoneyMovementAmountException;
import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.treasury.SystemTreasury;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;
import org.junit.jupiter.api.Test;

class TreasuryFundingTest {

  @Test
  void shouldFundWalletFromTreasury() {
    SystemTreasury treasury = new SystemTreasury();
    Wallet destination = new Wallet();
    Money amount = Money.of("100.00");
    TreasuryFunding funding = new TreasuryFunding();

    funding.execute(treasury, destination, amount);

    assertThat(treasury.balance()).isEqualTo(Money.of("999900.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldRejectZeroFunding() {
    SystemTreasury treasury = new SystemTreasury();
    Wallet destination = new Wallet();
    Money amount = Money.of("0.00");
    TreasuryFunding funding = new TreasuryFunding();

    assertThatThrownBy(() -> funding.execute(treasury, destination, amount))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(treasury.balance()).isEqualTo(Money.of("1000000.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("0.00"));
  }

  @Test
  void shouldRejectNegativeFunding() {
    SystemTreasury treasury = new SystemTreasury();
    Wallet destination = new Wallet();
    Money amount = Money.of("-10.00");
    TreasuryFunding funding = new TreasuryFunding();

    assertThatThrownBy(() -> funding.execute(treasury, destination, amount))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(treasury.balance()).isEqualTo(Money.of("1000000.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("0.00"));
  }

  @Test
  void shouldRejectFundingWhenTreasuryHasInsufficientBalance() {
    SystemTreasury treasury = new SystemTreasury();
    Wallet destination = new Wallet();
    Money amount = Money.of("1000000.01");
    TreasuryFunding funding = new TreasuryFunding();

    assertThatThrownBy(() -> funding.execute(treasury, destination, amount))
        .isInstanceOf(InsufficientBalanceException.class);

    assertThat(treasury.balance()).isEqualTo(Money.of("1000000.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("0.00"));
  }
}
