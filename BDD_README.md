# BDD (Behavior-Driven Development) - Implementação

## Visão Geral

Este projeto implementa BDD (Behavior-Driven Development) utilizando o framework **Cucumber** para Java, permitindo a escrita de testes em linguagem natural (Gherkin) que descrevem o comportamento esperado do sistema.

## Tecnologias Utilizadas

- **Cucumber 7.18.1** - Framework BDD para Java
- **Cucumber Spring** - Integração do Cucumber com Spring Boot
- **JUnit Platform Suite** - Execução dos testes BDD
- **Gherkin** - Linguagem para especificação de cenários
- **H2 Database** - Banco de dados em memória para testes

## Estrutura do Projeto BDD

```
src/test/
├── java/com/fiap/techchallenge/bdd/
│   ├── CucumberSpringConfiguration.java  # Configuração Spring para BDD
│   ├── CucumberTestRunner.java           # Runner para executar os testes
│   ├── CustomerStepDefinitions.java       # Definições dos passos (steps)
│   └── CucumberHooks.java                # Hooks para setup/teardown
└── resources/
    ├── features/
    │   └── customer.feature               # Cenários em Gherkin
    ├── application-test.yml               # Configuração de teste
    └── cucumber.properties                # Configurações do Cucumber
```

## Funcionalidades Testadas

### Cenários Implementados (8 cenários)

1. **Cadastrar um novo cliente com sucesso**
   - Valida o fluxo completo de cadastro de cliente
   - Verifica geração de ID, nome, email e CPF

2. **Não cadastrar cliente com CPF duplicado**
   - Verifica validação de CPF único no sistema
   - Testa tratamento de erro de duplicidade

3. **Buscar cliente por CPF existente**
   - Testa busca por CPF válido
   - Valida retorno correto do cliente

4. **Buscar cliente por CPF inexistente**
   - Verifica comportamento quando cliente não existe
   - Testa tratamento de exceção NotFoundException

5. **Buscar cliente por ID existente**
   - Testa busca por ID válido
   - Valida retorno correto do cliente

6. **Listar todos os clientes**
   - Testa listagem completa de clientes
   - Verifica quantidade de registros retornados

7. **Validar CPF inválido**
   - Testa validação de formato de CPF
   - Verifica checksum do CPF

8. **Validar email inválido**
   - Testa validação de formato de email
   - Verifica padrão de email válido

## Como Executar os Testes BDD

### Executar apenas testes BDD:
```bash
mvn test -Dtest=CucumberTestRunner
```

### Executar todos os testes (incluindo BDD):
```bash
mvn test
```

### Executar com verificação de cobertura:
```bash
mvn verify
```

### Gerar relatório HTML do Cucumber:
```bash
mvn test
# Relatório disponível em: target/cucumber-reports/cucumber.html
```

## Exemplo de Cenário Gherkin

```gherkin
Cenário: Cadastrar um novo cliente com sucesso
  Dado que eu tenho os dados de um novo cliente
    | nome          | email                  | cpf            |
    | João Silva    | joao.silva@email.com   | 12345678909    |
  Quando eu cadastro o cliente
  Então o cliente deve ser cadastrado com sucesso
  E o cliente deve ter um ID válido
  E o cliente deve ter o nome "João Silva"
  E o cliente deve ter o email "joao.silva@email.com"
  E o cliente deve ter o CPF "12345678909"
```

## Relatórios

### Cucumber Reports
- **HTML**: `target/cucumber-reports/cucumber.html`
- **JSON**: `target/cucumber-reports/cucumber.json`

### JaCoCo Coverage
- **HTML**: `target/site/jacoco/index.html`
- **Cobertura mínima exigida**: 85% (instruções e branches)

## Boas Práticas Implementadas

1. **Isolamento de Cenários**: Cada cenário é executado de forma isolada usando hooks
2. **CPFs Válidos**: Utilização de CPFs que passam na validação de checksum
3. **Linguagem Natural**: Cenários escritos em português para melhor compreensão
4. **Spring Integration**: Integração completa com Spring Boot para testes de integração
5. **H2 In-Memory**: Banco de dados em memória para testes rápidos e isolados
6. **Limpeza Automática**: Database é limpo antes de cada cenário

## Documentação Adicional

- [Cucumber Documentation](https://cucumber.io/docs/cucumber/)
- [Cucumber Java](https://cucumber.io/docs/installation/java/)
- [Gherkin Reference](https://cucumber.io/docs/gherkin/reference/)

