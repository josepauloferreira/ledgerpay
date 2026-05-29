package io.github.phlmth.ledgerpay.domain.treasury;

import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;

public class SystemTreasury {
  private final Wallet funds;

  public SystemTreasury() {
    this.funds = new Wallet(Money.of("1000000.00"));
  }

  public Money balance() {
    return funds.balance();
  }

  public void debit(Money amount) {
    funds.debit(amount);
  }
}
