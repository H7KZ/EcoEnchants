package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.util.Logger;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
public final class Kinetic extends EcoEnchant {
    public Kinetic() {
        super(
                new EcoEnchantBuilder("kinetic", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)) return;

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "reduction-per-level");
        double multiplier = 1 - ((reduction/100) * level);
        event.setDamage(event.getDamage() * multiplier);
    }
}
