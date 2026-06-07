package io.github.phlmth.ledgerpay.domain.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.phlmth.ledgerpay.domain.exception.InsufficientBalanceException;
import io.github.phlmth.ledgerpay.domain.exception.InvalidMoneyMovementAmountException;
import io.github.phlmth.ledgerpay.domain.exception.InvalidWalletBalanceException;
import io.github.phlmth.ledgerpay.domain.money.Money;
import org.junit.jupiter.api.Test;

class WalletTest {

  @Test
  void shouldStartWithZeroBalance() {
    Wallet wallet = new Wallet();

    assertThat(wallet.balance()).isEqualTo(Money.of("0.00"));
  }

  @Test
  void shouldRejectNullInitialBalance() {
    assertThatThrownBy(() -> new Wallet(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldRejectNegativeInitialBalance() {
    assertThatThrownBy(() -> new Wallet(Money.of("-10.00")))
        .isInstanceOf(InvalidWalletBalanceException.class);
  }

  @Test
  void shouldRejectDebitWhenBalanceIsInsufficient() {
    Wallet wallet = new Wallet(Money.of("100.00"));

    assertThatThrownBy(() -> wallet.debit(Money.of("150.00")))
        .isInstanceOf(InsufficientBalanceException.class);

    assertThat(wallet.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldRejectZeroDebit() {
    Wallet wallet = new Wallet(Money.of("100.00"));

    assertThatThrownBy(() -> wallet.debit(Money.of("0.00")))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(wallet.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldRejectNegativeDebit() {
    Wallet wallet = new Wallet(Money.of("100.00"));

    assertThatThrownBy(() -> wallet.debit(Money.of("-10.00")))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(wallet.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldRejectZeroCredit() {
    Wallet wallet = new Wallet(Money.of("100.00"));

    assertThatThrownBy(() -> wallet.credit(Money.of("0.00")))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(wallet.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldRejectNegativeCredit() {
    Wallet wallet = new Wallet(Money.of("100.00"));

    assertThatThrownBy(() -> wallet.credit(Money.of("-10.00")))
        .isInstanceOf(InvalidMoneyMovementAmountException.class);

    assertThat(wallet.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldAllowDebitEqualToBalance() {
    Wallet wallet = new Wallet(Money.of("100.00"));
    wallet.debit(Money.of("100.00"));

    assertThat(wallet.balance()).isEqualTo(Money.of("0.00"));
  }

  @Test
  void shouldGenerateWalletIdWhenUsingNoArgsConstructor() {
    Wallet wallet = new Wallet();

    assertThat(wallet.id()).isNotNull();
  }

  @Test
  void shouldGenerateWalletIdWhenUsingExplicitBalance() {
    Wallet wallet = new Wallet(Money.of("100.00"));

    assertThat(wallet.id()).isNotNull();
  }

  @Test
  void shouldPreserveExplicitWalletId() {
    WalletId walletId = WalletId.of("wallet-1");
    Money balance = Money.of("100.00");

    Wallet wallet = new Wallet(walletId, balance);

    assertThat(wallet.id()).isEqualTo(walletId);
    assertThat(wallet.balance()).isEqualTo(balance);
  }

  @Test
  void shouldRejectNullWalletIdWhenUsingExplicitConstructor() {
    WalletId walletId = null;
    Money balance = Money.of("0.00");

    assertThatThrownBy(() -> new Wallet(walletId, balance))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldRejectNullBalanceWhenUsingExplicitConstructor() {
    WalletId walletId = WalletId.newId();
    Money balance = null;

    assertThatThrownBy(() -> new Wallet(walletId, balance))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldRejectNegativeBalanceWhenUsingExplicitConstructor() {
    Money balance = Money.of("-10.00");
    assertThatThrownBy(() -> new Wallet(WalletId.newId(), balance))
        .isInstanceOf(InvalidWalletBalanceException.class);
  }
}
