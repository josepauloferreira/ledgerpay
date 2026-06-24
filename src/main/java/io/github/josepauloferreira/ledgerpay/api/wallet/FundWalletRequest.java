package io.github.josepauloferreira.ledgerpay.api.wallet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record FundWalletRequest(
    @NotBlank @Pattern(regexp = "-?\\d+(\\.\\d{1,2})?") String amount) {}
