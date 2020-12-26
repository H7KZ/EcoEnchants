package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.util.DurabilityUtils;
import com.willfp.eco.util.interfaces.EcoRunnable;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Repairing extends EcoEnchant implements EcoRunnable {
    private final Set<Player> players = new HashSet<>();
    private int amount = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "multiplier");

    public Repairing() {
        super(
                "repairing", EnchantmentType.SPECIAL
        );
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;
        refreshPlayer((Player) event.getEntity());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        refresh();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        refresh();
    }

    @EventHandler
    public void onInventoryDrop(EntityDropItemEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;
        refreshPlayer((Player) event.getEntity());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player))
            return;
        refreshPlayer((Player) event.getWhoClicked());
    }

    private void refresh() {
        players.clear();
        this.getPlugin().getServer().getOnlinePlayers().forEach(player -> {
            if(Arrays.stream(player.getInventory().getContents()).parallel().anyMatch(item -> EnchantChecks.item(item, this)))
                players.add(player);
        });
        amount = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "multiplier");
    }

    private void refreshPlayer(Player player) {
        players.remove(player);
        if(Arrays.stream(player.getInventory().getContents()).parallel().anyMatch(item -> EnchantChecks.item(item, this)))
            players.add(player);
    }

    @Override
    public void run() {
        players.forEach((player -> {
            if(this.getDisabledWorlds().contains(player.getWorld())) return;
            for(ItemStack item : player.getInventory().getContents()) {
                int level = EnchantChecks.getItemLevel(item, this);
                if(level == 0) continue;

                if(!(item.getItemMeta() instanceof Repairable)) continue;

                if(player.getInventory().getItemInMainHand().equals(item)) continue;
                if(player.getInventory().getItemInOffHand().equals(item)) continue;
                if(player.getItemOnCursor().equals(item)) continue;

                DurabilityUtils.repairItem(item, amount * level);
                player.updateInventory();
            }
        }));
    }

    @Override
    public long getTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "repeat-ticks");
    }
}