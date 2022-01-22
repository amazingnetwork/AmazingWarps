package br.com.amazingnetwork.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtils {
    public static ItemStack build(String material, String displayName, List<String> lore, int data) {
        if (Material.getMaterial(material) == null)
            return new ItemStack(Material.DIAMOND, 1);

        ItemStack icon = new ItemStack(Material.getMaterial(material), 1, (short) data);
        ItemMeta itemMeta = icon.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        icon.setItemMeta(itemMeta);

        return icon;
    }
}
