package br.com.amazingnetwork.entities;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Warp {
    private String name;
    private Location location;
    private int iconSlot;
    private ItemStack icon;
    private String permission;

    public Warp(String name, Location location, int iconSlot, ItemStack icon, String permission) {
        this.name = name;
        this.location = location;
        this.setIconSlot(iconSlot);
        this.icon = icon;
        this.permission = permission;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public Location getLocation() {
        return location;
    }

    public void setIconSlot(int iconSlot) {
        this.iconSlot = iconSlot;
    }
    public int getIconSlot() {
        return iconSlot;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }
    public ItemStack getIcon() {
        return icon;
    }

    public void setPermission(String permission) { this.permission = permission; }
    public String getPermission() { return permission; }
}
