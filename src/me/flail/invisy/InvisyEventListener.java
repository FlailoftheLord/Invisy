package me.flail.invisy;

import org.bukkit.craftbukkit.v1_14_R1.entity.CraftMob;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.potion.PotionEffectType;

import me.flail.invisy.tools.Logger;

public class InvisyEventListener extends Logger implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void entityTargetEntity(EntityTargetLivingEntityEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}

		try {
			LivingEntity target = event.getTarget();
			if (target.hasPotionEffect(PotionEffectType.INVISIBILITY) && plugin.mobsIgnoreInvisPlayers) {
				event.setCancelled(true);
			}

		} catch (Exception e) {
		}

	}

	@EventHandler(priority=EventPriority.LOW)
	public void entityDamaged(EntityDamageByEntityEvent event) {
		if ((event.getDamager() instanceof LivingEntity) && !(event.getDamager() instanceof Player)) {
			LivingEntity target = (LivingEntity) event.getEntity();
			LivingEntity targetter = (LivingEntity) event.getDamager();

			if (!(target instanceof Player)) {
				return;
			}

			if (target.hasPotionEffect(PotionEffectType.INVISIBILITY) && plugin.mobsIgnoreInvisPlayers) {
				event.setDamage(0.0);

				try {
					((CraftMob) targetter).setTarget(null);

				} catch (Exception e) {
				}

			}

		}

	}

}
