package me.tagette.template.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import me.tagette.template.TDatabase;
import me.tagette.template.TLanguage;
import me.tagette.template.TLogger;
import me.tagette.template.TPermissions;
import me.tagette.template.TSettings;
import me.tagette.template.TTools;
import me.tagette.template.Template;
import me.tagette.template.extras.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @description Handles a command.
 * @author Tagette
 */
public class TemplateCmd implements CommandExecutor {

    private final Template plugin;

    public TemplateCmd(Template instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean handled = false;
        DataManager dbm = TDatabase.dbm;
        if (is(label, "template")) {
            if (args == null || args.length == 0) {
                handled = true;
                // Will send only if the sender is a player.
                sendMessage(sender, "You are using " + colorizeText(Template.name, ChatColor.GREEN)
                        + " version " + colorizeText(Template.version, ChatColor.GREEN) + ".");
                // Will send to the console only if the sender is NOT a player.
                sendLog(sender, Template.name + " version " + Template.version + ".");
            } else {
                // Put your commands in here
                if (is(args[0], "getawesome")) {
                    handled = true;
                    if ((isPlayer(sender) && TPermissions.permission(getPlayer(sender), "template.awesomeness", getPlayer(sender).isOp()))
                            || !isPlayer(sender)) {
                        if (args.length == 1 && isPlayer(sender)) {
                            String name = getName(sender);
                            int awesomeness = 0;
                            try {
                                ResultSet rs = dbm.query("SELECT awesomeness FROM players WHERE name = '" + name + "'");
                                if (rs != null && rs.next()) {
                                    awesomeness = rs.getInt("awesomeness");
                                }
                            } catch (SQLException se) {
                            }
                            if (awesomeness > 0) {
                                sendMessage(sender, "Your awesomeness is " + awesomeness + "!");
                            } else {
                                sendMessage(sender, "You do not have awesomeness.");
                            }
                        } else if (args.length == 2) {
                            String name = args[1];
                            if (!name.isEmpty()) {
                                int awesomeness = 0;
                                try {
                                    ResultSet rs = dbm.query("SELECT awesomeness FROM players WHERE name = '" + name + "'");
                                    if (rs.next()) {
                                        awesomeness = rs.getInt("awesomeness");
                                    }
                                } catch (SQLException se) {
                                }
                                if (awesomeness > 0) {
                                    sendMessage(sender, name + "'s awesomeness is " + awesomeness + "!");
                                    sendLog(sender, name + "'s awesomeness is " + awesomeness + "!");
                                } else {
                                    sendMessage(sender, name + " does not have awesomeness.");
                                    sendLog(sender, name + " does not have awesomeness.");
                                }
                            }
                        }
                    } else {
                        sendMessage(sender, TLanguage.getLanguage("noPermissions"));
                    }
                } else if (is(args[0], "setawesome")) {
                    handled = true;
                    if ((isPlayer(sender) && TPermissions.permission(getPlayer(sender), "template.awesomeness.set", getPlayer(sender).isOp()))
                            || !isPlayer(sender)) {
                        if (args.length == 2 && isPlayer(sender)) {
                            String name = getName(sender);
                            if (TTools.isInt(args[1])) {
                                int newAwesome = Integer.parseInt(args[1]);
                                if (newAwesome >= TSettings.lowestAwesome && newAwesome <= TSettings.highestAwesome) {
                                    try {
                                        boolean hasAwesome = dbm.query("SELECT * FROM players WHERE name = '" + name + "'").next();
                                        if (hasAwesome) {
                                            dbm.update("UPDATE players SET awesomeness = '" + newAwesome + "' WHERE name = '" + name + "'");
                                        } else {
                                            dbm.insert("INSERT INTO players (name, awesomeness) VALUES ('" + name + "', '" + newAwesome + "')");
                                        }
                                    } catch (SQLException se) {
                                    }
                                    sendMessage(sender, "Your awesomeness is now " + newAwesome + "!");
                                } else {
                                    sendMessage(sender, "Awesomeness can only be from "
                                            + TSettings.lowestAwesome + " to "
                                            + TSettings.highestAwesome + ".");
                                }
                            } else {
                                sendMessage(sender, args[1] + " is not an awesome level.");
                            }
                        } else if (args.length == 3) {
                            String name = args[1];
                            if (TTools.isInt(args[2])) {
                                int newAwesome = Integer.parseInt(args[2]);
                                if (newAwesome >= TSettings.lowestAwesome && newAwesome <= TSettings.highestAwesome) {
                                    try {
                                        boolean hasAwesome = dbm.query("SELECT * FROM players WHERE name = '" + name + "'").next();
                                        if (hasAwesome) {
                                            dbm.update("UPDATE players SET awesomeness = '" + newAwesome + "' WHERE name = '" + name + "'");
                                        } else {
                                            dbm.insert("INSERT INTO players (name, awesomeness) VALUES ('" + name + "', '" + newAwesome + "')");
                                        }
                                        sendMessage(sender, name + "'s awesomeness is now " + newAwesome + "!");
                                        sendLog(sender, name + "'s awesomeness is now " + newAwesome + "!");
                                    } catch (SQLException se) {
                                        sendMessage(sender, "Could not set " + name + "'s awesomeness.");
                                        sendLog(sender, "Could not set " + name + "'s awesomeness.");
                                    }
                                } else {
                                    sendMessage(sender, "Awesomeness can only be from "
                                            + TSettings.lowestAwesome + " to "
                                            + TSettings.highestAwesome + ".");
                                    sendLog(sender, "Awesomeness can only be from "
                                            + TSettings.lowestAwesome + " to "
                                            + TSettings.highestAwesome + ".");
                                }
                            } else {
                                sendMessage(sender, args[2] + " is not an awesome level.");
                                sendLog(sender, args[2] + " is not an awesome level.");
                            }
                        }
                    } else {
                        sendMessage(sender, TLanguage.getLanguage("noPermissions"));
                    }

                } else if (is(args[0], "debug")) {
                    handled = true;
                    if (isPlayer(sender)) {
                        if (TPermissions.isAdmin(getPlayer(sender))) {
                            // Toggles debug mode for player.
                            if (plugin.isDebugging(getPlayer(sender))) {
                                plugin.stopDebugging(getPlayer(sender));
                                sendMessage(sender, "You have exited debug mode for " + colorizeText(Template.name, ChatColor.GREEN) + ".");
                            } else {
                                plugin.startDebugging(getPlayer(sender));
                                sendMessage(sender, "You have entered debug mode for " + colorizeText(Template.name, ChatColor.GREEN) + ".");
                            }
                            if (plugin.inDebugMode()) {
                                TLogger.info("Debug mode initiated.");
                                sendMessage(sender, colorizeText(Template.name, ChatColor.GREEN) + " has begun debugging.");
                            } else {
                                TLogger.info("Debug mode terminated.");
                                sendMessage(sender, colorizeText(Template.name, ChatColor.GREEN) + " has stopped debugging.");
                            }
                        } else {
                            sendMessage(sender, TLanguage.getLanguage("noPermissions"));
                        }
                    } else {
                        if (plugin.inDebugMode()) {
                            plugin.stopDebugging();
                            TLogger.info("Debug mode terminated.");
                        } else {
                            plugin.startDebugging();
                            TLogger.info("Debug mode initiated.");
                        }
                    }
                } else if (is(args[0], "reload")) {
                    handled = true;
                    if (isPlayer(sender)) {
                        if (TPermissions.isAdmin(getPlayer(sender))) {
                            TSettings.load();
                            TLanguage.load();
                            sendMessage(sender, colorizeText(Template.name, ChatColor.GREEN) + " has been reloaded.");
                            TLogger.info("Config files reloaded.");
                        } else {
                            sendMessage(sender, TLanguage.getLanguage("noPermissions"));
                        }
                    } else {
                        TSettings.load();
                        TLanguage.load();
                        TLogger.info("Config files reloaded.");
                    }
                } else if (is(args[0], "help") || is(args[0], "?")) {
                    handled = true;
                    sendMessage(sender, "No help yet.");
                } else {
                    handled = true;
                    sendMessage(sender, "Unknown " + Template.name + " command: /" + label + " " + TTools.join(args, " "));
                    sendLog(sender, "Unknown " + Template.name + " command: " + label + " " + TTools.join(args, " "));
                }
            }
        }
        return handled;
    }

    // Simplifies and shortens the if statements for commands.
    private boolean is(String entered, String label) {
        return entered.equalsIgnoreCase(label);
    }

    // Checks if the current user is actually a player.
    private boolean isPlayer(CommandSender sender) {
        return sender != null && sender instanceof Player;
    }

    // Checks if the current user is actually a player and sends a message to that player.
    private boolean sendMessage(CommandSender sender, String message) {
        boolean sent = false;
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            player.sendMessage(message);
            sent = true;
        }
        return sent;
    }

    private boolean sendLog(CommandSender sender, String message) {
        boolean sent = false;
        if (!isPlayer(sender)) {
            TLogger.info(message);
            sent = true;
        }
        return sent;
    }

    // Checks if the current user is actually a player and returns the name of that player.
    private String getName(CommandSender sender) {
        String name = "";
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            name = player.getName();
        }
        return name;
    }

    // Gets the player if the current user is actually a player.
    private Player getPlayer(CommandSender sender) {
        Player player = null;
        if (isPlayer(sender)) {
            player = (Player) sender;
        }
        return player;
    }

    private String colorizeText(String text, ChatColor color) {
        return color + text + ChatColor.WHITE;
    }
}
