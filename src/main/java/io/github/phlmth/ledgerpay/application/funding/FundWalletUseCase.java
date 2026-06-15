package io.github.phlmth.ledgerpay.application.funding;

import io.github.phlmth.ledgerpay.domain.funding.TreasuryFunding;
import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovement;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementHistory;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementId;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementParticipant;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementType;
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

    Objects.requireNonNull(occurredAt);

    treasuryFunding.execute(treasury, wallet, amount);

    MoneyMovement movement =
        new MoneyMovement(
            MoneyMovementId.newId(),
            MoneyMovementType.TREASURY_FUNDING,
            MoneyMovementParticipant.systemTreasury(),
            MoneyMovementParticipant.wallet(wallet.id()),
            amount,
            occurredAt);

    history.record(movement);
    return movement;
  }
}
