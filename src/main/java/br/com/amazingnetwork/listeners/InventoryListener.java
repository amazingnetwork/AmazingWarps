package br.com.amazingnetwork.listeners;

import br.com.amazingnetwork.entities.Warp;
import br.com.amazingnetwork.managers.WarpManager;
import br.com.amazingnetwork.managers.YamlFileManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.Sound;

public class InventoryListener implements Listener {

    private final YamlFileManager config;

    private final WarpManager warpManager;

    public InventoryListener(YamlFileManager config, WarpManager warpManager) {
        this.config = config;
        this.warpManager = warpManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)
            return;

        Player p = (Player) e.getWhoClicked();

        if (e.getInventory().getName().equalsIgnoreCase(config.getFile().getString("inventoryName"))) {
            e.setCancelled(true);

            Warp warp = warpManager
                    .getWarp()
                    .values()
                    .stream()
                    .filter(warpEl -> warpEl.getIconSlot() == e.getSlot())
                    .findFirst()
                    .orElse(null);

            if (warp == null)
                return;

            p.chat("/warp " + warp.getName().toLowerCase());
            p.closeInventory();
            p.playSound(p.getLocation(), Sound.CLICK, 1, 2f);
        }
    }
}
