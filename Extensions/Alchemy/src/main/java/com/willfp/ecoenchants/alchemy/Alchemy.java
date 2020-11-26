package com.willfp.ecoenchants.alchemy;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

public class Alchemy extends EcoEnchant {
    public Alchemy() {
        super("alchemy", EnchantmentType.NORMAL, AlchemyMain.class);
    }

    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if(event.getNewEffect() == null) return;
        if(!(event.getEntity() instanceof LivingEntity)) return;

        LivingEntity entity = (LivingEntity) event.getEntity();

        int level = EnchantChecks.getArmorPoints(entity, this);
        if(level == 0) return;

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        PotionEffect effect = event.getNewEffect();

        PotionEffect newEffect = new PotionEffect(
                effect.getType(),
                effect.getDuration(),
                ((effect.getAmplifier() + 1) * 2) - 1,
                effect.isAmbient(),
                effect.hasParticles(),
                effect.hasIcon()
        );

        entity.removePotionEffect(effect.getType());
        entity.addPotionEffect(newEffect);
    }
}
