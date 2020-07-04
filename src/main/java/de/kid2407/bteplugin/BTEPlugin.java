package de.kid2407.bteplugin;

import de.kid2407.bteplugin.command.BuildCommand;
import de.kid2407.bteplugin.command.FlyCommand;
import de.kid2407.bteplugin.command.SpeedCommand;
import de.kid2407.bteplugin.command.VisitCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class BTEPlugin extends JavaPlugin {

    public final static String PREFIX = ChatColor.AQUA + "[BTE-Plugin] " + ChatColor.WHITE;

    @Override
    public void onEnable() {
        Logger logger = getLogger();

        logger.info("Registriere Kommandos");

        FlyCommand flyCommand = new FlyCommand();
        SpeedCommand speedCommand = new SpeedCommand();
        BuildCommand buildCommand = new BuildCommand();
        VisitCommand visitCommand = new VisitCommand();

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
}
