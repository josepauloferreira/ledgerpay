package io.github.phlmth.ledgerpay.domain.movement;

import io.github.phlmth.ledgerpay.domain.wallet.WalletId;
import java.util.Objects;

public record MoneyMovementParticipant(Type type, WalletId walletId) {
  public enum Type {
    SYSTEM_TREASURY,
    WALLET
  }

  public MoneyMovementParticipant {
    Objects.requireNonNull(type);

    if (type == Type.SYSTEM_TREASURY && walletId != null) {
      throw new IllegalArgumentException();
    }

    if (type == Type.WALLET && walletId == null) {
      throw new NullPointerException();
    }
  }

  public static MoneyMovementParticipant systemTreasury() {
    return new MoneyMovementParticipant(Type.SYSTEM_TREASURY, null);
  }

  public static MoneyMovementParticipant wallet(WalletId walletId) {
    return new MoneyMovementParticipant(Type.WALLET, walletId);
  }

  public boolean isSystemTreasury() {
    return this.type == Type.SYSTEM_TREASURY;
  }

  public boolean isWallet() {
    return this.type == Type.WALLET;
  }
}
