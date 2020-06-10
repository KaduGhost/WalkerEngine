package br.com.redewalker.kaduzin.engine.sistema.placeholder;

import br.com.redewalker.kaduzin.engine.sistema.chat.Canal;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public abstract class PlaceholderHook {
	
	private String nome;
	
	public PlaceholderHook(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public abstract String checkPlaceholder(String pattern, Usuario j, Canal canal);

}
