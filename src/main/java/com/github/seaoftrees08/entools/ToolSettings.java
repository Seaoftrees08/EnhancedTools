package com.github.seaoftrees08.entools;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ToolSettings {

    //掘削半径1で削除、奇数のみ設定可能
    public static void setRadius(int radius, Player p){
        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();

        PersistentDataContainer pdc = meta != null ? meta.getPersistentDataContainer() : null;
        if(pdc == null){
            EnhancedTools.inst().getLogger().warning("[Enhanced Tools] ItemMeta is null. at setRadius()");
            return;
        }

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

        return Objects.requireNonNull(meta).getPersistentDataContainer()
                .getOrDefault(getNamespacedKey(), PersistentDataType.INTEGER, 1);
    }

    public static boolean canEnTool(Material m){
        return m.name().contains("PICKAXE") || m.name().contains("SHOVEL") || m.name().contains("AXE") || m.name().contains("HOE");
    }

    public static void setMineAll(Player p){
        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();

        PersistentDataContainer pdc = meta != null ? meta.getPersistentDataContainer() : null;
        if(pdc == null){
            EnhancedTools.inst().getLogger().warning("[Enhanced Tools] ItemMeta is null. at setMineAll()");
            return;
        }
        if(!getMineall(p)){
            pdc.set(getMineAllKey(), PersistentDataType.INTEGER, 1);
        }else{
            pdc.remove(getMineAllKey());
        }

        p.getInventory().getItemInMainHand().setItemMeta(meta);
        setLore(p);
    }

    public static boolean getMineall(Player p){
        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
        int r = 0;
        if (meta != null) {
            r = meta.getPersistentDataContainer()
                    .getOrDefault(getMineAllKey(), PersistentDataType.INTEGER, 0);
        }else{
            EnhancedTools.inst().getLogger().warning("[Enhanced Tools] ItemMeta is null. at getMineAll()");
        }
        return r==1;
    }

    public static boolean canMineall(Material m){
        return m.name().contains("PICKAXE");
    }

    public static boolean breakBlock(Material m){
        return m.name().contains(Material.COAL_ORE.name())
                || m.name().contains(Material.IRON_ORE.name())
                || m.name().contains(Material.COPPER_ORE.name())
                || m.name().contains(Material.LAPIS_ORE.name())
                || m.name().contains(Material.GOLD_ORE.name())
                || m.name().contains(Material.DIAMOND_ORE.name())
                || m.name().contains(Material.REDSTONE_ORE.name())
                || m.name().contains(Material.EMERALD.name())
                || m.name().contains(Material.GLOWSTONE.name())
                || m.name().contains(Material.NETHER_QUARTZ_ORE.name())
                || m.name().contains(Material.ANCIENT_DEBRIS.name());
    }

    private static void setLore(Player p){
        int r = getRadius(p);
        boolean mineall = getMineall(p);
        ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
        if(meta==null){
            EnhancedTools.inst().getLogger().warning("[Enhanced Tools] ItemMeta is null. at setLore()");
            return;
        }
        if(r>1){
            if(mineall) {
                meta.setLore(Arrays.asList("掘削半径: " + r, "Mineall: 有効"));
            }else if(p.getInventory().getItemInMainHand().getType().name().contains("HOE")){
                meta.setLore(List.of("農耕半径: " + r));
            }else{
                meta.setLore(List.of("掘削半径: " + r));
            }
        }else{
            if(mineall){
                meta.setLore(List.of("MineAll: 有効"));
            }else{
                meta.setLore(null);
            }
        }
        p.getInventory().getItemInMainHand().setItemMeta(meta);
    }

    private static NamespacedKey getNamespacedKey(){
        return new NamespacedKey(EnhancedTools.inst(), "enhancedtools");
    }
    private static NamespacedKey getMineAllKey(){
        return new NamespacedKey(EnhancedTools.inst(), "enhancedtools_mineall");
    }
}
