package io.github.phlmth.ledgerpay.domain.movement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.phlmth.ledgerpay.domain.wallet.WalletId;
import org.junit.jupiter.api.Test;

class MoneyMovementParticipantTest {

  @Test
  void shouldCreateSystemTreasuryParticipant() {
    MoneyMovementParticipant treasuryParticipant = MoneyMovementParticipant.systemTreasury();

    assertThat(treasuryParticipant.isSystemTreasury()).isTrue();
    assertThat(treasuryParticipant.walletId()).isNull();
  }

  @Test
  void shouldCreateWalletParticipant() {
    WalletId walletId = WalletId.newId();
    MoneyMovementParticipant walletParticipant = MoneyMovementParticipant.wallet(walletId);

    assertThat(walletParticipant.isWallet()).isTrue();
    assertThat(walletParticipant.walletId()).isEqualTo(walletId);
  }

  @Test
  void shouldRejectWalletParticipantWithoutWalletId() {
    assertThatThrownBy(() -> MoneyMovementParticipant.wallet(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldConsiderWalletParticipantWithSameWalletIdEqual() {
    WalletId walletId = WalletId.newId();

    MoneyMovementParticipant walletParticipant = MoneyMovementParticipant.wallet(walletId);
    MoneyMovementParticipant anotherWalletParticipant = MoneyMovementParticipant.wallet(walletId);

    assertThat(walletParticipant).isEqualTo(anotherWalletParticipant);
  }

  @Test
  void shouldConsiderWalletParticipantWithDifferentWalletIdsDifferent() {
    WalletId walletId = WalletId.newId();
    WalletId anotherWalletId = WalletId.newId();

    MoneyMovementParticipant walletParticipant = MoneyMovementParticipant.wallet(walletId);
    MoneyMovementParticipant anotherWalletParticipant =
        MoneyMovementParticipant.wallet(anotherWalletId);

    assertThat(walletParticipant).isNotEqualTo(anotherWalletParticipant);
  }

  @Test
  void shouldConsiderSystemTreasuryDifferentFromWalletParticipant() {

    MoneyMovementParticipant walletParticipant = MoneyMovementParticipant.wallet(WalletId.newId());
    MoneyMovementParticipant treasuryParticipant = MoneyMovementParticipant.systemTreasury();

    assertThat(walletParticipant).isNotEqualTo(treasuryParticipant);
  }

  @Test
  void shouldRejectWalletParticipantWithoutWalletIdFromCanonicalConstructor() {
    assertThatThrownBy(
            () -> new MoneyMovementParticipant(MoneyMovementParticipant.Type.WALLET, null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldRejectSystemTreasuryParticipantWithWalletIdFromCanonicalConstructor() {
    WalletId walletId = WalletId.newId();

    assertThatThrownBy(
            () ->
                new MoneyMovementParticipant(
                    MoneyMovementParticipant.Type.SYSTEM_TREASURY, walletId))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
