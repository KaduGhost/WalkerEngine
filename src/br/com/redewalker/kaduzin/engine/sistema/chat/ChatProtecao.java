package br.com.redewalker.kaduzin.engine.sistema.chat;

import java.util.HashMap;
import java.util.regex.Pattern;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.apis.MensagensAPI;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils;

public class ChatProtecao {
	
	private static final HashMap<Usuario, String> chatSpam = new HashMap<>();
	private static final HashMap<Usuario, Long> delayChatSpam = new HashMap<>();
    private static final HashMap<Usuario, Integer> UrlSpam = new HashMap<>();
	
	public static String filtrarMensagem(Usuario j, String msg, Canal canal) {
		
		//antispam
        if (WalkerEngine.get().getConfigManager().getConfigChat().isAntiSpam() && !j.hasPermission("walker.chat.bypass-spam") && !WalkerEngine.get().getConfigManager().getConfigChat().getAntiSpamDesativarNosCanais().contains(canal.getNick())) {
            
        	//check spam messages
            if (!chatSpam.containsKey(j) || !chatSpam.get(j).equalsIgnoreCase(msg)) {
                chatSpam.put(j, msg);
                delayChatSpam.put(j, System.currentTimeMillis()+System.currentTimeMillis()+(WalkerEngine.get().getConfigManager().getConfigChat().getAntiSpamTempoEntreMensagens()*1000L));
            } else if (chatSpam.get(j).equalsIgnoreCase(msg)) {
            	if (delayChatSpam.get(j) > System.currentTimeMillis()) MensagensAPI.mensagemErro("Você não pode enviar mensagens parecidas tão rápido", j.getPlayer());
            	else {
            		chatSpam.remove(j);
            		delayChatSpam.remove(j);
            	}
            	return null;
            }
        }
        
        String regexIP = WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPRegexIP();
        String regexUrl = WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPRegexURL();

        //check ip and website
        if (WalkerEngine.get().getConfigManager().getConfigChat().isAntiIP() && !j.hasPermission("walker.chat.bypass-antiip") && !WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPDesativarNosCanais().contains(canal.getNick())) {

        	HashMap<String, String> replaces = new HashMap<String, String>();
            for (String check : WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPWhitelist()) {
            	String i = WalkersUtils.gerarCodigoSimples(6);
            	while (replaces.containsKey(i)) i = WalkersUtils.gerarCodigoSimples(6);
                msg = msg.replace(check, i);
            	replaces.put(i, check);
            }

            if (Pattern.compile(regexIP).matcher(msg).find() || Pattern.compile(regexUrl).matcher(msg).find()) {
                addURLspam(j);
                if (WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPCancelarOuReplace().equalsIgnoreCase("cancelar")) {
                	MensagensAPI.mensagemErro("Não podes enviar urls neste canal :3", j.getPlayer());
                    return null;
                } else msg = msg.replaceAll(regexIP, WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPReplace()).replaceAll(regexUrl, WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPReplace());
            }
            
            for (String i : replaces.keySet()) {
            	msg = msg.replace(i, replaces.get(i));
            }
        }
        
        
		return msg;
	}
	
public static String filtrarMensagem(Usuario j, String msg) {
		
		//antispam
        if (WalkerEngine.get().getConfigManager().getConfigChat().isAntiSpam() && !j.hasPermission("walker.chat.bypass-spam")) {
            
        	//check spam messages
            if (!chatSpam.containsKey(j) || !chatSpam.get(j).equalsIgnoreCase(msg)) {
                chatSpam.put(j, msg);
                delayChatSpam.put(j, System.currentTimeMillis()+System.currentTimeMillis()+(WalkerEngine.get().getConfigManager().getConfigChat().getAntiSpamTempoEntreMensagens()*1000L));
            } else if (chatSpam.get(j).equalsIgnoreCase(msg)) {
            	if (delayChatSpam.get(j) > System.currentTimeMillis()) MensagensAPI.mensagemErro("Você não pode enviar mensagens parecidas tão rápido", j.getPlayer());
            	else {
            		chatSpam.remove(j);
            		delayChatSpam.remove(j);
            	}
            	return null;
            }
        }
        
        String regexIP = WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPRegexIP();
        String regexUrl = WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPRegexURL();

        //check ip and website
        if (WalkerEngine.get().getConfigManager().getConfigChat().isAntiIP() && !j.hasPermission("walker.chat.bypass-antiip")) {

        	HashMap<String, String> replaces = new HashMap<String, String>();
            for (String check : WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPWhitelist()) {
            	String i = WalkersUtils.gerarCodigoSimples(6);
            	while (replaces.containsKey(i)) i = WalkersUtils.gerarCodigoSimples(6);
                msg = msg.replace(check, i);
            	replaces.put(i, check);
            }

            if (Pattern.compile(regexIP).matcher(msg).find() || Pattern.compile(regexUrl).matcher(msg).find()) {
                addURLspam(j);
                if (WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPCancelarOuReplace().equalsIgnoreCase("cancelar")) {
                	MensagensAPI.mensagemErro("Não podes enviar urls neste canal :3", j.getPlayer());
                    return null;
                } else msg = msg.replaceAll(regexIP, WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPReplace()).replaceAll(regexUrl, WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPReplace());
            }
            
            for (String i : replaces.keySet()) {
            	msg = msg.replace(i, replaces.get(i));
            }
        }
        
        
		return msg;
	}
	
	private static void addURLspam(Usuario p) {
        if (WalkerEngine.get().getConfigManager().getConfigChat().isAntiIPPunicao()) {
            if (!UrlSpam.containsKey(p)) UrlSpam.put(p, 1);
            else {
                UrlSpam.put(p, UrlSpam.get(p) + 1);
                if (UrlSpam.get(p) >= WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPPunicaoMaxChances()) {
                	//int time = WalkerEngine.get().getConfigManager().getConfigChat().getAntiIPPunicaoTempo();
                	
                	//punir
                    UrlSpam.remove(p);
                }
            }
        }
    }

}
