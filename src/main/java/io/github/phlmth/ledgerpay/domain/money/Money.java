package io.github.phlmth.ledgerpay.domain.money;

import java.math.BigDecimal;

public record Money(BigDecimal amount) {

    public Money {
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
