package io.github.phlmth.ledgerpay.domain.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.phlmth.ledgerpay.domain.money.Money;
import org.junit.jupiter.api.Test;

class WalletTest {

  @Test
  void shouldStartWithZeroBalance() {
    Wallet wallet = new Wallet();

    assertThat(wallet.balance()).isEqualTo(Money.of("0.00"));
  }

  @Test
  void shouldRejectDebitWhenBalanceIsInsufficient() {
    Wallet wallet = new Wallet(Money.of("100.00"));

    assertThatThrownBy(() -> wallet.debit(Money.of("150.00")))
        .isInstanceOf(IllegalStateException.class);

    assertThat(wallet.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldRejectZeroDebit() {
    Wallet wallet = new Wallet(Money.of("100.00"));

    assertThatThrownBy(() -> wallet.debit(Money.of("0.00")))
        .isInstanceOf(IllegalArgumentException.class);

    assertThat(wallet.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldRejectNegativeDebit() {
    Wallet wallet = new Wallet(Money.of("100.00"));

    assertThatThrownBy(() -> wallet.debit(Money.of("-10.00")))
        .isInstanceOf(IllegalArgumentException.class);

    assertThat(wallet.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldRejectZeroCredit() {
    Wallet wallet = new Wallet(Money.of("100.00"));

    assertThatThrownBy(() -> wallet.credit(Money.of("0.00")))
        .isInstanceOf(IllegalArgumentException.class);

    assertThat(wallet.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldRejectNegativeCredit() {
    Wallet wallet = new Wallet(Money.of("100.00"));

    assertThatThrownBy(() -> wallet.credit(Money.of("-10.00")))
        .isInstanceOf(IllegalArgumentException.class);

    assertThat(wallet.balance()).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldAllowDebitEqualToBalance() {
    Wallet wallet = new Wallet(Money.of("100.00"));
    wallet.debit(Money.of("100.00"));

    assertThat(wallet.balance()).isEqualTo(Money.of("0.00"));
  }
}
