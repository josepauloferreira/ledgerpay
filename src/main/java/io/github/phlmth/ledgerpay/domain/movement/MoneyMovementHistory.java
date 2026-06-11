package io.github.phlmth.ledgerpay.domain.movement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MoneyMovementHistory {
  private final List<MoneyMovement> movements;

  public MoneyMovementHistory() {
    movements = new ArrayList<>();
  }

  public List<MoneyMovement> movements() {
    return List.copyOf(movements);
  }

  public void record(MoneyMovement movement) {
    Objects.requireNonNull(movement);
    this.movements.add(movement);
  }
}
