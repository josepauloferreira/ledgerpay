package io.github.phlmth.ledgerpay.domain.money;

import java.math.BigDecimal;

public record Money(BigDecimal amount) {

    public Money {
        amount = amount.setScale(2);
    }
    public static Money of(String value) {
        return new Money(new BigDecimal(value));
    }
}