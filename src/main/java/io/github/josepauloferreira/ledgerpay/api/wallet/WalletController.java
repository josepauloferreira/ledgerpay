package io.github.josepauloferreira.ledgerpay.api.wallet;

import io.github.josepauloferreira.ledgerpay.domain.wallet.Wallet;
import io.github.josepauloferreira.ledgerpay.infra.memory.WalletStore;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/wallets")
public class WalletController {

  private final WalletStore store;

  public WalletController(WalletStore store) {
    this.store = store;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public WalletResponse createWallet() {
    Wallet wallet = store.create();
    return toResponse(wallet);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public WalletResponse getWallet(@PathVariable String id) {
    Optional<Wallet> wallet = store.findById(id);
    if (wallet.isPresent()) {
      return toResponse(wallet.get());
    }

    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
  }

  private WalletResponse toResponse(Wallet wallet) {
    return new WalletResponse(wallet.id().toString(), wallet.balance().amount().toPlainString());
  }
}
