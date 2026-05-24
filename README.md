# LedgerPay

> Backend Java sandbox para carteiras digitais e transferências.


## Status atual
projeto está na fase de fundação e modelagem inicial do domínio.

## Stack atual
- Java 21
- Maven
- JUnit 5
- AssertJ


## Como executar os testes
```bash
#linux
./mvnw test

#windows
.\mvnw.cmd test
```
## Roadmap imediato

- [ ] Money Value Object
- [ ] Funding via System Treasury em memória
- [ ] Peer Transfer em memória
- [ ] Persistência com Spring Boot e PostgreSQL
- [ ] API REST sandbox
- [ ] Evolução futura para ledger

## Limitações atuais

no estado atual do projeto ainda não existem API, persistência, autenticação ou ledger formal.
