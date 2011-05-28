package me.tagette.template.commands;

import me.tagette.template.TDatabase;
import me.tagette.template.TLanguage;
import me.tagette.template.TPermissions;
import me.tagette.template.TSettings;
import me.tagette.template.TTools;
import me.tagette.template.Template;
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
    
    public TemplateCmd(Template instance){
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean handled = false;
        if (is(label, "template")) {
            if (args == null || args.length == 0) {
                handled = true;
                sendMessage(sender, "You are using " + colorizeText(Template.name, ChatColor.GREEN)
                        + " version " + colorizeText(Template.version, ChatColor.GREEN) + ".");
            } else {
                // Put your commands in here
                if (is(args[0], "nuke")) {
                    handled = true;
                    if (isPlayer(sender) && TPermissions.isAdmin(getPlayer(sender))) {
                        if (TSettings.adminsObeyLimits) {
                            sendMessage(sender, TLanguage.getLanguage("nukeAdminWithLimits") + " (" + TSettings.maxNukes + " nukes total)");
                        } else {
                            sendMessage(sender, TLanguage.getLanguage("nukeAdminWithoutLimits"));
                        }
                    } else {
                        sendMessage(sender, TLanguage.getLanguage("nukeNonAdmin") + " (" + TSettings.maxNukes + " nukes total)");
                    }
                } else if (is(args[0], "getawesome")) {
                    handled = true;
                    if (isPlayer(sender) && TPermissions.permission(getPlayer(sender), "template.awesomeness", getPlayer(sender).isOp())) {
                        if (args.length == 1) {
                            String name = getName(sender);
                            int awesomeness = TDatabase.dbm.getIntByName("awesomeness", name);
                            if (awesomeness > 0) {
                                sendMessage(sender, "Your awesomeness is " + awesomeness + "!");
                            } else {
                                sendMessage(sender, "You do not have awesomeness.");
                            }
                        } else if (args.length == 2) {
                            String name = args[1];
                            if (!name.isEmpty()) {
                                int awesomeness = TDatabase.dbm.getIntByName("awesomeness", name);
                                if (awesomeness > 0) {
                                    sendMessage(sender, name + "'s awesomeness is " + awesomeness + "!");
                                } else {
                                    sendMessage(sender, name + " does not have awesomeness.");
                                }
                            }
                        }
                    } else {
                        sendMessage(sender, "You do not have permissions.");
                    }
                } else if (is(args[0], "setawesome")) {
                    handled = true;
                    if (isPlayer(sender) && TPermissions.permission(getPlayer(sender), "template.awesomeness.set", getPlayer(sender).isOp())) {
                        if (args.length == 2) {
                            String name = getName(sender);
                            if (TTools.isInt(args[1])) {
                                int newAwesome = Integer.parseInt(args[1]);
                                TDatabase.dbm.setIntByName("awesomeness", name, newAwesome);
                                sendMessage(sender, "Your awesomeness is now " + newAwesome + "!");
                            } else {
                                sendMessage(sender, args[1] + " is not an awesome level.");
                            }
                        } else if (args.length == 3) {
                            String name = args[1];
                            if (TTools.isInt(args[2])) {
                                int newAwesome = Integer.parseInt(args[2]);
                                TDatabase.dbm.setIntByName("awesomeness", name, newAwesome);
                                sendMessage(sender, name + "'s awesomeness is now " + newAwesome + "!");
                            } else {
                                sendMessage(sender, args[2] + " is not an awesome level.");
                            }
                        }
                    } else {
                        sendMessage(sender, "You do not have permission.");
                    }

                } else if (is(args[0], "debug")) {
                    handled = true;
                    if(isPlayer(sender) && TPermissions.isAdmin(getPlayer(sender))) {
                        plugin.debugging = !plugin.debugging;
                        if(plugin.debugging)
                            sendMessage(sender, "Plugin has begun debugging.");
                        else
                            sendMessage(sender, "Plugin has stopped debugging.");
                    }
                }else if (is(args[0], "reload")) {
                    handled = true;
                    if(isPlayer(sender) && TPermissions.isAdmin(getPlayer(sender))) {
                        TSettings.load();
                        TLanguage.load();
                        sendMessage(sender, colorizeText(Template.name, ChatColor.GREEN) + " has been reloaded.");
                    }
                } else if (is(args[0], "help") || is(args[0], "?")) {
                    handled = true;
                    sendMessage(sender, "No help yet.");
                } else {
                    handled = true;
                    sendMessage(sender, "Unknown " + Template.name + " command: /" + label + " " + join(args, " "));
                }
            }
        }
        return handled;
    }

    // Simplifies and shortens the if statements for commands.
    private boolean is(String entered, String label) {
        return entered.equalsIgnoreCase(label);
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

    // Checks if the current user is actually a player and returns the name of that player.
    private String getName(CommandSender sender) {
        String name = "";
        if(isPlayer(sender)){
            Player player = (Player) sender;
            name = player.getName();
        }
        return name;
    }
    
    // Checks if the current user is actually a player.
    private boolean isPlayer(CommandSender sender){
        return sender instanceof Player;
    }
    
    // Gets the player if the current user is actually a player.
    private Player getPlayer(CommandSender sender){
        Player player = null;
        if(isPlayer(sender)){
            player = (Player) sender;
        }
        return player;
    }
    
    private String colorizeText(String text, ChatColor color){
        return color + text + ChatColor.WHITE;
    }
    
    private String join(String[] split, String delimiter) {
        String joined = "";
        for (String s : split) {
            joined += s + delimiter;
        }
        joined = joined.substring(0, joined.length() - (delimiter.length()));
        return joined;
    }
}
