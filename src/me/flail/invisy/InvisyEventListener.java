package me.flail.invisy;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.potion.PotionEffectType;

import me.flail.invisy.tools.Logger;

public class InvisyEventListener extends Logger implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void entityTargetEntity(EntityTargetLivingEntityEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}

		LivingEntity target = event.getTarget();
		if (target.hasPotionEffect(PotionEffectType.INVISIBILITY) && plugin.mobsIgnoreInvisPlayers) {
			event.setCancelled(true);
		}

	}

}
