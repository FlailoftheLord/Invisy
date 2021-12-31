package me.flail.invisy;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
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
			if (target.hasPotionEffect(PotionEffectType.INVISIBILITY) && plugin.mobsIgnoreInvisPlayers
					|| plugin.invisibleUsers.contains(target.getUniqueId())) {
				event.setCancelled(true);
			}

		} catch (Exception e) {
		}

	}

	@EventHandler(priority = EventPriority.LOW)
	public void entityDamaged(EntityDamageByEntityEvent event) {
		if ((event.getDamager() instanceof LivingEntity) && !(event.getDamager() instanceof Player)
				&& (event.getEntity() instanceof LivingEntity)) {
			LivingEntity target = (LivingEntity) event.getEntity();
			LivingEntity targetter = (LivingEntity) event.getDamager();

			if (!(target instanceof Player)) {
				return;
			}

			if (target.hasPotionEffect(PotionEffectType.INVISIBILITY) && plugin.mobsIgnoreInvisPlayers
					|| plugin.invisibleUsers.contains(target.getUniqueId())) {
				event.setDamage(0.0);

				try {
					((Mob) targetter).setTarget(null);

				} catch (Exception e) {
				}

			}

		}

	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		User u = new User(uuid);
		if (u.isVanished())
			event.setQuitMessage(null);

		plugin.invisibleUsers.remove(uuid);
		plugin.userMap.remove(uuid);
	}

	@EventHandler
	public void playerDC(PlayerKickEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		plugin.invisibleUsers.remove(uuid);
		plugin.userMap.remove(uuid);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void playerJoin(PlayerJoinEvent event) {
		User user = new User(event.getPlayer().getUniqueId());
		plugin.userMap.put(user.uuid(), user);

		if (user.isVanished() && plugin.persistVanish)
			event.setJoinMessage(null);

		plugin.loadVanishedPlayers();
	}

	@EventHandler
	public void serverlistPing(ServerListPingEvent event) {
		Iterator<Player> iter = event.iterator();
		while (iter.hasNext()) {
			User u = new User(iter.next().getUniqueId());
			if (u.isVanished())
				iter.remove();

		}
	}

	@EventHandler
	public void gamemodeChange(PlayerGameModeChangeEvent e) {
		User u = new User(e.getPlayer().getUniqueId());
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			if (u.isVanished() && plugin.flyOnVanish)
				u.player().setAllowFlight(true);
			u.player().setFlying(true);
		}, 1L);

	}

	@EventHandler
	public void advancement(PlayerAdvancementDoneEvent e) {
		User u = new User(e.getPlayer().getUniqueId());

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
	public void onPacketReceiving(PacketEvent e) {
		/*
		 *  Not completed yet, is for canceling player packets via tablist & other actions.
		 */

	}

	@Override
	public void onPacketSending(PacketEvent e) {
		if (e.getPacketType().equals(PacketType.Play.Client.ADVANCEMENTS))
			e.setCancelled(true);
		/*
		 *  Not completed yet, is for canceling player packets via tablist & other actions.
		 */

	}

}
