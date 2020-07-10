package de.kid2407.bteplugin.command;

import de.kid2407.bteplugin.BTEPlugin;
import de.kid2407.bteplugin.Permissions;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * User: Tobias Franz
 * Date: 04.07.2020
 * Time: 10:15
 */
public class BuildCommand implements CommandExecutor {

    private final GameMode firstGameMode;
    private final GameMode secondGameMode;

    public BuildCommand(GameMode firstGameMode, GameMode secondGameMode) {
        this.firstGameMode = firstGameMode;
        this.secondGameMode = secondGameMode;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(Permissions.BUILD_USE.getPermission())) {
                GameMode playerGameMode = player.getGameMode();
                if (playerGameMode.equals(firstGameMode) || playerGameMode.equals(secondGameMode)) {
                    GameMode newGameMode = playerGameMode.equals(firstGameMode) ? secondGameMode : firstGameMode;
                    player.setGameMode(newGameMode);
                    player.sendMessage(BTEPlugin.PREFIX + "Gamemode zu " + newGameMode.name() + " geändert.");
                } else {
                    player.sendMessage(BTEPlugin.PREFIX + "Du musst in " + firstGameMode.name() + " oder " + secondGameMode.name() + " sein, um diesen Befehl nutzen zu können.");
                }
            } else {
                player.sendMessage(BTEPlugin.PREFIX + "Du hast keine Berechtigung, diesen Befehl zu benutzen!");
            }

            return true;
        }

        sender.sendMessage(BTEPlugin.PREFIX + "Kann nur von einem Spieler benutzt werden!");
        return false;
    }
}
