package io.github.josepauloferreira.ledgerpay.domain.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class WalletIdTest {

  @Test
  void shouldCreateWalletId() {
    String expectedId = "wallet-1";

    WalletId walletId = WalletId.of("wallet-1");

    assertThat(walletId.id()).isEqualTo(expectedId);
  }

  @Test
  void shouldRejectNullStringFromCanonicalConstructor() {
    assertThatThrownBy(() -> new WalletId(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldRejectEmptyAndBlankStringFromCanonicalConstructor() {
    assertThatThrownBy(() -> new WalletId(" ")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new WalletId("")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldBeEqualWhenIdsAreEqual() {
    WalletId walletId = WalletId.of("wallet-1");
    WalletId anotherWalletId = WalletId.of("wallet-1");

    assertThat(walletId).isEqualTo(anotherWalletId);
  }

  @Test
  void shouldNotBeEqualWhenIdsAreDifferent() {
    WalletId walletId = WalletId.of("wallet-1");
    WalletId anotherWalletId = WalletId.of("wallet-2");

    assertThat(walletId).isNotEqualTo(anotherWalletId);
  }

  @Test
  void shouldCreateIdWhenUsingNoArgsConstructor() {
    WalletId id = WalletId.newId();

    assertThat(id).isNotNull();
  }
}
