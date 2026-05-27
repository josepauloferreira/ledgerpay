# LedgerPay

> Backend Java sandbox para carteiras digitais e transferências.

## Status atual

Fundação técnica concluída. O projeto iniciou a modelagem do domínio financeiro com o Value Object `Money` já integrado à branch principal.

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
### Linux/macOS

```bash
./mvnw test
```

###  Windows

```powershell
.\mvnw.cmd test
```

## Atalhos de desenvolvimento

Os comandos abaixo são atalhos opcionais para ambientes com `make` disponível. O Maven Wrapper permanece como forma canônica de executar o build e os testes do projeto.

| Comando       | Descrição                                              |
| ------------- | ------------------------------------------------------ |
| `make help`   | Lista os atalhos disponíveis.                          |
| `make test`   | Executa a suíte de testes.                             |
| `make verify` | Executa a validação completa antes de review ou merge. |
| `make clean`  | Remove os artefatos gerados pelo build.                |
| `make format` | Aplica a formatação automática do código Java.         |
| `make format-check` | Verifica se o código Java segue o padrão de formatação.         |

## Qualidade automatizada

O projeto utiliza Spotless com `google-java-format` para manter a formatação do código Java consistente. A verificação de formatação é executada durante `make verify`.

## Roadmap imediato

- [x] Money Value Object
- [ ] Funding via System Treasury em memória
- [ ] Peer Transfer em memória
- [ ] Persistência com Spring Boot e PostgreSQL
- [ ] API REST sandbox
- [ ] Evolução futura para ledger

## Limitações atuais

No estado atual do projeto ainda não existem API, persistência, autenticação ou ledger formal.
