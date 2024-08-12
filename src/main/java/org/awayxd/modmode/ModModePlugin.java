package org.awayxd.modmode;

import org.awayxd.modmode.commands.*;
import org.awayxd.modmode.listeners.PlayerConnectListener;
import org.awayxd.modmode.listeners.PlayerDropPickupListener;
import org.awayxd.modmode.listeners.PlayerInteractListener;
import org.awayxd.modmode.listeners.PlayerMoveListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;

public class ModModePlugin extends JavaPlugin {

    private static ModModePlugin instance;
    private PlayerInteractListener interactListener;
    private final Set<UUID> modModePlayers = new HashSet<>();
    private final Map<UUID, Boolean> glowVisibility = new HashMap<>(); // Track glow visibility
    private File playerIpsFile;
    private YamlConfiguration playerIpsConfig;

    @Override
    public void onEnable() {
        instance = this;
        interactListener = new PlayerInteractListener();

        // Initialize player IP tracking
        playerIpsFile = new File(getDataFolder(), "player_ips.yml");
        if (!playerIpsFile.exists()) {
            saveResource("player_ips.yml", false);
        }
        playerIpsConfig = YamlConfiguration.loadConfiguration(playerIpsFile);

        // Register command executors
        getCommand("mod").setExecutor(new ModModeCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("unfreeze").setExecutor(new UnfreezeCommand());
        getCommand("invsee").setExecutor(new InvseeCommand());
        getCommand("staffchat").setExecutor(new StaffChatCommand());
        getCommand("smite").setExecutor(new SmiteCommand());
        getCommand("checkalts").setExecutor(new CheckAltsCommand(this)); // Register /checkalts command

        // Register event listeners
        getServer().getPluginManager().registerEvents(interactListener, this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropPickupListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerConnectListener(this), this); // Register player IP tracking

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

    public Map<UUID, Boolean> getGlowVisibility() {
        return glowVisibility;
    }

    public File getPlayerIpsFile() {
        return playerIpsFile;
    }

    public YamlConfiguration getPlayerIpsConfig() {
        return playerIpsConfig;
    }
}
