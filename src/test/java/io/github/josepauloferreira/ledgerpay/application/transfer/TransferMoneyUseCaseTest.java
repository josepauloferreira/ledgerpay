package io.github.josepauloferreira.ledgerpay.application.transfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.josepauloferreira.ledgerpay.domain.exception.InsufficientBalanceException;
import io.github.josepauloferreira.ledgerpay.domain.exception.SameWalletTransferException;
import io.github.josepauloferreira.ledgerpay.domain.money.Money;
import io.github.josepauloferreira.ledgerpay.domain.movement.MoneyMovement;
import io.github.josepauloferreira.ledgerpay.domain.movement.MoneyMovementHistory;
import io.github.josepauloferreira.ledgerpay.domain.movement.MoneyMovementType;
import io.github.josepauloferreira.ledgerpay.domain.wallet.Wallet;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class TransferMoneyUseCaseTest {
  @Test
  void shouldTransferMoneyAndRecordMoneyMovement() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = new Wallet();
    MoneyMovementHistory history = new MoneyMovementHistory();
    Money amount = Money.of("60.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    TransferMoneyUseCase useCase = new TransferMoneyUseCase(history);
    MoneyMovement movement = useCase.transfer(source, destination, amount, occurredAt);

    assertThat(source.balance()).isEqualTo(Money.of("40.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("60.00"));
    assertThat(movement.type()).isEqualTo(MoneyMovementType.PEER_TRANSFER);
    assertThat(history.movements()).containsExactly(movement);
    assertThat(movement.source().walletId()).isEqualTo(source.id());
    assertThat(movement.destination().walletId()).isEqualTo(destination.id());
    assertThat(movement.amount()).isEqualTo(amount);
    assertThat(movement.occurredAt()).isEqualTo(occurredAt);
  }

  @Test
  void shouldPreserveStateAndNotRecordMoneyMovementWhenSourceHasInsufficientBalance() {
    Wallet source = new Wallet(Money.of("59.99"));
    Wallet destination = new Wallet();
    MoneyMovementHistory history = new MoneyMovementHistory();
    Money amount = Money.of("60.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    TransferMoneyUseCase useCase = new TransferMoneyUseCase(history);

    assertThatThrownBy(() -> useCase.transfer(source, destination, amount, occurredAt))
        .isInstanceOf(InsufficientBalanceException.class);

    assertThat(source.balance()).isEqualTo(Money.of("59.99"));
    assertThat(destination.balance()).isEqualTo(Money.of("0.00"));
    assertThat(history.movements()).isEmpty();
  }

  @Test
  void shouldPreserveStateAndNotRecordMoneyMovementWhenTransferringToSameWallet() {
    Wallet source = new Wallet(Money.of("100.00"));
    Wallet destination = source;
    MoneyMovementHistory history = new MoneyMovementHistory();
    Money amount = Money.of("60.00");
    Instant occurredAt = Instant.parse("2026-06-10T12:00:00Z");

    TransferMoneyUseCase useCase = new TransferMoneyUseCase(history);

    assertThatThrownBy(() -> useCase.transfer(source, destination, amount, occurredAt))
        .isInstanceOf(SameWalletTransferException.class);

    assertThat(source.balance()).isEqualTo(Money.of("100.00"));
    assertThat(destination.balance()).isEqualTo(Money.of("100.00"));
    assertThat(history.movements()).isEmpty();
  }
}
