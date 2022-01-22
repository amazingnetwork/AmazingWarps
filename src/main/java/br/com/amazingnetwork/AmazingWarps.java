package br.com.amazingnetwork;

import br.com.amazingnetwork.listeners.InventoryListener;
import br.com.amazingnetwork.listeners.PlayerListener;
import br.com.amazingnetwork.managers.WarpManager;
import br.com.amazingnetwork.managers.YamlFileManager;
import br.com.amazingnetwork.commands.WarpCommand;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AmazingWarps extends JavaPlugin {

    private YamlFileManager configYml;
    private YamlFileManager messagesYml;
    private YamlFileManager warpsYml;

    private WarpManager warpManager;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§6[Amazing Warps] §7carregado com sucesso!");

        configYml = new YamlFileManager(null, "config", false);
        messagesYml = new YamlFileManager(null, "messages", false);
        warpsYml = new YamlFileManager(null, "warps", false);

        configYml.saveFile();
        messagesYml.saveFile();
        warpsYml.saveFile();

        warpManager = new WarpManager(warpsYml);
        warpManager.loadWarps();

        registerListeners();
        registerCommands();
    }

    public void reload() {
        configYml.reloadFile();
        messagesYml.reloadFile();
        warpsYml.reloadFile();

        warpManager.loadWarps();
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(warpManager), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(configYml, warpManager), this);
    }

    public void registerCommands() {
        getCommand("warp").setExecutor(new WarpCommand(configYml, messagesYml, warpManager));
    }

    public static AmazingWarps getInstance() {
        return getPlugin(AmazingWarps.class);
    }
}
