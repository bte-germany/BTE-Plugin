package de.kid2407.bteplugin.spigot.command;

import de.kid2407.bteplugin.spigot.BTEPlugin;
import de.kid2407.bteplugin.spigot.Permissions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User: Tobias Franz
 * Date: 03.07.2020
 * Time: 07:44
 */
public class VisitCommand implements CommandExecutor, TabCompleter, Listener {

    private final ArrayList<String> subcommands = new ArrayList<>(Arrays.asList("come", "create", "help", "join", "leave", "start", "stop"));

    private Player tourLeader = null;

    private final ArrayList<Player> visitors = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                TextComponent baseText = new TextComponent(BTEPlugin.PREFIX + "Bitte gib einen gültigen Sub-Befehl ein. Für mehr Infos benutze ");
                TextComponent clickText = new TextComponent("/visit help");
                clickText.setBold(true);
                clickText.setColor(ChatColor.BLUE);
                clickText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/visit help"));
                baseText.addExtra(clickText);
                player.spigot().sendMessage(baseText);
                return true;
            }

            String subcommand = args[0];
            if (!subcommands.contains(subcommand)) {
                player.sendMessage(BTEPlugin.PREFIX + "Unbekannter Befehl /visit " + subcommand);
                return true;
            }

            switch (subcommand) {
                case "come":
                    command_come(player);
                    break;
                case "create":
                    command_create(player);
                    break;
                case "help":
                    command_help(player);
                    break;
                case "join":
                    command_join(player);
                    break;
                case "leave":
                    command_leave(player);
                    break;
                case "start":
                    command_start(player);
                    break;
                case "stop":
                    command_stop(player);
                    break;
            }

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 0) {
            return subcommands.stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        }
        return subcommands;
    }

    private boolean canUseLeaderCommand(Player player) {
        if (!player.hasPermission(Permissions.VISIT_CREATE.getPermission())) {
            player.sendMessage(BTEPlugin.PREFIX + "Du hast keine Berechtigung, diesen Befehl zu benutzen.");
            return false;
        }

        if (tourLeader == null) {
            player.sendMessage(BTEPlugin.PREFIX + "Es gibt keine aktive Tour.");
            return false;
        }

        if (!tourLeader.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(BTEPlugin.PREFIX + "Du bist nicht der Leiter der Tour.");
            return false;
        }

        return true;
    }

    private boolean canCreateTour(Player player) {
        if (!player.hasPermission(Permissions.VISIT_CREATE.getPermission())) {
            player.sendMessage(BTEPlugin.PREFIX + "Du hast keine Berechtigung, diesen Befehl zu benutzen.");
            return false;
        }

        if (tourLeader != null) {
            player.sendMessage(BTEPlugin.PREFIX + "Es ist bereits eine Tour aktiv.");
            return false;
        }

        return true;
    }

    public void command_come(Player player) {
        if (canUseLeaderCommand(player)) {
            player.sendMessage("Teleportiere " + visitors.size() + " Spieler.");

            for (Player p : visitors) {
                BTEPlugin.instance.sendPluginMessage("teleportPlayerToServer", player, p.getUniqueId().toString());
            }
        }
    }

    public void command_create(Player player) {
        if (canCreateTour(player)) {
            tourLeader = player;

            TextComponent base = new TextComponent(BTEPlugin.PREFIX + "Die Tour wurde erstellt!\nBenutze ");

            TextComponent textCome = new TextComponent("/visit come");
            textCome.setBold(true);
            textCome.setColor(ChatColor.BLUE);
            textCome.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/visit come"));

            base.addExtra(textCome);
            base.addExtra(" um alle Besucher zu dir zu teleportieren und ");

            TextComponent textStop = new TextComponent("/visit stop");
            textStop.setBold(true);
            textStop.setColor(ChatColor.BLUE);
            textStop.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/visit stop"));

            base.addExtra(textStop);
            base.addExtra(" um die Tour zu beenden.");

            player.spigot().sendMessage(base);

            TextComponent textStart = new TextComponent(BTEPlugin.PREFIX + "Um die Besucher über den Start der Tour zu informieren, benutze ");
            TextComponent textStartCommand = new TextComponent("/visit start");
            textStartCommand.setBold(true);
            textStartCommand.setColor(ChatColor.BLUE);
            textStartCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/visit start"));

            textStart.addExtra(textStartCommand);

            player.spigot().sendMessage(textStart);

            BTEPlugin.instance.sendPluginMessage("startTour", player);
        }
    }

    public void command_help(Player player) {
        TextComponent text = new TextComponent("----- " + BTEPlugin.PREFIX + "Hilfe\n");

        TextComponent command_come = new TextComponent("/visit come");
        command_come.setBold(true);
        command_come.setColor(ChatColor.BLUE);
        command_come.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/visit come"));
        TextComponent command_come_extra = new TextComponent(": Teleportiert alle Besucher zur aktuellen Position. Nur vom Tourleiter nutzbar\n");
        command_come_extra.setColor(ChatColor.RESET);
        command_come_extra.setBold(false);
        command_come.addExtra(command_come_extra);
        text.addExtra(command_come);

        TextComponent command_create = new TextComponent("/visit create");
        command_create.setBold(true);
        command_create.setColor(ChatColor.BLUE);
        command_create.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/visit create"));
        TextComponent command_create_extra = new TextComponent(": Erstellt eine neue Tour\n");
        command_create_extra.setColor(ChatColor.RESET);
        command_create_extra.setBold(false);
        command_create.addExtra(command_create_extra);
        text.addExtra(command_create);

        TextComponent command_help = new TextComponent("/visit help");
        command_help.setBold(true);
        command_help.setColor(ChatColor.BLUE);
        command_help.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/visit help"));
        TextComponent command_help_extra = new TextComponent(": Zeigt diese Hilfe an\n");
        command_help_extra.setColor(ChatColor.RESET);
        command_help_extra.setBold(false);
        command_help.addExtra(command_help_extra);
        text.addExtra(command_help);

        TextComponent command_join = new TextComponent("/visit join");
        command_join.setBold(true);
        command_join.setColor(ChatColor.BLUE);
        command_join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/visit join"));
        TextComponent command_join_extra = new TextComponent(": Trete der Tour als Besucher bei\n");
        command_join_extra.setColor(ChatColor.RESET);
        command_join_extra.setBold(false);
        command_join.addExtra(command_join_extra);
        text.addExtra(command_join);

        TextComponent command_leave = new TextComponent("/visit leave");
        command_leave.setBold(true);
        command_leave.setColor(ChatColor.BLUE);
        command_leave.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/visit leave"));
        TextComponent command_leave_extra = new TextComponent(": Verlasse die aktuelle Tour\n");
        command_leave_extra.setColor(ChatColor.RESET);
        command_leave_extra.setBold(false);
        command_leave.addExtra(command_leave_extra);
        text.addExtra(command_leave);

        TextComponent command_start = new TextComponent("/visit start");
        command_start.setBold(true);
        command_start.setColor(ChatColor.BLUE);
        command_start.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/visit start"));
        TextComponent command_start_extra = new TextComponent(": Benachrichtige alle Besucher, dass die Tour jetzt startet\n");
        command_start_extra.setColor(ChatColor.RESET);
        command_start_extra.setBold(false);
        command_start.addExtra(command_start_extra);
        text.addExtra(command_start);

        TextComponent command_stop = new TextComponent("/visit stop");
        command_stop.setBold(true);
        command_stop.setColor(ChatColor.BLUE);
        command_stop.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/visit stop"));
        TextComponent command_stop_extra = new TextComponent(": Beende die aktuelle Tour\n");
        command_stop_extra.setColor(ChatColor.RESET);
        command_stop_extra.setBold(false);
        command_stop.addExtra(command_stop_extra);
        text.addExtra(command_stop);

        player.spigot().sendMessage(text);
    }

    public void command_join(Player player) {
        if (tourLeader == null) {
            player.sendMessage(BTEPlugin.PREFIX + "Es gibt keine aktive Tour.");
            return;
        }

        if (tourLeader.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(BTEPlugin.PREFIX + "Du kannst nicht deiner eigenen Tour beitreten.");
            return;
        }

        if (visitors.contains(player)) {
            player.sendMessage(BTEPlugin.PREFIX + "Du bist der Tour bereits beigetreten.");
        }

        addVisitor(player);

        player.sendMessage(BTEPlugin.PREFIX + "Du bist der Tour beigetreten.");
    }

    public void command_leave(Player player) {
        if (tourLeader == null) {
            player.sendMessage(BTEPlugin.PREFIX + "Es gibt keine aktive Tour.");
            return;
        }

        if (tourLeader.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(BTEPlugin.PREFIX + "Du kannst nicht deiner eigenen Tour beitreten.");
            return;
        }

        if (!visitors.contains(player)) {
            player.sendMessage(BTEPlugin.PREFIX + "Du bist in keiner Tour.");
        }

        removeVisitor(player);

        player.sendMessage(BTEPlugin.PREFIX + "Du hast die Tour verlassen.");
    }

    public void command_start(Player player) {
        if (canUseLeaderCommand(player)) {
            for (Player visitor : visitors) {
                visitor.sendMessage(BTEPlugin.PREFIX + "Die Tour beginnt jetzt.");
            }

            player.sendMessage(BTEPlugin.PREFIX + "Besucher wurden benachrichtigt.");
        }
    }

    public void command_stop(Player player) {
        command_stop(player, false);
    }

    public void command_stop(Player player, boolean silent) {
        if (canUseLeaderCommand(player)) {
            if (!silent) {
                String message = BTEPlugin.PREFIX + "Die Tour wurde beendet.";

                for (Player visitor : visitors) {
                    visitor.sendMessage(message);
                }

                player.sendMessage(message);
            }

            tourLeader = null;
            visitors.clear();

            BTEPlugin.instance.sendPluginMessage("stopTour", player);
        }
    }

    private void removeVisitor(Player player) {
        removeVisitor(player, true);
    }

    public void removeVisitor(Player player, boolean sendPluginMessage) {
        visitors.remove(player);
        tourLeader.sendMessage(BTEPlugin.PREFIX + player.getDisplayName() + " hat die Tour verlassen.");

        if (sendPluginMessage) {
            BTEPlugin.instance.sendPluginMessage("requestRemovePlayer", player, player.getUniqueId().toString());
        }
    }

    private void addVisitor(Player player) {
        addVisitor(player, true);
    }

    public void addVisitor(Player player, boolean sendPluginMessage) {
        visitors.add(player);
        tourLeader.sendMessage(BTEPlugin.PREFIX + player.getDisplayName() + " ist der Tour beigetreten.");

        if (sendPluginMessage) {
            BTEPlugin.instance.sendPluginMessage("requestAddPlayer", player, player.getUniqueId().toString());
        }
    }

    public void teleportVisitor(UUID uuid) {
        Player visitor = Bukkit.getPlayer(uuid);

        if (visitors.contains(visitor)) {
            Location leadLocation = tourLeader.getLocation();
            int randomX;
            int randomZ;
            Random random = new Random();
            randomX = random.nextInt(5) * (random.nextBoolean() ? 1 : -1);
            randomZ = random.nextInt(5) * (random.nextBoolean() ? 1 : -1);

            Location teleportLocation = leadLocation.clone();
            teleportLocation.add(randomX, 0, randomZ);

            visitor.teleport(teleportLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    public void startTour(UUID uuid) {
        this.tourLeader = Bukkit.getPlayer(uuid);
    }

    public void stopTour() {
        tourLeader = null;
        visitors.clear();
    }
}
