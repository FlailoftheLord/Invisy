package me.flail.invisy;

import org.bukkit.craftbukkit.v1_14_R1.entity.CraftMob;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

import me.flail.invisy.tools.Logger;
import me.flail.invisy.user.User;

public class InvisyEventListener extends Logger implements Listener, PacketListener {

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

	@EventHandler
	public void packetEvent(PacketEvent event) {
		PacketType type = event.getPacketType();

	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent event) {
		plugin.invisibleUsers.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void playerDC(PlayerKickEvent event) {
		plugin.invisibleUsers.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void playerJoin(PlayerJoinEvent event) {
		User user = new User(event.getPlayer().getUniqueId());

		if (user.isVanished() && plugin.persistVanish) {
			user.setVanished(true);
		} else {
			user.setVanished(false);
		}

		plugin.loadVanishedPlayers();
	}

	@Override
	public Plugin getPlugin() {
		return plugin;
	}

	@Override
	public ListeningWhitelist getReceivingWhitelist() {
		return null;
	}

	@Override
	public ListeningWhitelist getSendingWhitelist() {
		return null;
	}

	@Override
	public void onPacketReceiving(PacketEvent paramPacketEvent) {

	}

	@Override
	public void onPacketSending(PacketEvent paramPacketEvent) {

	}

}
