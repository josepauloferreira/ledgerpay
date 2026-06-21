package io.github.josepauloferreira.ledgerpay.api.wallet;

import jakarta.validation.constraints.NotBlank;

public record FundWalletRequest(@NotBlank String amount) {}
