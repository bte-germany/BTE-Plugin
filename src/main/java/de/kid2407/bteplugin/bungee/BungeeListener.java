package de.kid2407.bteplugin.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

/**
 * User: Tobias Franz
 * Date: 14.07.2020
 * Time: 18:44
 */
public class BungeeListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("bteplugin")) {
            return;
        }
        //noinspection UnstableApiUsage
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String command = in.readUTF();
        if (event.getReceiver() instanceof ProxiedPlayer) {
            if (command.startsWith("request")) {
                String origin = ((ProxiedPlayer) event.getReceiver()).getServer().getInfo().getName();
                ProxyServer.getInstance().getServers().forEach((name, serverInfo) ->
                {
                    if (!name.equals(origin)) {
                        //noinspection UnstableApiUsage
                        ByteArrayDataOutput data = ByteStreams.newDataOutput();
                        data.writeUTF(command.replace("request", "response"));
                        data.writeUTF(in.readUTF());

                        serverInfo.sendData("bteplugin", data.toByteArray());
                    }
                });
            } else if (command.equals("teleportPlayerToServer")) {
                String targetServerName = in.readUTF();
                ServerInfo targetServer = ProxyServer.getInstance().getServerInfo(targetServerName);
                UUID playerUUID = UUID.fromString(in.readUTF());
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
                if (targetServer != null && player != null) {
                    player.connect(targetServer, (b, throwable) -> {
                        if (b) {
                            //noinspection UnstableApiUsage
                            ByteArrayDataOutput data = ByteStreams.newDataOutput();
                            data.writeUTF("teleport");
                            data.writeUTF(playerUUID.toString());

                            player.getServer().sendData("bteplugin", data.toByteArray());
                        }
                    });
                }
            } else if (command.equals("stopTour")) {
                String origin = ((ProxiedPlayer) event.getReceiver()).getServer().getInfo().getName();
                ProxyServer.getInstance().getServers().forEach((name, serverInfo) ->
                {
                    if (!name.equals(origin)) {
                        //noinspection UnstableApiUsage
                        ByteArrayDataOutput data = ByteStreams.newDataOutput();
                        data.writeUTF(command);

                        serverInfo.sendData("bteplugin", data.toByteArray());
                    }
                });
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent playerDisconnectEvent) {
        //noinspection UnstableApiUsage
        ByteArrayDataOutput data = ByteStreams.newDataOutput();
        data.writeUTF("responseRemovePlayer");
        data.writeUTF(playerDisconnectEvent.getPlayer().getUniqueId().toString());

        ProxyServer.getInstance().getServers().forEach(((name, serverInfo) -> serverInfo.sendData("bteplugin", data.toByteArray())));
    }

}
