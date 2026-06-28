package io.github.josepauloferreira.ledgerpay.api.movement;

import io.github.josepauloferreira.ledgerpay.domain.movement.MoneyMovement;
import io.github.josepauloferreira.ledgerpay.domain.movement.MoneyMovementHistory;
import io.github.josepauloferreira.ledgerpay.domain.movement.MoneyMovementParticipant;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movements")
public class MoneyMovementController {

  private final MoneyMovementHistory moneyMovementHistory;

  public MoneyMovementController(MoneyMovementHistory moneyMovementHistory) {
    this.moneyMovementHistory = moneyMovementHistory;
  }

  @GetMapping
  public List<MoneyMovementResponse> listMovements() {
    var movements = moneyMovementHistory.movements();

    return movements.stream().map(this::toResponse).toList();
  }

  private MoneyMovementResponse toResponse(MoneyMovement movement) {
    return new MoneyMovementResponse(
        movement.id().id(),
        movement.type().name(),
        toParticipantResponse(movement.source()),
        toParticipantResponse(movement.destination()),
        movement.amount().amount().toPlainString(),
        movement.occurredAt().toString());
  }

  private MoneyMovementParticipantResponse toParticipantResponse(
      MoneyMovementParticipant participant) {

    return new MoneyMovementParticipantResponse(
        participant.type().name(), participant.isWallet() ? participant.walletId().id() : null);
  }
}
