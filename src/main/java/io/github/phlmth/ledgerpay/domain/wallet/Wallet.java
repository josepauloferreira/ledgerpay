package io.github.phlmth.ledgerpay.domain.wallet;

import io.github.phlmth.ledgerpay.domain.exception.InsufficientBalanceException;
import io.github.phlmth.ledgerpay.domain.exception.InvalidMoneyMovementAmountException;
import io.github.phlmth.ledgerpay.domain.exception.InvalidWalletBalanceException;
import io.github.phlmth.ledgerpay.domain.money.Money;
import java.util.Objects;

public class Wallet {
  private Money balance;
  private final WalletId id;

  public Wallet() {
    this(WalletId.newId(), Money.of("0.00"));
  }

  public Wallet(Money balance) {
    this(WalletId.newId(), balance);
  }

  public Wallet(WalletId walletId, Money balance) {
    Objects.requireNonNull(walletId);
    Objects.requireNonNull(balance);

    if (balance.isLessThan(Money.of("0.00"))) {
      throw new InvalidWalletBalanceException("cannot be created with a negative balance");
    }

    this.id = walletId;
    this.balance = balance;
  }

  public Money balance() {
    return this.balance;
  }

  public WalletId id() {
    return this.id;
  }

  public void credit(Money amount) {
    if (!amount.isPositive()) {
      throw new InvalidMoneyMovementAmountException("invalid money movement amount");
    }

    balance = balance.add(amount);
  }

  public void debit(Money amount) {
    if (!amount.isPositive()) {
      throw new InvalidMoneyMovementAmountException("invalid money movement amount");
    }

    if (balance.isLessThan(amount)) {
      throw new InsufficientBalanceException("insufficient balance");
    }

    balance = balance.subtract(amount);
  }
}
