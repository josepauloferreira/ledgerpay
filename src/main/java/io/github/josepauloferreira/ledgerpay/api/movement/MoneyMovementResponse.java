package io.github.josepauloferreira.ledgerpay.api.movement;

public record MoneyMovementResponse(
    String id,
    String type,
    MoneyMovementParticipantResponse source,
    MoneyMovementParticipantResponse destination,
    String amount,
    String occurredAt) {}
