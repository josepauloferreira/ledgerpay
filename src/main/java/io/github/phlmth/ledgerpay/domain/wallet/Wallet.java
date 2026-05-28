package io.github.phlmth.ledgerpay.domain.wallet;

import io.github.phlmth.ledgerpay.domain.money.Money;

public class Wallet {
  private Money balance;

  public Wallet(Money balance) {
    this.balance = balance;
  }

  public Money balance() {
    return this.balance;
  }

  public void credit(Money amount) {
    balance = balance.add(amount);
  }

  public void debit(Money amount) {
    balance = balance.subtract(amount);
  }
}
