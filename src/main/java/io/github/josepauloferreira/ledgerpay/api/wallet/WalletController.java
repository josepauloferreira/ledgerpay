package io.github.josepauloferreira.ledgerpay.api.wallet;

import io.github.josepauloferreira.ledgerpay.application.funding.FundWalletUseCase;
import io.github.josepauloferreira.ledgerpay.application.transfer.TransferMoneyUseCase;
import io.github.josepauloferreira.ledgerpay.domain.money.Money;
import io.github.josepauloferreira.ledgerpay.domain.treasury.SystemTreasury;
import io.github.josepauloferreira.ledgerpay.domain.wallet.Wallet;
import io.github.josepauloferreira.ledgerpay.infra.memory.WalletStore;
import jakarta.validation.Valid;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/wallets")
public class WalletController {

  private final WalletStore store;
  private final FundWalletUseCase fundWalletUseCase;
  private final SystemTreasury treasury;
  private final TransferMoneyUseCase transferMoneyUseCase;

  private WalletResponse toResponse(Wallet wallet) {
    return new WalletResponse(wallet.id().id(), wallet.balance().amount().toPlainString());
  }

  private Wallet findWalletOrThrow(String id) {
    return store.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  public WalletController(
      WalletStore store,
      FundWalletUseCase fundWalletUseCase,
      SystemTreasury treasury,
      TransferMoneyUseCase transferMoneyUseCase) {
    this.store = store;
    this.fundWalletUseCase = fundWalletUseCase;
    this.treasury = treasury;
    this.transferMoneyUseCase = transferMoneyUseCase;
  }

  @PostMapping
  public ResponseEntity<WalletResponse> createWallet(UriComponentsBuilder uriBuilder) {
    var wallet = store.create();

    var response = toResponse(wallet);

    var location = uriBuilder.path("/wallets/{id}").buildAndExpand(response.id()).toUri();

    return ResponseEntity.created(location).body(response);
  }

  @GetMapping("/{id}")
  public WalletResponse getWallet(@PathVariable String id) {
    var wallet = findWalletOrThrow(id);

    return toResponse(wallet);
  }

  @PostMapping("/{id}/funding")
  public WalletResponse fundWallet(
      @PathVariable String id, @RequestBody @Valid FundWalletRequest request) {
    var wallet = findWalletOrThrow(id);

    var amount = Money.of(request.amount());
    fundWalletUseCase.fund(treasury, wallet, amount, Instant.now());

    return toResponse(wallet);
  }

  @PostMapping("/{id}/transfers")
  public TransferMoneyResponse transferMoney(
      @PathVariable String id, @RequestBody @Valid TransferMoneyRequest request) {
    var source = findWalletOrThrow(id);
    var target = findWalletOrThrow(request.targetWalletId());

    var amount = Money.of(request.amount());

    transferMoneyUseCase.transfer(source, target, amount, Instant.now());

    return new TransferMoneyResponse(toResponse(source), toResponse(target));
  }
}
