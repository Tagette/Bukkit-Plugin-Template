package me.tagette.template;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

/**
 * @description Handles all player related events
 * @author Tagette
 */
public class TPlayerListener extends PlayerListener {

    private final Template plugin;

    public TPlayerListener(Template instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            Player player = event.getPlayer();
            plugin.debug(player.getName() + " right-clicked a " + block.getType().name() + " block. (" + block.getX() + ", " + block.getY() + ", " + block.getZ() + ")");
        }
    }
//    public void onPlayerJoin(PlayerJoinEvent event) {
//    }
//
//    public void onPlayerQuit(PlayerQuitEvent event) {
//    }
//
//    public void onPlayerKick(PlayerKickEvent event) {
//    }
//
//    public void onPlayerChat(PlayerChatEvent event) {
//    }
//
//    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
//    }
//
//    public void onPlayerMove(PlayerMoveEvent event) {
//    }
//
//    public void onPlayerTeleport(PlayerTeleportEvent event) {
//    }
//
//    public void onPlayerRespawn(PlayerRespawnEvent event) {
//    }
//
//    public void onPlayerInteract(PlayerInteractEvent event) {
//    }
//
//    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
//    }
//
//    public void onPlayerLogin(PlayerLoginEvent event) {
//    }
//
//    public void onPlayerPreLogin(PlayerPreLoginEvent event) {
//    }
//
//    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
//    }
//
//    public void onPlayerAnimation(PlayerAnimationEvent event) {
//    }
//
//    public void onInventoryOpen(PlayerInventoryEvent event) {
//    }
//
//    public void onItemHeldChange(PlayerItemHeldEvent event) {
//    }
//
//    public void onPlayerDropItem(PlayerDropItemEvent event) {
//    }
//
//    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
//    }
//
//    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
//    }
//
//    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
//    }
//
//    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
//    }
//
//    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
//    }
//
//    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
//    }
}
