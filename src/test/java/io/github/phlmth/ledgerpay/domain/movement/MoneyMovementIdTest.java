package io.github.phlmth.ledgerpay.domain.movement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MoneyMovementIdTest {
  @Test
  void shouldCreateMoneyMovementId() {
    String expectedId = "movement-01";

    MoneyMovementId moneyMovementId = MoneyMovementId.of(expectedId);

    assertThat(moneyMovementId.id()).isEqualTo(expectedId);
  }

  @Test
  void shouldRejectNullStringFromCanonicalConstructor() {
    assertThatThrownBy(() -> new MoneyMovementId(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldRejectEmptyAndBlankStringFromCanonicalConstructor() {
    assertThatThrownBy(() -> new MoneyMovementId(" ")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new MoneyMovementId("")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldBeEqualWhenIdsAreEqual() {
    MoneyMovementId movementId = MoneyMovementId.of("movement-1");
    MoneyMovementId anotherMovementId = MoneyMovementId.of("movement-1");

    assertThat(movementId).isEqualTo(anotherMovementId);
  }

  @Test
  void shouldNotBeEqualWhenIdsAreDifferent() {
    MoneyMovementId movementId = MoneyMovementId.of("movement-1");
    MoneyMovementId anotherMovementId = MoneyMovementId.of("movement-2");

    assertThat(movementId).isNotEqualTo(anotherMovementId);
  }

  @Test
  void shouldCreateIdWhenUsingNewId() {
    MoneyMovementId movementId = MoneyMovementId.newId();

    assertThat(movementId).isNotNull();
  }
}
