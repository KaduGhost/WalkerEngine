package br.com.redewalker.kaduzin.engine.sistema.placeholder;

import java.text.NumberFormat;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.chat.Canal;
import br.com.redewalker.kaduzin.engine.sistema.grupo.Grupo;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;

public class PlaceHookWalkers extends PlaceholderHook {

	public PlaceHookWalkers(String nome) throws Exception {
		super(nome);
	}

	@Override
	public String checkPlaceholder(String pattern, Usuario j, Canal canal) {
		String formato = pattern.toLowerCase();
		Grupo grupo = WalkerEngine.get().getGruposManager().getGrupo(j.getGrupoIn());
		Grupo gpout = WalkerEngine.get().getGruposManager().getGrupo(j.getGrupoOut());
		switch (formato) {
		case "%grupo_cor%":
			return grupo.getTag().getCor().toString();
		case "%grupo_tag%":
			return grupo.getTag().getTag();
		case "%grupo_tag_full%":
			return grupo.getTag().toString();
		case "%grupo_nome%":
			return grupo.getNome();
		case "%grupoout_tag%":
			return gpout.getTag().toString();
		case "%grupoout_tag_full%":
			return gpout.getTag().toString();
		case "%grupoout_nome%":
			return gpout.getNome();
		case "%jogador_nick%":
			return j.getNickOriginal();
		case "%jogador_tag%":
			return grupo.getTag().getTag();
		case "%jogador_tag_full%":
			return grupo.getTag().toString();
		case "%server_tag%":
			return WalkerEngine.get().getChatManager().getChatTag("{server}").getTag();
		case "%server_tag_full%":
			return WalkerEngine.get().getChatManager().getChatTag("{server}").getCor()+WalkerEngine.get().getChatManager().getChatTag("{server}").getTag();
		case "%server_nome%":
			return WalkerEngine.get().getServerType().toString();
		case "%server_cor%":
			return WalkerEngine.get().getChatManager().getChatTag("{server}").getCor();
		case "%jogador_cash%":
			return NumberFormat.getInstance().format(j.getCash());
		case "%chat_tag%":
			return canal.getTag().toString();
		case "%chat_tag_cor%":
			return canal.getTag().getCor().toString();
		default:
			return formato;
		}
	}

}
