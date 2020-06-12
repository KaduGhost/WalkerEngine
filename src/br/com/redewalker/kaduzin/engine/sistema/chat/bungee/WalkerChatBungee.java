package br.com.redewalker.kaduzin.engine.sistema.chat.bungee;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import br.com.redewalker.kaduzin.engine.WalkerEngine;
import br.com.redewalker.kaduzin.engine.sistema.chat.Canal;
import br.com.redewalker.kaduzin.engine.sistema.usuario.Usuario;
import br.com.redewalker.kaduzin.engine.utils.WalkersUtils;
import br.net.fabiozumbi12.MinEmojis.Fanciful.FancyMessage;

public class WalkerChatBungee implements PluginMessageListener, Listener {

    public static void sendBungee(Canal ch, FancyMessage text) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(WalkerEngine.get().getServerType().toString());
        out.writeUTF(ch.getNome());
        out.writeUTF(text.toJSONString());

        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        try {
            p.sendPluginMessage(WalkerEngine.get(), "bungee:walkerchat", out.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("bungee:walkerchat")) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        String id = "";
        String ch = "";
        String json = "";
        try {
            id = in.readUTF();
            ch = in.readUTF();
            json = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Canal chan = WalkerEngine.get().getChatManager().getCanal(ch);
        if (chan == null || !chan.isBungee()) return;
        if (chan.getDistancia() == 0) {
        	for (Player receiver : Bukkit.getServer().getOnlinePlayers()) {
        		Usuario usuario = WalkerEngine.get().getUsuarioManager().getUsuario(receiver.getName());
        		if (usuario != null && usuario.hasPermission("walker.chat.chat"+chan.getNome())) {
        			WalkersUtils.performCommand(receiver, Bukkit.getConsoleSender(), "tellraw " + receiver.getName() + " " + json);
        		}
            }
        }
        Bukkit.getConsoleSender().sendMessage("Mensagem bungee para o canal " + chan.getNome() + " do server: " + id);
    }
}