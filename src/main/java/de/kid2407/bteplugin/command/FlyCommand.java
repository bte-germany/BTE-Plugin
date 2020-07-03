package de.kid2407.bteplugin.command;

import de.kid2407.bteplugin.BTEPlugin;
import de.kid2407.bteplugin.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * User: Tobias Franz
 * Date: 02.07.2020
 * Time: 19:13
 */
public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Permissions.FLY_USE.getPermission())) {
                player.setAllowFlight(!player.getAllowFlight());
            }

            return true;
        }

        sender.sendMessage(BTEPlugin.PREFIX + "Kann nur von einem Spieler benutzt werden!");
        return false;
    }
}
