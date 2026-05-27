package io.github.phlmth.ledgerpay.domain.money;

import java.math.BigDecimal;
import java.util.Objects;

public record Money(BigDecimal amount) {

  public Money {
    Objects.requireNonNull(amount);

    try {
      amount = amount.setScale(2);
    } catch (ArithmeticException exception) {
      throw new IllegalArgumentException(exception);
    }
  }

  public static Money of(String value) {
    return new Money(new BigDecimal(value));
  }
}
