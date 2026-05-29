package io.github.phlmth.ledgerpay.domain.money;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyTest {
  @Test
  void shouldCreateMoneyWithTwoDecimalPlaces() {
    var expectedAmount = new BigDecimal("10.50");

    Money money = Money.of("10.50");

    assertThat(money.amount()).isEqualTo(expectedAmount);
  }

  @Test
  void shouldNormalizeValueWithoutDecimalPlaces() {
    var expectedAmount = new BigDecimal("10.00");

    Money money = Money.of("10");

    assertThat(money.amount()).isEqualTo(expectedAmount);
  }

  @Test
  void shouldNormalizeAmountCreatedThroughCanonicalConstructor() {
    var expectedAmount = new BigDecimal("10.00");

    Money money = new Money(new BigDecimal("10"));

    assertThat(money.amount()).isEqualTo(expectedAmount);
  }

  @Test
  void shouldNormalizeValueWithOneDecimalPlace() {
    var expectedAmount = new BigDecimal("10.50");

    Money money = Money.of("10.5");

    assertThat(money.amount()).isEqualTo(expectedAmount);
  }

  @Test
  void shouldNormalizeAmountWithTrailingFractionalZeros() {
    var expectedAmount = new BigDecimal("10.00");

    Money money = Money.of("10.000");

    assertThat(money.amount()).isEqualTo(expectedAmount);
  }

  @Test
  void shouldRejectFractionalCentAmountFromTextualFactory() {
    assertThatThrownBy(() -> Money.of("10.001")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldRejectFractionalCentAmountFromCanonicalConstructor() {
    assertThatThrownBy(() -> new Money(new BigDecimal("10.001")))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void shouldAllowZeroAmount() {
    var expectedAmount = new BigDecimal("0.00");

    Money money = Money.of("0.00");

    assertThat(money.amount()).isEqualTo(expectedAmount);
  }

  @Test
  void shouldAllowNegativeAmount() {
    var expectedAmount = new BigDecimal("-10.00");

    Money money = Money.of("-10.00");

    assertThat(money.amount()).isEqualTo(expectedAmount);
  }

  @Test
  void shouldRejectNullAmountFromCanonicalConstructor() {
    assertThatThrownBy(() -> new Money(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void shouldAddAmounts() {
    Money money = Money.of("0.00");

    Money result = money.add(Money.of("100.00"));

    assertThat(result).isEqualTo(Money.of("100.00"));
  }

  @Test
  void shouldSubtractAmounts() {
    Money money = Money.of("1000000.00");

    Money result = money.subtract(Money.of("100.00"));

    assertThat(result).isEqualTo(Money.of("999900.00"));
  }

  @Test
  void shouldIdentifyPositiveAmount() {
    Money money = Money.of("100.00");

    boolean result = money.isPositive();

    assertThat(result).isTrue();
  }

  @Test
  void shouldIdentifyZeroAmountAsNotPositive() {
    Money money = Money.of("0.00");

    boolean result = money.isPositive();

    assertThat(result).isFalse();
  }

  @Test
  void shouldIdentifyNegativeAmountAsNotPositive() {
    Money money = Money.of("-10.00");

    boolean result = money.isPositive();

    assertThat(result).isFalse();
  }

  @Test
  void shouldIdentifySmallerAmount() {
    Money money = Money.of("100.00");

    boolean result = money.isLessThan(Money.of("150.00"));

    assertThat(result).isTrue();
  }

  @Test
  void shouldIdentifyEqualAmountAsNotSmaller() {
    Money money = Money.of("100.00");

    boolean result = money.isLessThan(Money.of("100.00"));

    assertThat(result).isFalse();
  }

  @Test
  void shouldIdentifyGreaterAmountAsNotSmaller() {
    Money money = Money.of("150.00");

    boolean result = money.isLessThan(Money.of("100.00"));

    assertThat(result).isFalse();
  }
}
