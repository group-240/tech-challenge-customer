# language: pt
Funcionalidade: Gerenciamento de Clientes
  Como um usuário do sistema
  Eu quero gerenciar clientes
  Para que eu possa cadastrar e consultar informações de clientes

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

  Cenário: Não cadastrar cliente com CPF duplicado
    Dado que já existe um cliente com CPF "98765432100"
    Quando eu tento cadastrar um novo cliente com o mesmo CPF "98765432100"
    Então deve retornar um erro de CPF duplicado

  Cenário: Buscar cliente por CPF existente
    Dado que existe um cliente cadastrado com CPF "52998224725"
    Quando eu busco o cliente pelo CPF "52998224725"
    Então o cliente deve ser encontrado
    E o CPF do cliente deve ser "52998224725"

  Cenário: Buscar cliente por CPF inexistente
    Quando eu busco um cliente pelo CPF "99999999999" que não existe
    Então nenhum cliente deve ser encontrado

  Cenário: Buscar cliente por ID existente
    Dado que existe um cliente cadastrado
    Quando eu busco o cliente pelo seu ID
    Então o cliente deve ser encontrado por ID

  Cenário: Listar todos os clientes
    Dado que existem 3 clientes cadastrados
    Quando eu listo todos os clientes
    Então devem ser retornados 3 clientes

  Cenário: Validar CPF inválido
    Dado que eu tenho os dados de um cliente com CPF inválido
      | nome          | email                  | cpf            |
      | Maria Santos  | maria@email.com        | 123            |
    Quando eu tento cadastrar o cliente
    Então deve retornar um erro de CPF inválido

  Cenário: Validar email inválido
    Dado que eu tenho os dados de um cliente com email inválido
      | nome          | email          | cpf            |
      | Pedro Costa   | emailinvalido  | 12345678909    |
    Quando eu tento cadastrar o cliente
    Então deve retornar um erro de email inválido

