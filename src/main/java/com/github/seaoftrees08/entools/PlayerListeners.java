package com.github.seaoftrees08.entools;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerListeners implements Listener {

    public PlayerListeners(EnhancedTools entool){
        entool.getServer().getPluginManager().registerEvents(this, entool);
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e){

        if(ToolSettings.canMineall(e.getPlayer().getInventory().getItemInMainHand().getType())
                && ToolSettings.getMineall(e.getPlayer()) && ToolSettings.breakBlock(e.getBlock().getType())){
            e.setCancelled(true);
            new Excavation(e.getBlock(), e.getPlayer());

        }else if(ToolSettings.canEnTool(e.getPlayer().getInventory().getItemInMainHand().getType())
                && ToolSettings.getRadius(e.getPlayer()) > 1){
            e.setCancelled(true);
            new Excavation(e.getPlayer(), e.getBlock());
        }


    }

}
