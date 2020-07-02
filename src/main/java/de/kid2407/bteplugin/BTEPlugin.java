package de.kid2407.bteplugin;

import de.kid2407.bteplugin.command.FlyCommand;
import de.kid2407.bteplugin.command.SpeedCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class BTEPlugin extends JavaPlugin {

    public final static String PREFIX = "§" + ChatColor.AQUA.getChar() + "[BTE-Plugin] §" + ChatColor.WHITE.getChar();

    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("fly").setExecutor(new FlyCommand());
        Bukkit.getPluginCommand("speed").setExecutor(new SpeedCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
