package io.github.josepauloferreira.ledgerpay.infra.config;

import io.github.josepauloferreira.ledgerpay.application.funding.FundWalletUseCase;
import io.github.josepauloferreira.ledgerpay.application.transfer.TransferMoneyUseCase;
import io.github.josepauloferreira.ledgerpay.domain.movement.MoneyMovementHistory;
import io.github.josepauloferreira.ledgerpay.domain.treasury.SystemTreasury;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LedgerPayConfig {

  @Bean
  MoneyMovementHistory moneyMovementHistory() {
    return new MoneyMovementHistory();
  }

  @Bean
  FundWalletUseCase fundWalletUseCase(MoneyMovementHistory moneyMovementHistory) {
    return new FundWalletUseCase(moneyMovementHistory);
  }

  @Bean
  SystemTreasury systemTreasury() {
    return new SystemTreasury();
  }

  @Bean
  TransferMoneyUseCase transferMoneyUseCase(MoneyMovementHistory moneyMovementHistory) {
    return new TransferMoneyUseCase(moneyMovementHistory);
  }
}
