package org.awayxd.modmode;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.awayxd.modmode.commands.ModModeCommand;
import org.awayxd.modmode.commands.FreezeCommand;
import org.awayxd.modmode.commands.UnfreezeCommand;
import org.awayxd.modmode.listeners.PlayerInteractListener;
import org.awayxd.modmode.listeners.PlayerMoveListener;
import org.awayxd.modmode.listeners.PlayerDropPickupListener;
import org.awayxd.modmode.commands.InvSeeCommand;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ModModePlugin extends JavaPlugin {

    private static ModModePlugin instance;
    private PlayerInteractListener interactListener;
    private final Set<UUID> modModePlayers = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;
        interactListener = new PlayerInteractListener();

        if (getCommand("modmode") == null || getCommand("freeze") == null || getCommand("unfreeze") == null) {
            getLogger().severe("Commands are not registered in plugin.yml");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getCommand("modmode").setExecutor(new ModModeCommand());
        this.getCommand("freeze").setExecutor(new FreezeCommand());
        this.getCommand("unfreeze").setExecutor(new UnfreezeCommand());

        getServer().getPluginManager().registerEvents(interactListener, this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropPickupListener(), this);

        getLogger().info("ModModePlugin has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("ModModePlugin has been disabled.");
    }

    public static ModModePlugin getInstance() {
        return instance;
    }

    public PlayerInteractListener getInteractListener() {
        return interactListener;
    }

    public Set<UUID> getModModePlayers() {
        return modModePlayers;
    }
}
