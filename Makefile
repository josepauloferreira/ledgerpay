.DEFAULT_GOAL := help

MVNW := ./mvnw

.PHONY: help test verify clean

help: ## show available commands
	@grep -E '^[a-zA-Z_-]+:.*?## ' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[1;32m%-18s\033[0m %s\n", $$1, $$2}'

test: ## run the test suite
	$(MVNW) test

verify: ## run validation before review or merge
	$(MVNW) clean verify

clean: ## clean Maven build artifacts
	$(MVNW) clean
