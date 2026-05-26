# LedgerPay

> Backend Java sandbox para carteiras digitais e transferências.


## Status atual
projeto está na fase de fundação e modelagem inicial do domínio.

## Documentação
- [Definição inicial do projeto](docs/00-project-definition.md)

## Stack atual
- Java 21
- Maven
- JUnit 5
- AssertJ

## Desenvolvimento com GitHub Codespaces
O repositório possui uma configuração de dev container com Java 21 para criar um ambiente de desenvolvimento reproduzível no GitHub Codespaces.

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
