package com.github.seaoftrees08.entools;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ToolSettings {

    //掘削半径1で削除、奇数のみ設定可能
    public static void setRadius(int radius, Player p){
        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if(radius!=1){
            pdc.set(getNamespacedKey(), PersistentDataType.INTEGER, radius);
        }else{
            pdc.remove(getNamespacedKey());
        }

        p.getInventory().getItemInMainHand().setItemMeta(meta);
        setLore(p);
    }

    public static int getRadius(Player p){
        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();

        int r = meta.getPersistentDataContainer()
                .getOrDefault(getNamespacedKey(), PersistentDataType.INTEGER, 1);

        return r;
    }

    public static boolean canEnTool(Material m){
        return m.name().contains("PICKAXE") || m.name().contains("SHOVEL") || m.name().contains("AXE");
    }

    public static void setMineall(Player p){
        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if(!getMineall(p)){
            pdc.set(getMineallKey(), PersistentDataType.INTEGER, 1);
        }else{
            pdc.remove(getMineallKey());
        }

        p.getInventory().getItemInMainHand().setItemMeta(meta);
        setLore(p);
    }

    public static boolean getMineall(Player p){
        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
        int r = meta.getPersistentDataContainer()
                .getOrDefault(getMineallKey(), PersistentDataType.INTEGER, 0);
        return r==1;
    }

    public static boolean canMineall(Material m){
        return m.name().contains("PICKAXE");
    }

    public static boolean breakBlock(Material m){
        return m.equals(Material.COAL_ORE)
                || m.equals(Material.IRON_ORE)
                || m.equals(Material.LAPIS_ORE)
                || m.equals(Material.GOLD_ORE)
                || m.equals(Material.NETHER_GOLD_ORE)
                || m.equals(Material.DIAMOND_ORE)
                || m.equals(Material.REDSTONE_ORE)
                || m.equals(Material.EMERALD_ORE)
                || m.equals(Material.GLOWSTONE)
                || m.equals(Material.NETHER_QUARTZ_ORE);
    }

    private static void setLore(Player p){
        int r = getRadius(p);
        boolean mineall = getMineall(p);
        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
        if(r>1){
            if(mineall){
                meta.setLore(Arrays.asList("掘削半径: "+r, "Mineall: 有効"));
            }else{
                meta.setLore(Arrays.asList("掘削半径: "+r));
            }
        }else{
            if(mineall){
                meta.setLore(Arrays.asList("MineAll: 有効"));
            }else{
                meta.setLore(null);
            }
        }
        p.getInventory().getItemInMainHand().setItemMeta(meta);
    }

    private static NamespacedKey getNamespacedKey(){
        return new NamespacedKey(EnhancedTools.inst(), "enhancedtools");
    }
    private static NamespacedKey getMineallKey(){
        return new NamespacedKey(EnhancedTools.inst(), "enhancedtools_mineall");
    }
}
