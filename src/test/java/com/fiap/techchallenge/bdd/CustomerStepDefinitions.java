package com.fiap.techchallenge.bdd;

import com.fiap.techchallenge.adapters.controllers.CustomerController;
import com.fiap.techchallenge.domain.entities.Customer;
import com.fiap.techchallenge.domain.exception.DomainException;
import com.fiap.techchallenge.domain.exception.InvalidCpfException;
import com.fiap.techchallenge.domain.exception.InvalidEmailException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerStepDefinitions {

    @Autowired
    private CustomerController customerController;

    private Customer createdCustomer;
    private Customer foundCustomer;
    private List<Customer> customerList;
    private Exception thrownException;
    private String currentName;
    private String currentEmail;
    private String currentCpf;
    private UUID currentCustomerId;

    @Dado("que eu tenho os dados de um novo cliente")
    public void queEuTenhoOsDadosDeUmNovoCliente(DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        currentName = data.get("nome");
        currentEmail = data.get("email");
        currentCpf = data.get("cpf");
        thrownException = null;
    }

    @Quando("eu cadastro o cliente")
    public void euCadastroOCliente() {
        try {
            createdCustomer = customerController.registerCustomer(currentName, currentEmail, currentCpf);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Então("o cliente deve ser cadastrado com sucesso")
    public void oClienteDeveSerCadastradoComSucesso() {
        assertNotNull(createdCustomer, "O cliente deveria ter sido criado");
        assertNull(thrownException, "Não deveria ter ocorrido exceção");
    }

    @Então("o cliente deve ter um ID válido")
    public void oClienteDeveTerUmIDValido() {
        assertNotNull(createdCustomer.getId(), "O ID do cliente não pode ser nulo");
    }

    @Então("o cliente deve ter o nome {string}")
    public void oClienteDeveTerONome(String expectedName) {
        assertEquals(expectedName, createdCustomer.getName());
    }

    @Então("o cliente deve ter o email {string}")
    public void oClienteDeveTerOEmail(String expectedEmail) {
        assertEquals(expectedEmail.toLowerCase(), createdCustomer.getEmail());
    }

    @Então("o cliente deve ter o CPF {string}")
    public void oClienteDeveTerOCPF(String expectedCpf) {
        assertEquals(expectedCpf, createdCustomer.getCpf());
    }

    @Dado("que já existe um cliente com CPF {string}")
    public void queJaExisteUmClienteComCPF(String cpf) {
        try {
            customerController.registerCustomer("Cliente Existente", "existente@email.com", cpf);
        } catch (Exception e) {
            // Cliente já pode existir de outro teste
        }
        currentCpf = cpf;
    }

    @Quando("eu tento cadastrar um novo cliente com o mesmo CPF {string}")
    public void euTentoCadastrarUmNovoClienteComOMesmoCPF(String cpf) {
        try {
            customerController.registerCustomer("Novo Cliente", "novo@email.com", cpf);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Então("deve retornar um erro de CPF duplicado")
    public void deveRetornarUmErroDeCPFDuplicado() {
        assertNotNull(thrownException, "Deveria ter lançado uma exceção");
        assertTrue(thrownException instanceof DomainException, "Deveria ser uma DomainException");
        assertTrue(thrownException.getMessage().contains("already exists"),
                "A mensagem deve indicar que o CPF já existe");
    }

    @Dado("que existe um cliente cadastrado com CPF {string}")
    public void queExisteUmClienteCadastradoComCPF(String cpf) {
        try {
            // Tenta buscar o cliente primeiro
            Optional<Customer> existing = customerController.findCustomerByCpf(cpf);
            if (existing.isPresent()) {
                createdCustomer = existing.get();
            } else {
                throw new RuntimeException("Not found");
            }
        } catch (Exception e) {
            // Se não existe, cria um novo
            createdCustomer = customerController.registerCustomer("Cliente Teste", "teste@email.com", cpf);
        }
        currentCpf = cpf;
    }

    @Quando("eu busco o cliente pelo CPF {string}")
    public void euBuscoOClientePeloCPF(String cpf) {
        try {
            Optional<Customer> result = customerController.findCustomerByCpf(cpf);
            foundCustomer = result.orElse(null);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Então("o cliente deve ser encontrado")
    public void oClienteDeveSerEncontrado() {
        assertNotNull(foundCustomer, "O cliente deveria ter sido encontrado");
    }

    @Então("o CPF do cliente deve ser {string}")
    public void oCPFDoClienteDeveSer(String expectedCpf) {
        assertEquals(expectedCpf, foundCustomer.getCpf());
    }

    @Quando("eu busco um cliente pelo CPF {string} que não existe")
    public void euBuscoUmClientePeloCPFQueNaoExiste(String cpf) {
        try {
            customerController.findCustomerByCpf(cpf);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Então("nenhum cliente deve ser encontrado")
    public void nenhumClienteDeveSerEncontrado() {
        assertNotNull(thrownException, "Deveria ter lançado uma exceção de não encontrado");
    }

    @Dado("que existe um cliente cadastrado")
    public void queExisteUmClienteCadastrado() {
        createdCustomer = customerController.registerCustomer(
                "Cliente Para Busca",
                "busca@email.com",
                "12345678909"
        );
        currentCustomerId = createdCustomer.getId();
    }

    @Quando("eu busco o cliente pelo seu ID")
    public void euBuscoOClientePeloSeuID() {
        Optional<Customer> result = customerController.findCustomerById(currentCustomerId);
        foundCustomer = result.orElse(null);
    }

    @Então("o cliente deve ser encontrado por ID")
    public void oClienteDeveSerEncontradoPorID() {
        assertNotNull(foundCustomer, "O cliente deveria ter sido encontrado por ID");
        assertEquals(currentCustomerId, foundCustomer.getId());
    }

    @Dado("que existem {int} clientes cadastrados")
    public void queExistemClientesCadastrados(int quantidade) {
        // Lista de CPFs válidos para testes
        String[] validCpfs = {
            "12345678909", "98765432100", "11144477735",
            "52998224725", "84394789508", "71506168051"
        };

        // Limpa antes de cadastrar
        List<Customer> existing = customerController.findAllCustomers();

        // Cadastrar clientes necessários para garantir a quantidade mínima
        int needed = quantidade - existing.size();
        for (int i = 0; i < needed && i < validCpfs.length; i++) {
            try {
                customerController.registerCustomer(
                        "Cliente Teste " + i,
                        "clienteteste" + i + "@email.com",
                        validCpfs[i]
                );
            } catch (Exception e) {
                // Cliente pode já existir, continua
            }
        }
    }

    @Quando("eu listo todos os clientes")
    public void euListoTodosOsClientes() {
        customerList = customerController.findAllCustomers();
    }

    @Então("devem ser retornados {int} clientes")
    public void devemSerRetornadosClientes(int quantidade) {
        assertNotNull(customerList, "A lista de clientes não pode ser nula");
        assertTrue(customerList.size() >= quantidade,
                "Deveria retornar pelo menos " + quantidade + " clientes");
    }

    @Dado("que eu tenho os dados de um cliente com CPF inválido")
    public void queEuTenhoOsDadosDeUmClienteComCPFInvalido(DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        currentName = data.get("nome");
        currentEmail = data.get("email");
        currentCpf = data.get("cpf");
        thrownException = null;
    }

    @Quando("eu tento cadastrar o cliente")
    public void euTentoCadastrarOCliente() {
        try {
            customerController.registerCustomer(currentName, currentEmail, currentCpf);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Então("deve retornar um erro de CPF inválido")
    public void deveRetornarUmErroDeCPFInvalido() {
        assertNotNull(thrownException, "Deveria ter lançado uma exceção");
        assertTrue(thrownException instanceof InvalidCpfException,
                "Deveria ser uma InvalidCpfException");
    }

    @Dado("que eu tenho os dados de um cliente com email inválido")
    public void queEuTenhoOsDadosDeUmClienteComEmailInvalido(DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        currentName = data.get("nome");
        currentEmail = data.get("email");
        currentCpf = data.get("cpf");
        thrownException = null;
    }

    @Então("deve retornar um erro de email inválido")
    public void deveRetornarUmErroDeEmailInvalido() {
        assertNotNull(thrownException, "Deveria ter lançado uma exceção");
        assertTrue(thrownException instanceof InvalidEmailException,
                "Deveria ser uma InvalidEmailException");
    }
}

