package com.github.seaoftrees08.entools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Excavation {

    List<Block> selectedBlock = new ArrayList<>();
    int radius;

    //for Excavation
    public Excavation(Player p, Block b){
        radius = ToolSettings.getRadius(p);
        double pitch = p.getLocation().clone().getPitch();
        double yaw = p.getLocation().clone().getYaw();
        if(pitch < -60 || 45 < pitch){
            xzSelect(b);
        }else if(Math.abs(Math.sin(Math.toRadians(yaw))) < Math.sin(Math.toRadians(45))){
            xySelect(b);
        }else{
            yzSelect(b);
        }
        work(p);
    }

    //for MineAll
    public Excavation(Block b, Player p){
        Location l = b.getLocation().clone();
        select(l, l.clone().add(20,20,20), l.clone().add(-20,-20,-20), b.getType());
        work(p);
    }

    //for Plow(耕す)
    public Excavation(Player p, Block b, int radius){
        this.radius = radius;
        xzSelect(b);
        plow(p);
    }

    private void xzSelect(Block b){
        int r = radius >> 1;

        Location l1 = b.getLocation().clone().add(-r, 0, -r);
        Location l2 = b.getLocation().clone().add(r, 0, r);
        Location l = b.getLocation().clone();
        select(l, l1, l2, b.getType());

    }

    private void xySelect(Block b){
        int r = radius >> 1;

        Location l1 = b.getLocation().clone().add(-r, -r, 0);
        Location l2 = b.getLocation().clone().add(r, r, 0);
        Location l = b.getLocation().clone();
        select(l, l1, l2, b.getType());
    }

    private void yzSelect(Block b){
        int r = radius >> 1;

        Location l1 = b.getLocation().clone().add(0, -r, -r);
        Location l2 = b.getLocation().clone().add(0, r, r);
        Location l = b.getLocation().clone();
        select(l, l1, l2, b.getType());
    }

    private void select(Location l, Location l1, Location l2, Material type){
        if(!selectedBlock.isEmpty() && selectedBlock.contains(l.getBlock())) return;
        if(!inArea(l, l1, l2)) return;
        if(!l.getBlock().getType().equals(type)) return;

        selectedBlock.add(l.getBlock());
        select(l.clone().add(1,0,0), l1, l2, type);
        select(l.clone().add(-1,0,0), l1, l2, type);
        select(l.clone().add(0,1,0), l1, l2, type);
        select(l.clone().add(0,-1,0), l1, l2, type);
        select(l.clone().add(0,0,1), l1, l2, type);
        select(l.clone().add(0,0,-1), l1, l2, type);
    }


    private boolean inArea(Location arg, Location l1, Location l2){
        boolean x = (l1.getX()<=arg.getX() && arg.getX()<=l2.getX()) || (l2.getX()<=arg.getX() && arg.getX()<=l1.getX());
        boolean y = (l1.getY()<=arg.getY() && arg.getY()<=l2.getY()) || (l2.getY()<=arg.getY() && arg.getY()<=l1.getY());
        boolean z = (l1.getZ()<=arg.getZ() && arg.getZ()<=l2.getZ()) || (l2.getZ()<=arg.getZ() && arg.getZ()<=l1.getZ());
        return x && y && z;
    }

    private void work(Player p){
        ItemStack tool = p.getInventory().getItemInMainHand();
        consumption(p, selectedBlock.size());

        new BukkitRunnable(){
            @Override
            public void run() {
                for(Block b : selectedBlock) b.breakNaturally(tool);
            }
        }.run();
    }

    private void plow(Player p){
        consumption(p, selectedBlock.size());

        new BukkitRunnable(){
            @Override
            public void run() {
                for(Block b : selectedBlock) if(b.getType().equals(Material.GRASS_BLOCK) || b.getType().equals(Material.DIRT)) b.setType(Material.FARMLAND);
            }
        }.run();
    }

    private void consumption(Player p, int value){
        ItemStack tool = p.getInventory().getItemInMainHand();
        int level = tool.getEnchantmentLevel(Enchantment.DURABILITY);
        //cf https://minecraft-ja.gamepedia.com/%E8%80%90%E4%B9%85%E5%8A%9B
        double decreaseProbability = (60.0+(40.0/(level+1.0))) / 100.0;

        short decrease = (short)(durability(tool) + (short)(value*decreaseProbability));
        if(tool.getType().getMaxDurability() == durability(tool)){
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 100, 1);
            p.spawnParticle(Particle.ITEM_CRACK, p.getLocation(), 40, tool);
            p.getInventory().setItemInMainHand(null);
            return;
        }else if(tool.getType().getMaxDurability() < decrease){
            decrease = tool.getType().getMaxDurability();
        }
        setDurability(tool,decrease);
    }

    private short durability(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        return meta==null ? 0 : (short)((Damageable)meta).getDamage();
    }

    private void setDurability(ItemStack item, short durability){
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            ((Damageable) meta).setDamage(durability);
            item.setItemMeta(meta);
        }
    }

}
