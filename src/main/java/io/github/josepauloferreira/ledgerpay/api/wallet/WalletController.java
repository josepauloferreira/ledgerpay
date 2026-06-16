package io.github.josepauloferreira.ledgerpay.api.wallet;

import io.github.josepauloferreira.ledgerpay.domain.wallet.Wallet;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallets")
public class WalletController {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public WalletResponse createWallet() {
    Wallet wallet = new Wallet();
    return new WalletResponse(wallet.id().toString(), wallet.balance().amount().toPlainString());
  }
}
