package test.java.br.com.aula;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import main.java.br.com.aula.Banco;
import main.java.br.com.aula.Cliente;
import main.java.br.com.aula.Conta;
import main.java.br.com.aula.TipoConta;
import main.java.br.com.aula.exception.ContaInvalidaException;
import main.java.br.com.aula.exception.ContaJaExistenteException;
import main.java.br.com.aula.exception.ContaNaoExistenteException;
import main.java.br.com.aula.exception.ContaSemSaldoException;
import main.java.br.com.aula.exception.TransferenciaInvalidaException;

public class BancoTest {

	/************ Cadastro de contas ************/
	@Test
	public void deveCadastrarConta() throws ContaJaExistenteException, ContaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// AÃ§Ã£o
		banco.cadastrarConta(conta);

		// VerificaÃ§Ã£o
		assertEquals(1, banco.obterContas().size());
	}

	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaComNumeroRepetido() throws ContaJaExistenteException, ContaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// AÃ§Ã£o
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}

	@Test(expected = ContaInvalidaException.class)
	public void naoDeveCadastrarContaComNumeroInvalido() throws ContaJaExistenteException, ContaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 0, 0, TipoConta.CORRENTE);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);

		Assert.fail();
	}

	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaComNomeRepetido() throws ContaJaExistenteException, ContaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Joao");
		Conta conta2 = new Conta(cliente2, 456, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// AÃ§Ã£o
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		Assert.fail();
	}

	/************ Transferencia entre contas ************/
	@Test
	public void deveEfetuarTransferenciaContasCorrentes()
			throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(-100, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}

	@Test
	public void deveEfetuarTransferenciaContasCorrenteEPoupança()
			throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 1000, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 500, TipoConta.POUPANCA);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(900, contaOrigem.getSaldo());
		assertEquals(600, contaDestino.getSaldo());

	}

	// Verificação
	@Test(expected = ContaNaoExistenteException.class)
	public void naoDeveEfetuarTransferenciaSeContaOrigemNaoExiste()
			throws ContaNaoExistenteException, TransferenciaInvalidaException, ContaSemSaldoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 1000, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 120, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

	}

	@Test(expected = ContaSemSaldoException.class)
	public void naoDevePermitirContaOrigemTipoPoupançaFicarComSaldoNegativo()
			throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 100, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 500, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 1000);

		// Verificação

	}

	// Verificação
	@Test(expected = ContaNaoExistenteException.class)
	public void naoDeveEfetuarTransferenciaSeContaDestinoNaoExiste()
			throws ContaNaoExistenteException, TransferenciaInvalidaException, ContaSemSaldoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 1000, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 120, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

	}

	@Test(expected = TransferenciaInvalidaException.class)
	public void naoDeveTransferirValorNegativo()
			throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 1000, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 120, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, -100);

		// Verificação
		Assert.fail();
	}

}
