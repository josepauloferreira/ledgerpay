package io.github.phlmth.ledgerpay.domain.funding;

import io.github.phlmth.ledgerpay.domain.exception.InvalidMoneyMovementAmountException;
import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.treasury.SystemTreasury;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;

public class TreasuryFunding {
  public void execute(SystemTreasury treasury, Wallet destination, Money amount) {

    if (!amount.isPositive()) {
      throw new InvalidMoneyMovementAmountException("invalid money movement amount");
    }

    treasury.debit(amount);
    destination.credit(amount);
  }
}
