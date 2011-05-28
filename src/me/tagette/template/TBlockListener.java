package me.tagette.template;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * @decsription Handles all block related events
 * @author Tagette
 */
public class TBlockListener extends BlockListener {

    private final Template plugin;

    public TBlockListener(final Template plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
    }
//    
//    public void onBlockDamage(BlockDamageEvent event) {
//    }
//
//    public void onBlockCanBuild(BlockCanBuildEvent event) {
//    }
//
//    public void onBlockFromTo(BlockFromToEvent event) {
//    }
//    
//    public void onBlockFlow(BlockFromToEvent event) {
//    }
//
//    public void onBlockIgnite(BlockIgniteEvent event) {
//    }
//
//    public void onBlockPhysics(BlockPhysicsEvent event) {
//    }
//
//    public void onBlockPlace(BlockPlaceEvent event) {
//    }
//
//    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
//    }
//
//    public void onLeavesDecay(LeavesDecayEvent event) {
//    }
//
//    public void onSignChange(SignChangeEvent event) {
//    }
//
//    public void onBlockBurn(BlockBurnEvent event) {
//    }
//
//    public void onBlockBreak(BlockBreakEvent event) {
//    }
//
//    public void onSnowForm(SnowFormEvent event) {
//    }
//
//    public void onBlockDispense(BlockDispenseEvent event) {
//    }
}
