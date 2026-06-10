package io.github.phlmth.ledgerpay.domain.movement;

import java.util.Objects;
import java.util.UUID;

public record MoneyMovementId(String id) {

  public MoneyMovementId {
    Objects.requireNonNull(id);

    if (id.isBlank()) {
      throw new IllegalArgumentException();
    }
  }

  public static MoneyMovementId newId() {
    return new MoneyMovementId(UUID.randomUUID().toString());
  }

  public static MoneyMovementId of(String id) {
    return new MoneyMovementId(id);
  }
}
