package de.kid2407.bteplugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Tobias Franz
 * Date: 03.07.2020
 * Time: 07:44
 */
public class VisitCommand implements CommandExecutor, TabCompleter {

    private final ArrayList<String> subcommands = new ArrayList<>(Arrays.asList("come", "create", "end", "join", "leave", "start"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
