package io.github.phlmth.ledgerpay.domain.funding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.phlmth.ledgerpay.domain.exception.InsufficientBalanceException;
import io.github.phlmth.ledgerpay.domain.exception.InvalidMoneyMovementAmountException;
import io.github.phlmth.ledgerpay.domain.money.Money;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovement;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementHistory;
import io.github.phlmth.ledgerpay.domain.movement.MoneyMovementType;
import io.github.phlmth.ledgerpay.domain.treasury.SystemTreasury;
import io.github.phlmth.ledgerpay.domain.wallet.Wallet;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class TreasuryFundingTest {

  @Test
  void shouldFundWalletFromTreasury() {
    SystemTreasury treasury = new SystemTreasury();
    Wallet destination = new Wallet();
    Money amount = Money.of("100.00");
    TreasuryFunding funding = new TreasuryFunding();
    MoneyMovementHistory history = new MoneyMovementHistory();
    Instant occurredAt = Instant.parse("2026-06-12T10:00:00Z");

    funding.execute(treasury, destination, amount, history, occurredAt);

    assertThat(treasury.balance()).isEqualTo(Money.of("999900.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldRejectZeroFunding() {
    SystemTreasury treasury = new SystemTreasury();
    Wallet destination = new Wallet();
    Money amount = Money.of("0.00");
    TreasuryFunding funding = new TreasuryFunding();
    MoneyMovementHistory history = new MoneyMovementHistory();
    Instant occurredAt = Instant.parse("2026-06-12T10:00:00Z");

    assertThatThrownBy(() -> funding.execute(treasury, destination, amount, history, occurredAt))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(treasury.balance()).isEqualTo(Money.of("1000000.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("0.00"));
    assertThat(history.movements()).isEmpty();
  }

  @Test
  void shouldRejectNegativeFunding() {
    SystemTreasury treasury = new SystemTreasury();
    Wallet destination = new Wallet();
    Money amount = Money.of("-10.00");
    TreasuryFunding funding = new TreasuryFunding();
    MoneyMovementHistory history = new MoneyMovementHistory();
    Instant occurredAt = Instant.parse("2026-06-12T10:00:00Z");

    assertThatThrownBy(() -> funding.execute(treasury, destination, amount, history, occurredAt))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(treasury.balance()).isEqualTo(Money.of("1000000.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("0.00"));
    assertThat(history.movements()).isEmpty();
  }

  @Test
  void shouldRejectFundingWhenTreasuryHasInsufficientBalance() {
    SystemTreasury treasury = new SystemTreasury();
    Wallet destination = new Wallet();
    Money amount = Money.of("1000000.01");
    TreasuryFunding funding = new TreasuryFunding();
    MoneyMovementHistory history = new MoneyMovementHistory();
    Instant occurredAt = Instant.parse("2026-06-12T10:00:00Z");

    assertThatThrownBy(() -> funding.execute(treasury, destination, amount, history, occurredAt))
        .isInstanceOf(InsufficientBalanceException.class);

    assertThat(treasury.balance()).isEqualTo(Money.of("1000000.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("0.00"));
    assertThat(history.movements()).isEmpty();
  }

  @Test
  void shouldRecordMoneyMovementAfterSuccessfulFunding() {
    SystemTreasury treasury = new SystemTreasury();
    Wallet wallet = new Wallet();
    Money amount = Money.of("100.00");
    MoneyMovementHistory history = new MoneyMovementHistory();
    Instant occurredAt = Instant.parse("2026-06-12T10:00:00Z");
    TreasuryFunding funding = new TreasuryFunding();

    funding.execute(treasury, wallet, amount, history, occurredAt);

    assertThat(history.movements()).hasSize(1);

    MoneyMovement registeredMovement = history.movements().getFirst();

    assertThat(registeredMovement.type()).isEqualTo(MoneyMovementType.TREASURY_FUNDING);
    assertThat(registeredMovement.source().isSystemTreasury()).isTrue();
    assertThat(registeredMovement.destination().walletId()).isEqualTo(wallet.id());
    assertThat(registeredMovement.amount()).isEqualTo(amount);
    assertThat(registeredMovement.occurredAt()).isEqualTo(occurredAt);
  }
}
