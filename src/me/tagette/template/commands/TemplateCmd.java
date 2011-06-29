package me.tagette.template.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import me.tagette.template.*;
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
    private CommandSender cSender;

    public TemplateCmd(Template instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        cSender = sender;
        boolean handled = false;
        DataManager dbm = TDatabase.dbm;
        // This is when the user types only the command label.
        // For example '/template'.
        if (args == null || args.length == 0) {
            handled = true;

            // Will send only if the sender is a player.
            sendMessage("You are using " + colorizeText(plugin.name, ChatColor.GREEN)
                    + " version " + colorizeText(plugin.version, ChatColor.GREEN) + ".");

            // Will send to the console only if the sender is NOT a player.
            sendLog(plugin.name + " version " + plugin.version + ".");
        } // This checks and runs the default commands. If the command is not a 
        //     default command then it continues to your commands.
        // There are 3 default commands: debug, reload and help.
        else if (!defaultCommand(command, label, args)) {

            // Put your commands in here

            if (is(args[0], "getawesome")) {
                handled = true;
                if ((isPlayer() && TPermissions.has(getPlayer(), "template.awesomeness", getPlayer().isOp()))
                        || !isPlayer()) {
                    if (args.length == 1 && isPlayer()) {
                        String name = getName();
                        int awesomeness = 0;
                        try {
                            ResultSet rs = dbm.query("SELECT awesomeness FROM players WHERE name = '" + name + "'");
                            if (rs != null && rs.next()) {
                                awesomeness = rs.getInt("awesomeness");
                            }
                        } catch (SQLException se) {
                        }
                        if (awesomeness > 0) {
                            sendMessage("Your awesomeness is " + awesomeness + "!");
                        } else {
                            sendMessage("You do not have awesomeness.");
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
                                sendMessage(name + "'s awesomeness is " + awesomeness + "!");
                                sendLog(name + "'s awesomeness is " + awesomeness + "!");
                            } else {
                                sendMessage(name + " does not have awesomeness.");
                                sendLog(name + " does not have awesomeness.");
                            }
                        }
                    }
                } else {
                    sendMessage(TLanguage.getLanguage("noPermissions"));
                }
            } else if (is(args[0], "setawesome")) {
                handled = true;
                if ((isPlayer() && TPermissions.has(getPlayer(), "template.awesomeness.set", getPlayer().isOp()))
                        || !isPlayer()) {
                    if (args.length == 2 && isPlayer()) {
                        String name = getName();
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
                                sendMessage("Your awesomeness is now " + newAwesome + "!");
                            } else {
                                sendMessage("Awesomeness can only be from "
                                        + TSettings.lowestAwesome + " to "
                                        + TSettings.highestAwesome + ".");
                            }
                        } else {
                            sendMessage(args[1] + " is not an awesome level.");
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
                                    sendMessage(name + "'s awesomeness is now " + newAwesome + "!");
                                    sendLog(name + "'s awesomeness is now " + newAwesome + "!");
                                } catch (SQLException se) {
                                    sendMessage("Could not set " + name + "'s awesomeness.");
                                    sendLog("Could not set " + name + "'s awesomeness.");
                                }
                            } else {
                                sendMessage("Awesomeness can only be from "
                                        + TSettings.lowestAwesome + " to "
                                        + TSettings.highestAwesome + ".");
                                sendLog("Awesomeness can only be from "
                                        + TSettings.lowestAwesome + " to "
                                        + TSettings.highestAwesome + ".");
                            }
                        } else {
                            sendMessage(args[2] + " is not an awesome level.");
                            sendLog(args[2] + " is not an awesome level.");
                        }
                    }
                } else {
                    sendMessage(TLanguage.getLanguage("noPermissions"));
                }

            } else {
                handled = true;
                sendMessage("Unknown " + plugin.name + " command: /" + label + " " + TTools.join(args, " "));
                sendLog("Unknown " + plugin.name + " command: " + label + " " + TTools.join(args, " "));
                if (isPlayer()) {
                    plugin.debug(getName() + " tried to enter an incorrect command: " + label + " " + TTools.join(args, " "));
                }
            }
        } else {
            handled = true;
        }
        return handled;
    }

    // Simplifies and shortens the if statements for commands.
    private boolean is(String entered, String label) {
        return entered.equalsIgnoreCase(label);
    }

    // Checks if the current user is actually a player.
    private boolean isPlayer() {
        return cSender != null && cSender instanceof Player;
    }

    // Checks if the current user is actually a player and sends a message to that player.
    private boolean sendMessage(String message) {
        boolean sent = false;
        if (isPlayer()) {
            getPlayer().sendMessage(message);
            sent = true;
        }
        return sent;
    }

    private boolean sendLog(String message) {
        boolean sent = false;
        if (!isPlayer()) {
            TLogger.info(message);
            sent = true;
        }
        return sent;
    }

    // Checks if the current user is actually a player and returns the name of that player.
    private String getName() {
        String name = "";
        if (isPlayer()) {
            name = getPlayer().getName();
        }
        return name;
    }

    // Gets the player if the current user is actually a player.
    private Player getPlayer() {
        Player player = null;
        if (isPlayer()) {
            player = (Player) cSender;
        }
        return player;
    }

    private String colorizeText(String text, ChatColor color) {
        return color + text + ChatColor.WHITE;
    }

    // Generic commands for the plugin.
    private boolean defaultCommand(Command command, String label, String[] args) {
        boolean isDefault = false;
        if (is(args[0], "debug")) {
            isDefault = true;
            if (isPlayer()) {
                if (TPermissions.isAdmin(getPlayer())) {
                    // Toggles debug mode for player.
                    if (plugin.isDebugging(getPlayer())) {
                        plugin.stopDebugging(getPlayer());
                        sendMessage("You have exited debug mode for " + colorizeText(plugin.name, ChatColor.GREEN) + ".");
                    } else {
                        if (plugin.startDebugging(getPlayer())) {
                            sendMessage("You have entered debug mode for " + colorizeText(plugin.name, ChatColor.GREEN) + ".");
                        }
                    }
                    if (plugin.inDebugMode()) {
                        TLogger.info("Debug mode initiated.");
                        sendMessage(colorizeText(plugin.name, ChatColor.GREEN) + " has begun debugging.");
                    } else {
                        TLogger.info("Debug mode terminated.");
                        sendMessage(colorizeText(plugin.name, ChatColor.GREEN) + " has stopped debugging.");
                    }
                } else {
                    sendMessage(TLanguage.getLanguage("noPermissions"));
                }
            } else {
                if (plugin.inDebugMode()) {
                    plugin.stopDebugging("You have exited debug mode for " + colorizeText(plugin.name, ChatColor.GREEN) + ".");
                    TLogger.info("Debug mode terminated.");
                } else {
                    plugin.startDebugging();
                    TLogger.info("Debug mode initiated.");
                }
            }
        } else if (is(args[0], "reload")) {
            isDefault = true;
            if (isPlayer()) {
                if (TPermissions.isAdmin(getPlayer())) {
                    TSettings.load();
                    TLanguage.load();
                    sendMessage(colorizeText(plugin.name, ChatColor.GREEN) + " has been reloaded.");
                    TLogger.info("Config files reloaded.");
                } else {
                    sendMessage(TLanguage.getLanguage("noPermissions"));
                }
            } else {
                TSettings.load();
                TLanguage.load();
                TLogger.info("Config files reloaded.");
            }
        } else if (is(args[0], "help") || is(args[0], "?")) {
            isDefault = true;
            sendMessage(THelp.getReadableHelp(getPlayer()));
            sendLog(THelp.getReadableHelp());
        }
        return isDefault;
    }
}
