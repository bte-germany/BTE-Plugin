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

    public final static String PREFIX = "§" + ChatColor.AQUA.getChar() + "[BTE-Plugin] §" + ChatColor.WHITE.getChar();

    @Override
    public void onEnable() {
        Logger logger = getLogger();

        logger.info("Registriere Commands");
        Bukkit.getPluginCommand("fly").setExecutor(new FlyCommand());
        Bukkit.getPluginCommand("speed").setExecutor(new SpeedCommand());
        Bukkit.getPluginCommand("build").setExecutor(new BuildCommand());
        Bukkit.getPluginCommand("visit").setExecutor(new VisitCommand());
        logger.info("Commands erfolreich registriert");

        getServer().getPluginManager().registerEvents(new SpeedCommand(), this);
        getServer().getPluginManager().registerEvents(new FlyCommand(), this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setWalkSpeed(SpeedCommand.PLAYER_DEFAULT_WALK_SPEED);
            player.setFlySpeed(SpeedCommand.PLAYER_DEFAULT_FLY_SPEED);
        }
    }
}
