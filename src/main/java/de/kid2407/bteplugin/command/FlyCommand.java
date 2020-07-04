package de.kid2407.bteplugin.command;

import de.kid2407.bteplugin.BTEPlugin;
import de.kid2407.bteplugin.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * User: Tobias Franz
 * Date: 02.07.2020
 * Time: 19:13
 */
public class FlyCommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Permissions.FLY_USE.getPermission())) {
                boolean newAllowFlight = !player.getAllowFlight();
                player.setAllowFlight(newAllowFlight);
                if (newAllowFlight) {
                    player.sendMessage("Fliegen " + ChatColor.GREEN + "aktiviert.");
                } else {
                    player.sendMessage("Fliegen " + ChatColor.RED + "deaktiviert.");
                }
            }

            return true;
        }

        sender.sendMessage(BTEPlugin.PREFIX + "Kann nur von einem Spieler benutzt werden!");
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        if (player.hasPermission(Permissions.FLY_JOIN.getPermission())) {
            player.setAllowFlight(true);
        }
    }
}
