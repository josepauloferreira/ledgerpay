package io.github.josepauloferreira.ledgerpay.application.funding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.josepauloferreira.ledgerpay.domain.exception.InvalidMoneyMovementAmountException;
import io.github.josepauloferreira.ledgerpay.domain.money.Money;
import io.github.josepauloferreira.ledgerpay.domain.movement.MoneyMovement;
import io.github.josepauloferreira.ledgerpay.domain.movement.MoneyMovementHistory;
import io.github.josepauloferreira.ledgerpay.domain.movement.MoneyMovementType;
import io.github.josepauloferreira.ledgerpay.domain.treasury.SystemTreasury;
import io.github.josepauloferreira.ledgerpay.domain.wallet.Wallet;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class FundWalletUseCaseTest {

  @Test
  void shouldFundWalletAndRecordMoneyMovement() {
    SystemTreasury treasury = new SystemTreasury();
    Wallet wallet = new Wallet();
    MoneyMovementHistory history = new MoneyMovementHistory();
    Money amount = Money.of("100.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    FundWalletUseCase useCase = new FundWalletUseCase(history);
    MoneyMovement movement = useCase.fund(treasury, wallet, amount, occurredAt);

    assertThat(wallet.balance()).isEqualTo(Money.of("100.00"));
    assertThat(treasury.balance()).isEqualTo(Money.of("999900.00"));
    assertThat(movement.type()).isEqualTo(MoneyMovementType.TREASURY_FUNDING);
    assertThat(movement.source().isSystemTreasury()).isTrue();
    assertThat(movement.destination().walletId()).isEqualTo(wallet.id());
    assertThat(movement.amount()).isEqualTo(amount);
    assertThat(movement.occurredAt()).isEqualTo(occurredAt);

    assertThat(history.movements()).containsExactly(movement);
  }

  @Test
  void shouldNotRecordMoneyMovementWhenFundingAmountIsInvalid() {
    SystemTreasury treasury = new SystemTreasury();
    Wallet wallet = new Wallet();
    Money amount = Money.of("0.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");
    MoneyMovementHistory history = new MoneyMovementHistory();

    FundWalletUseCase useCase = new FundWalletUseCase(history);

    assertThatThrownBy(() -> useCase.fund(treasury, wallet, amount, occurredAt))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(wallet.balance()).isEqualTo(Money.of("0.00"));
    assertThat(treasury.balance()).isEqualTo(Money.of("1000000.00"));
    assertThat(history.movements()).isEmpty();
  }
}
