package br.com.amazingnetwork.commands;

import br.com.amazingnetwork.AmazingWarps;
import br.com.amazingnetwork.entities.Warp;
import br.com.amazingnetwork.managers.WarpManager;
import br.com.amazingnetwork.managers.YamlFileManager;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WarpCommand implements CommandExecutor {

    private final YamlFileManager config;
    private final YamlFileManager messages;

    private final WarpManager warpManager;

    public WarpCommand(YamlFileManager config, YamlFileManager messages, WarpManager warpManager) {
        this.config = config;
        this.messages = messages;
        this.warpManager = warpManager;
    }

    public boolean playerHasPermission(Player p) {
        if (!p.hasPermission("warp.admin")) {
            p.sendMessage(messages.getFile().getString("noPermissions"));
            return false;
        }

        return true;
    }

    public List<String> getHelp() {
        return Arrays.asList(
            "",
            "§eAmazingWarps v1.0",
            "§fProvided by §eAmazing Network",
            "",
            " §f● §e/warp -h, -help §fLista os comandos do plugin",
            " §f● §e/warp list §fLista todos as warps",
            " §f● §e/warp set <name> §fSeta uma warp",
            " §f● §e/warp (del|delete|remove) <name> §fRemove uma warp",
            " §f● §e/warp <name> §fTeleporta o player a uma warp",
            " §f● §e/warp reload §fRecarrega as configurações do plugin",
            ""
        );
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player p = (Player) sender;
        Collection<Warp> warps = warpManager.getWarps();

        if (args.length == 0) {
            Inventory inv = Bukkit.createInventory(null, 6*9, config.getFile().getString("inventoryName"));

            for (Warp warp : warps) {
                if (warp.getIconSlot() > (6 * 9) - 1 || warp.getIconSlot() < 0)
                    continue;

                if (!warp.getPermission().isEmpty() && !p.hasPermission(warp.getPermission()))
                    continue;

                inv.setItem(warp.getIconSlot(), warp.getIcon());
            }

            p.openInventory(inv);

            return true;
        }

        String cmd = args[0];

        switch (cmd) {
            case "-h":
            case "-help": {
                if (!(p.hasPermission("warp.admin")))
                    p.sendMessage(messages.getFile().getString("onWarpNoArguments"));
                else
                    getHelp().forEach(p::sendMessage);

                break;
            }
            case "set": {
                if (!(playerHasPermission(p)))
                    break;

                if (args.length == 1) {
                    p.sendMessage(messages.getFile().getString("setWarpNoArguments"));
                    break;
                }

                String name = args[1];

                warpManager.store(name.toLowerCase(), p.getLocation());

                p.sendMessage(messages.getFile().getString("setWarp").replace("%warp%", name));
                break;
            }
            case "del":
            case "delete":
            case "remove": {
                if (!(playerHasPermission(p)))
                    break;

                if (args.length == 1) {
                    p.sendMessage(messages.getFile().getString("delWarpNoArguments"));
                    break;
                }

                String name = args[1];
                warpManager.destroy(name.toLowerCase());

                p.sendMessage(messages.getFile().getString("delWarp").replace("%warp%", name));
                break;
            }
            case "list": {
                if (warpManager.getWarpsName().isEmpty()) {
                    p.sendMessage(messages.getFile().getString("warpListEmpty"));
                    break;
                }

                String warpsString = messages.getFile().getString("warpList");
                String messageListWarps = warpsString.replace("%warps%", warpManager.getWarpsName().toString().replace("[", "").replace("]", ""));

                p.sendMessage(messageListWarps);
                break;
            }
            case "reload": {
                if (!(playerHasPermission(p)))
                    break;

                AmazingWarps.getInstance().reload();

                p.sendMessage(messages.getFile().getString("configReload"));
                break;
            }
            default: {
                if (warpManager.getWarp().containsKey(cmd)) {
                    Warp warp = warpManager.getWarp().get(cmd);

                    if (!warp.getPermission().isEmpty() && !p.hasPermission(warp.getPermission())) {
                        p.sendMessage(messages.getFile().getString("onWarpNoPermission"));

                        break;
                    }

                    p.teleport(warp.getLocation());
                    p.sendMessage(messages.getFile().getString("onWarp").replace("%warp%", cmd));
                } else {
                    p.sendMessage(messages.getFile().getString("warpNotExists").replace("%warp%", cmd));
                }

                break;
            }
        }

        return true;
    }
}
