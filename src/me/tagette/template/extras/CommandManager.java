package me.tagette.template.extras;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 *
 * @author brenda
 */
public class CommandManager {

    private Plugin plugin;
    private Map<String, CommandExecutor> commands = new Hashtable<String, CommandExecutor>();

    public CommandManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void loadFromDescription(PluginDescriptionFile desc, ClassLoader loader) {
        Object object = desc.getCommands();
        if (object == null) {
            return;
        }

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) object;

        for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
            String label = entry.getKey();
            String classname = entry.getValue().get("class").toString();
            try {
                Class<?> klass = Class.forName(classname, true, loader);
                Class<? extends CommandExecutor> commandClass = klass.asSubclass(CommandExecutor.class);
                CommandExecutor ce = commandClass.getConstructor(CommandExecutor.class).newInstance(plugin);
                addCommand(label, ce);
            } catch (NoSuchMethodException e) {
                System.out.println("No constructor that accepts a Plugin.");
            } catch (InstantiationException e) {
                System.out.println("Error while creating a Commandable object.");
            } catch (IllegalAccessException e) {
                System.out.println("Illegal access to the Commandable object constructor.");
            } catch (InvocationTargetException e) {
                System.out.println(e.getCause().getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("Unable to load command class for command /" + label);
            }
        }
    }

    public void addCommand(String label, CommandExecutor executor) {
        commands.put(label, executor);
    }

    public boolean dispatch(CommandSender sender, Command command, String label, String[] args) {
        if (!commands.containsKey(label)) {
            return false;
        }

        boolean handled = true;

        CommandExecutor ce = commands.get(label);
        handled = ce.onCommand(sender, command, label, args);

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
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            name = player.getName();
        }
        return name;
    }

    // Checks if the current user is actually a player.
    private boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

    // Gets the player if the current user is actually a player.
    private Player getPlayer(CommandSender sender) {
        Player player = null;
        if (isPlayer(sender)) {
            player = (Player) sender;
        }
        return player;
    }
}