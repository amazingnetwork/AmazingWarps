package br.com.amazingnetwork.listeners;
import br.com.amazingnetwork.entities.Warp;

import br.com.amazingnetwork.managers.WarpManager;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Collection;

public class PlayerListener implements Listener {

    private final WarpManager warpManager;

    public PlayerListener(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @EventHandler
    public void onPlayerCommandExecute(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        Collection<Warp> warps = warpManager.getWarps();
        String cmd = e.getMessage();

        if (cmd.contains(" "))
            cmd = cmd.split(" ")[0];

        String warpName = cmd.replaceFirst("/", "");

        String finalCmd = "/warp " + warpName;

        warps.forEach(warp -> {
            if (warp.getName().equalsIgnoreCase(warpName)) {
                e.setCancelled(true);
                p.chat(finalCmd);
            }
        });
    }
}
