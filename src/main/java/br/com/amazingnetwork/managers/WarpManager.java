package br.com.amazingnetwork.managers;

import br.com.amazingnetwork.entities.Warp;
import br.com.amazingnetwork.utils.ItemUtils;
import br.com.amazingnetwork.utils.LocationUtils;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class WarpManager {
    YamlFileManager warpFile;

    public WarpManager(YamlFileManager warpFil) {
        this.warpFile = warpFil;
    }

    private static final Map<String, Warp> warps = new HashMap<>();

    public void store(String name, Location location) {
        String serializeLoc = LocationUtils.serialize(location);

        if (warpFile.getFile().getString("warps." + name) != null) {
            warpFile.setPropertyFile("warps." + name + ".location", serializeLoc);
            warpFile.saveFile();

            loadWarps();
        } else {
            List<Integer> slots = new ArrayList<>();
            AtomicInteger slotIcon = new AtomicInteger(0);

            for (Entry<String, Warp> warp : warps.entrySet()) {
                slots.add(warp.getValue().getIconSlot());
            }

            Collections.sort(slots);

            for (Integer slot : slots) {
                if (slot == slotIcon.intValue())
                    slotIcon.getAndIncrement();
                else
                    break;
            }

            warpFile.setPropertyFile("warps." + name , name );
            warpFile.setPropertyFile("warps." + name + ".name", name);
            warpFile.setPropertyFile("warps." + name + ".location", serializeLoc);
            warpFile.setPropertyFile("warps." + name + ".iconSlot", slotIcon.intValue());
            warpFile.setPropertyFile("warps." + name + ".icon.displayName", name);
            warpFile.setPropertyFile("warps." + name + ".icon.material", "DIAMOND");
            warpFile.setPropertyFile("warps." + name + ".icon.data", 0);
            warpFile.setPropertyFile("warps." + name + ".icon.lore", Collections.singletonList("§eClique para teleportar"));
            warpFile.setPropertyFile("warps." + name + ".permission", "");
            warpFile.saveFile();

            ItemStack icon = ItemUtils.build("DIAMOND", name, Collections.singletonList("§eClique para teleportar"), 0);
            Location deserializeLoc = LocationUtils.deserialize(serializeLoc);

            Warp warp = new Warp(name, deserializeLoc, slotIcon.intValue(), icon, "");
            warps.put(name, warp);
        }
    }

    public void destroy(String name) {
        warpFile.setPropertyFile("warps." + name, null);
        warpFile.saveFile();

        warps.remove(name);
    }

    public void loadWarps() {
        warps.clear();

        if (warpFile.getFile().getString("warps") != null) {
            Set<String> warpsSet = warpFile.getFile().getConfigurationSection("warps").getKeys(false);

            warpsSet.forEach(warpSet -> {
                String location = warpFile.getFile().getString("warps." + warpSet + ".location");
                int iconSlot = warpFile.getFile().getInt("warps." + warpSet + ".iconSlot");
                String displayName = warpFile.getFile().getString("warps." + warpSet + ".icon.displayName");
                String material = warpFile.getFile().getString("warps." + warpSet + ".icon.material");
                int data = warpFile.getFile().getInt("warps." + warpSet + ".icon.data");
                ArrayList<String> lore = (ArrayList<String>) warpFile.getFile().getStringList("warps." + warpSet + ".icon.lore");
                String permission = warpFile.getFile().getString("warps." + warpSet + ".permission", "");

                Location deserializeLoc = LocationUtils.deserialize(location);
                ItemStack icon = ItemUtils.build(material, displayName, lore, data);

                Warp warp = new Warp(warpSet, deserializeLoc, iconSlot, icon, permission);

                warps.put(warpSet, warp);
            });
        }
    }

    public Map<String, Warp> getWarp() {
        return warps;
    }

    public Collection<Warp> getWarps() {
        return warps.values();
    }

    public List<String> getWarpsName() {
        List<String> warps = new ArrayList<>();

        for (Warp warp : getWarps()) {
            warps.add(warp.getName());
        }

        return warps;
    }
}
