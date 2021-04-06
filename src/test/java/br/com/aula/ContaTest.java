package test.java.br.com.aula;

import static org.junit.Assert.*;

import org.junit.Test;

import main.java.br.com.aula.Cliente;
import main.java.br.com.aula.Conta;
import main.java.br.com.aula.TipoConta;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContaTest {

	@Test
	public void deveCreditar() {

		// Cenario
		Cliente cliente = new Cliente("Jo�o");
		Conta c = new Conta(cliente, 123, 10, TipoConta.CORRENTE);

		// A��o
		c.creditar(5);

		// Verifica��o
		assertEquals(15, c.getSaldo());
		assertThat(c.getSaldo(), is(15));
	}

}
