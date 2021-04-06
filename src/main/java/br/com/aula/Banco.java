package main.java.br.com.aula;

import java.util.ArrayList;
import java.util.List;

import main.java.br.com.aula.exception.ContaInvalidaException;
import main.java.br.com.aula.exception.ContaJaExistenteException;
import main.java.br.com.aula.exception.ContaNaoExistenteException;
import main.java.br.com.aula.exception.ContaSemSaldoException;
import main.java.br.com.aula.exception.TransferenciaInvalidaException;

public class Banco {

	private List<Conta> contas = new ArrayList<Conta>();

	public Banco() {
	}

	public Banco(List<Conta> contas) {
		this.contas = contas;
	}


	public void cadastrarConta(Conta conta) throws ContaJaExistenteException, ContaInvalidaException {
		
		boolean isNumeroDeContaValido = conta.getNumeroConta() > 0;
		
		if(isNumeroDeContaValido) {
			for (Conta c : contas) {
				boolean isNomeClienteIgual = c.getCliente().getNome().equals(conta.getCliente().getNome());
				boolean isNumeroContaIgual = c.getNumeroConta() == conta.getNumeroConta();

				if (isNomeClienteIgual || isNumeroContaIgual) {
					throw new ContaJaExistenteException();
				}
			}
			
			this.contas.add(conta);
			
		} else {
			throw new ContaInvalidaException();
		}

	}

	public void efetuarTransferencia(int numeroContaOrigem, int numeroContaDestino, int valor)
			throws ContaNaoExistenteException, ContaSemSaldoException, TransferenciaInvalidaException {

		Conta contaOrigem = this.obterContaPorNumero(numeroContaOrigem);
		Conta contaDestino = this.obterContaPorNumero(numeroContaDestino);

		boolean isContaOrigemExistente = contaOrigem != null;
		boolean isContaDestinoExistente = contaDestino != null;
		boolean isValorTransferidoValido = valor >= 0;
		
		if(isValorTransferidoValido) {
			if (isContaOrigemExistente && isContaDestinoExistente) {

				boolean isContaOrigemPoupança = contaOrigem.getTipoConta().equals(TipoConta.POUPANCA);
				boolean isSaldoContaOrigemNegativo = contaOrigem.getSaldo() - valor < 0;

				if (isContaOrigemPoupança && isSaldoContaOrigemNegativo) {
					throw new ContaSemSaldoException();
				}
				
				contaOrigem.debitar(valor);
				contaDestino.creditar(valor);

			} else {
				throw new ContaNaoExistenteException();
			}
		}else {
			throw new TransferenciaInvalidaException();
		}

	}

	public Conta obterContaPorNumero(int numeroConta) {

		for (Conta c : contas) {
			if (c.getNumeroConta() == numeroConta) {
				return c;
			}
		}

		return null;
	}

	public List<Conta> obterContas() {
		return this.contas;
	}
	
}
