package io.github.josepauloferreira.ledgerpay.infra.memory;

import io.github.josepauloferreira.ledgerpay.domain.wallet.Wallet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class WalletStore {
  private final Map<String, Wallet> store = new ConcurrentHashMap<>();

  public Wallet create() {
    Wallet wallet = new Wallet();
    store.put(wallet.id().toString(), wallet);
    return wallet;
  }

  public Optional<Wallet> findById(String id) {
    return Optional.ofNullable(store.get(id));
  }
}
