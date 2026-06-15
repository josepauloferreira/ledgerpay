package io.github.phlmth.ledgerpay.application.funding;

import io.github.phlmth.ledgerpay.domain.funding.TreasuryFunding;
import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovement;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementHistory;
import io.github.phlmth.ledgerpay.domain.treasury.SystemTreasury;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;
import java.time.Instant;
import java.util.Objects;

public class FundWalletUseCase {
  private final MoneyMovementHistory history;
  private final TreasuryFunding treasuryFunding;

  public FundWalletUseCase(MoneyMovementHistory history) {
    this.history = Objects.requireNonNull(history);
    this.treasuryFunding = new TreasuryFunding();
  }

  public MoneyMovement fund(
      SystemTreasury treasury, Wallet wallet, Money amount, Instant occurredAt) {
    int nextMovementIndex = history.movements().size();

    treasuryFunding.execute(treasury, wallet, amount, history, occurredAt);

    return history.movements().get(nextMovementIndex);
  }
}
