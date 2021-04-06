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

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
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

		// Ação
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

		// A��o
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

		// Ação
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

		// A��o
		banco.efetuarTransferencia(123, 456, 100);

		// Verifica��o
		assertEquals(-100, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}

	@Test
	public void deveEfetuarTransferenciaContasCorrenteEPoupan�a()
			throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 1000, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 500, TipoConta.POUPANCA);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// A��o
		banco.efetuarTransferencia(123, 456, 100);

		// Verifica��o
		assertEquals(900, contaOrigem.getSaldo());
		assertEquals(600, contaDestino.getSaldo());

	}

	// Verifica��o
	@Test(expected = ContaNaoExistenteException.class)
	public void naoDeveEfetuarTransferenciaSeContaOrigemNaoExiste()
			throws ContaNaoExistenteException, TransferenciaInvalidaException, ContaSemSaldoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 1000, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 120, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaDestino));

		// A��o
		banco.efetuarTransferencia(123, 456, 100);

	}

	@Test(expected = ContaSemSaldoException.class)
	public void naoDevePermitirContaOrigemTipoPoupan�aFicarComSaldoNegativo()
			throws ContaSemSaldoException, ContaNaoExistenteException, TransferenciaInvalidaException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 100, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 500, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// A��o
		banco.efetuarTransferencia(123, 456, 1000);

		// Verifica��o

	}

	// Verifica��o
	@Test(expected = ContaNaoExistenteException.class)
	public void naoDeveEfetuarTransferenciaSeContaDestinoNaoExiste()
			throws ContaNaoExistenteException, TransferenciaInvalidaException, ContaSemSaldoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 1000, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 120, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem));

		// A��o
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

		// A��o
		banco.efetuarTransferencia(123, 456, -100);

		// Verifica��o
		Assert.fail();
	}

}
