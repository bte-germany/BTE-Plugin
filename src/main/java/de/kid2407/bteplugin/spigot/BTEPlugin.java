package de.kid2407.bteplugin.spigot;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.kid2407.bteplugin.spigot.command.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;
import java.util.logging.Logger;

public final class BTEPlugin extends JavaPlugin implements PluginMessageListener {

    public static BTEPlugin instance;

    private VisitCommand visitCommand;

    public final static String PREFIX = ChatColor.AQUA + "[BTE-Plugin] " + ChatColor.WHITE;

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "bteplugin", this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "bteplugin");

        instance = this;
        Logger logger = getLogger();

        logger.info("Registriere Kommandos");

        generateDefaultConfig();
        FileConfiguration configuration = getConfig();
        int firstGameModeId = configuration.getInt("firstGamemode", 1);
        int secondGameModeId = configuration.getInt("secondGamemode", 2);
        //noinspection deprecation
        GameMode firstGameMode = GameMode.getByValue(firstGameModeId);
        //noinspection deprecation
        GameMode secondGameMode = GameMode.getByValue(secondGameModeId);

        FlyCommand flyCommand = new FlyCommand();
        SpeedCommand speedCommand = new SpeedCommand();
        BuildCommand buildCommand = new BuildCommand(firstGameMode, secondGameMode);
        this.visitCommand = new VisitCommand();

        Bukkit.getPluginCommand("fly").setExecutor(flyCommand);
        Bukkit.getPluginCommand("speed").setExecutor(speedCommand);
        Bukkit.getPluginCommand("build").setExecutor(buildCommand);
        Bukkit.getPluginCommand("visit").setExecutor(visitCommand);
        logger.info("Kommandos erfolreich registriert");

        getServer().getPluginManager().registerEvents(flyCommand, this);
        getServer().getPluginManager().registerEvents(speedCommand, this);
        getServer().getPluginManager().registerEvents(visitCommand, this);
        logger.info("Events erfolreich registriert");
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setWalkSpeed(SpeedCommand.PLAYER_DEFAULT_WALK_SPEED);
            player.setFlySpeed(SpeedCommand.PLAYER_DEFAULT_FLY_SPEED);
        }
    }

    private void generateDefaultConfig() {
        FileConfiguration config = getConfig();
        //noinspection deprecation
        config.addDefault("firstGamemode", GameMode.CREATIVE.getValue());
        //noinspection deprecation
        config.addDefault("secondGamemode", GameMode.ADVENTURE.getValue());
        config.options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("bteplugin")) {
            return;
        }
        //noinspection UnstableApiUsage
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String command = in.readUTF();
        UUID playerUUID = UUID.fromString(in.readUTF());
        getLogger().info("Command: " + command);

        switch (command) {
            case "responseAddPlayer":
                visitCommand.addVisitor(Bukkit.getPlayer(playerUUID), false);
                break;
            case "responseRemovePlayer":
                visitCommand.removeVisitor(Bukkit.getPlayer(playerUUID), false);
                break;
            case "teleport":
                visitCommand.teleportVisitor(playerUUID);
                break;
            case "startTour":
                visitCommand.startTour(playerUUID);
            case "stopTour":
                visitCommand.stopTour();
                break;
        }
    }

    public void sendPluginMessage(String command, Player player, String... message) {
        //noinspection UnstableApiUsage
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(command);
        for (String part : message) {
            output.writeUTF(part);
        }

        player.sendPluginMessage(this, "bteplugin", output.toByteArray());
        player.sendMessage("Gesendet!");
    }
}
