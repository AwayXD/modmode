package org.awayxd.modmode;

import net.luckperms.api.LuckPerms;
import org.awayxd.modmode.commands.*;
import org.awayxd.modmode.listeners.JoinListener;
import org.awayxd.modmode.listeners.PlayerDropPickupListener;
import org.awayxd.modmode.listeners.PlayerInteractListener;
import org.awayxd.modmode.listeners.PlayerMoveListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ModModePlugin extends JavaPlugin {

    private static ModModePlugin instance;
    private PlayerInteractListener interactListener;
    private final Set<UUID> modModePlayers = new HashSet<>();

    private LuckPerms api;

    @Override
    public void onEnable() {
        instance = this;
        interactListener = new PlayerInteractListener();

        this.getCommand("mod").setExecutor(new ModModeCommand());
        this.getCommand("freeze").setExecutor(new FreezeCommand());
        this.getCommand("unfreeze").setExecutor(new UnfreezeCommand());
        this.getCommand("invsee").setExecutor(new InvseeCommand());
        this.getCommand("staffchat").setExecutor(new StaffChatCommand(this));
        this.getCommand("alts").setExecutor(new CheckAltsCommand(this));
        this.getCommand("smite").setExecutor(new SmiteCommand());

        // Register event listeners
        getServer().getPluginManager().registerEvents(interactListener, this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropPickupListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        getLogger().info("ModModePlugin has been enabled!");

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("ModModePlugin has been disabled.");
    }

    public LuckPerms getLuckPerms() {
        return api;
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
