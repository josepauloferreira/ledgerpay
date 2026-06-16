package io.github.josepauloferreira.ledgerpay.domain.wallet;

import java.util.Objects;
import java.util.UUID;

public record WalletId(String id) {

  public WalletId {
    Objects.requireNonNull(id);

    if (id.isBlank()) {
      throw new IllegalArgumentException();
    }
  }

  public static WalletId of(String id) {
    return new WalletId(id);
  }

  public static WalletId newId() {
    return new WalletId(UUID.randomUUID().toString());
  }
}
