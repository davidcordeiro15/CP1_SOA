package org.example.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import org.example.Dao.ClienteDao;
import org.example.model.Cliente;

@WebService
public class ClienteService {

    private ClienteDao clienteDao = new ClienteDao();
    // Cria um cliente recebendo esses parâmetros e retorna mensagem de sucesso/erro
    @WebMethod
    public String criarCliente(String nome, String email, String cpf) {
        try {
            if (nome.isBlank() || email.isBlank() || cpf.isBlank()) {
                return "Coloque todos os valores para criar um cliente!";
            }
            clienteDao.adicionaCliente(nome, email, cpf);

            // Validamos se o cliente foi criado com sucesso
            Cliente client = clienteDao.consultaCliente(email);

            if (client != null && email.equals(client.getEmail())) {
                return "Cliente criado com sucesso";

            }
            return "Algum erro aconteceu, o cliente não foi criado, tente novamente!";

        } catch (RuntimeException e) {
            return "Erro ao criar cliente: " + e.getMessage();
        }

    }

    // Recebe email do cliente (uma maneira de identificar ele facilmente sem um banco) e retornar se existe ou não
    @WebMethod
    public String consultaCliente(String email) {
        try {
            if (email.isBlank()) {
                return "Coloque o email para consultar um cliente!";
            }
            Cliente client = clienteDao.consultaCliente(email);

            if (client != null && email.equals(client.getEmail())) {
                return "Cliente encontrado com sucesso";

            }
            return "O cliente não foi encontrado!";

        } catch (RuntimeException e) {
            return "Erro ao procurar cliente: " + e.getMessage();
        }
    }

    // Recebe email do cliente para excluí-lo e retorna mensagem de sucesso/erro
    @WebMethod
    public String excluirCliente(String email) {
        try {
            if (email.isBlank()) {
                return "Coloque o email para excluir um cliente!";
            }
            Cliente client = clienteDao.consultaCliente(email);

            if (client == null) {
                return "O cliente não foi excluido, informe um email cadastrado!";

            }
            clienteDao.excluiCliente(email);

            return "O cliente foi removido!";

        } catch (RuntimeException e) {
            return "Erro ao excluir cliente: " + e.getMessage();
        }
    }

    // Recebe todos os parâmetros para modificar e retorna mensagem de sucesso/erro
    @WebMethod
    public String alterarDadosCliente(String nome, String email, String cpf) {

        try {
            if (nome.isBlank() || email.isBlank() || cpf.isBlank()) {
                return "Coloque todos os valores para alterar um cliente!";
            }
            clienteDao.alteraDadosCliente(nome, email, cpf);

            // Validamos se o cliente foi encontrado com sucesso
            Cliente client = clienteDao.consultaClienteCpf(cpf);

            if (client == null) {
                return "O cliente não foi encontrado, informe um cliente cadastrado!";

            }
            clienteDao.alteraDadosCliente(nome, email, cpf);

            Cliente clientAlt = clienteDao.consultaCliente(email);
            if (clientAlt.getEmail().equals(email)) {
                return "Os dados foram alterados corretamente!";

            }
            return "Aconteceu algum erro os dados não foram alterados, tente novamente!";
        } catch (RuntimeException e) {
            return "Erro ao criar cliente: " + e.getMessage();
        }
    }


}
