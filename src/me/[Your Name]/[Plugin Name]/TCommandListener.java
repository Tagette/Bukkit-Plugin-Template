package me.<Your Name>.<Plugin Name>;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @description Handles player commands
 * @author <Your Name>
 */
public class TCommandListener implements CommandExecutor {

    private final <Plugin Name> plugin;

    public TCommandListener(<Plugin Name> instance) {
        plugin = instance;
    }

    public void initialize(TCommandListener instance) {
        plugin.getCommand("<Plugin Name>").setExecutor(instance);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean handled = false;
        if (is(label, <Plugin Name>.name)) {
            if (args == null || args.length == 0) {
                handled = true;
                sendMessage(sender, "You are using " + <Plugin Name>.name + " version " + <Plugin Name>.version + ".");
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
                    if (isPlayer(sender) && TPermissions.permission(getPlayer(sender), "<Plugin Name>.awesomeness", getPlayer(sender).isOp())) {
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
                    if (isPlayer(sender) && TPermissions.permission(getPlayer(sender), "<Plugin Name>.awesomeness.set", getPlayer(sender).isOp())) {
                        if (args.length == 2) {
                            String name = getName(sender);
                            if (TTools.isInt(args[1])) {
                                int newAwesome = Integer.parseInt(args[1]);
                                TDatabase.dbm.setIntByName("awesomeness", name, newAwesome);
                            } else {
                                sendMessage(sender, args[1] + " is not an awesome level.");
                            }
                        } else if (args.length == 3) {
                            String name = args[1];
                            if (TTools.isInt(args[2])) {
                                int newAwesome = Integer.parseInt(args[2]);
                                TDatabase.dbm.setIntByName("awesomeness", name, newAwesome);
                                sendMessage(sender, args[2] + " is not an awesome level.");
                            } else {
                                sendMessage(sender, args[2] + " is not an awesome level.");
                            }
                        }
                    } else {
                        sendMessage(sender, "You do not have permission.");
                    }

                } else if (is(args[0], "help") || is(args[0], "?")) {
                    sendMessage(sender, "No help yet.");
                } else {
                    handled = true;
                    sendMessage(sender, "Unknown " + <Plugin Name>.name + " command " + label + ".");
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
}
