package me.flail.invisy.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class BaseUtilities extends LegacyUtils {

	/**
	 * Add new NBT data to this Item.
	 * 
	 * @param item
	 *                 the item to add the data to.
	 * @param key
	 *                 the key which you will store (and retrieve) the data.
	 * @param tag
	 *                 the data to set on the item.
	 * @return the new ItemStack with the set data on it.
	 */
	protected ItemStack addTag(ItemStack item, String key, String tag) {
		return addLegacyTag(item, key, tag);
	}

	/**
	 * Removes the tag from the ItemStack
	 * 
	 * @param item
	 * @param key
	 *                 the key which the NBT tag is stored under.
	 * @return the new ItemStack with the data removed.
	 */
	protected ItemStack removeTag(ItemStack item, String key) {
		return removeLegacyTag(item, key);
	}

	/**
	 * Gets the value stored via the NBT tag key.
	 * <br>
	 * It would be best to check {@link #hasTag()} before getting the tag.
	 * 
	 * @param item
	 * @param key
	 *                 the key to which the NBT data is stored.
	 * @return value of the NBT tag if found.
	 */
	protected String getTag(ItemStack item, String key) {
		return getLegacyTag(item, key);
	}

	/**
	 * Checks if the ItemStack has the specified Tag key.
	 * 
	 * @param item
	 *                 Item to check for NBT data tag.
	 * @param key
	 *                 String tag to check for.
	 * @return true if found, otherwise false.
	 */
	protected static boolean hasTag(ItemStack item, String key) {
		return hasLegacyTag(item, key);
	}

	/**
	 * Grabs the color code which modifies the substring <code>before</code> in the string
	 * <code>string</code>
	 * 
	 * @param string
	 * @param before
	 */
	public String getColor(String string, String before) {
		String first = string.split(before)[0];
		char c = first.charAt(first.lastIndexOf("&") + 1);
		return "&" + c;
	}

	/**
	 * @param mode
	 *                 whether to check only hostile mobs, passives or both.
	 * @return a full list of all EntityTypes which are valid Mobs. <br>
	 *         (not entities like arrows and effects)
	 */
	protected List<EntityType> validMobs(String mode) {
		List<EntityType> mobs = new ArrayList<>(16);

		switch (mode) {
		case "hostile":
			mobs.addAll(this.hostileMobs());
			break;
		case "passive":
			mobs.addAll(this.passiveMobs());
			break;
		default:
			mobs.addAll(passiveMobs());
			mobs.addAll(hostileMobs());
		}

		return mobs;
	}

	/**
	 * @return a list of all mobs which are "passive" or "non-hostile"
	 */
	private List<EntityType> passiveMobs() {
		List<EntityType> list = new ArrayList<>();
		list.add(EntityType.BAT);
		list.add(EntityType.CHICKEN);
		list.add(EntityType.COD);
		list.add(EntityType.COW);
		list.add(EntityType.DOLPHIN);
		list.add(EntityType.DONKEY);
		list.add(EntityType.FOX);
		list.add(EntityType.HORSE);
		list.add(EntityType.IRON_GOLEM);
		list.add(EntityType.LLAMA);
		list.add(EntityType.MULE);
		list.add(EntityType.MUSHROOM_COW);
		list.add(EntityType.OCELOT);
		list.add(EntityType.PARROT);
		list.add(EntityType.PIG);
		list.add(EntityType.POLAR_BEAR);
		list.add(EntityType.RABBIT);
		list.add(EntityType.SALMON);
		list.add(EntityType.SHEEP);
		list.add(EntityType.SKELETON_HORSE);
		list.add(EntityType.SNOWMAN);
		list.add(EntityType.SQUID);
		list.add(EntityType.TRADER_LLAMA);
		list.add(EntityType.TROPICAL_FISH);
		list.add(EntityType.TURTLE);
		list.add(EntityType.VILLAGER);
		list.add(EntityType.WANDERING_TRADER);
		list.add(EntityType.WOLF);
		list.add(EntityType.ZOMBIE_HORSE);

		return list;
	}

	/**
	 * @return a list of all mobs which are "hostiles"
	 */
	private List<EntityType> hostileMobs() {
		List<EntityType> list = new ArrayList<>();
		list.add(EntityType.BLAZE);
		list.add(EntityType.CAVE_SPIDER);
		list.add(EntityType.CREEPER);
		list.add(EntityType.DROWNED);
		list.add(EntityType.ELDER_GUARDIAN);
		list.add(EntityType.ENDERMAN);
		list.add(EntityType.ENDERMITE);
		list.add(EntityType.EVOKER);
		list.add(EntityType.GHAST);
		list.add(EntityType.GUARDIAN);
		list.add(EntityType.HUSK);
		list.add(EntityType.ILLUSIONER);
		list.add(EntityType.MAGMA_CUBE);
		list.add(EntityType.PHANTOM);
		list.add(EntityType.PIG_ZOMBIE);
		list.add(EntityType.PILLAGER);
		list.add(EntityType.PUFFERFISH);
		list.add(EntityType.RAVAGER);
		list.add(EntityType.SHULKER);
		list.add(EntityType.SILVERFISH);
		list.add(EntityType.SKELETON);
		list.add(EntityType.SLIME);
		list.add(EntityType.SPIDER);
		list.add(EntityType.STRAY);
		list.add(EntityType.VEX);
		list.add(EntityType.VINDICATOR);
		list.add(EntityType.WITCH);
		list.add(EntityType.WITHER_SKELETON);
		list.add(EntityType.ZOMBIE);
		list.add(EntityType.ZOMBIE_VILLAGER);

		return list;
	}

	private TreeMap<Integer, String> map = new TreeMap<>();

	public String romanNumeral(int number) {
		if (number == 0) {
			return "0";
		}

		map.clear();
		map.put(i(1000), "M");
		map.put(i(900), "CM");
		map.put(i(500), "D");
		map.put(i(400), "CD");
		map.put(i(100), "C");
		map.put(i(90), "XC");
		map.put(i(50), "L");
		map.put(i(40), "XL");
		map.put(i(10), "X");
		map.put(i(9), "IX");
		map.put(i(5), "V");
		map.put(i(4), "IV");
		map.put(i(1), "I");

		int n = map.floorKey(i(number)).intValue();
		if (number == n) {
			return map.get(i(number));
		}
		return map.get(i(n)) + romanNumeral(number - n);
	}

	private Integer i(int n) {
		return Integer.valueOf(n);
	}

	/**
	 * Methods for retrieving base NMS classes.
	 * 
	 * @author FlailoftheLord
	 */
	public static class Reflection {

		public static Class<?> getClass(String className) {
			return getNMSClass(className);
		}

		protected static Class<?> getNMSClass(String classDef) {
			try {

				return Class.forName(
						"net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "."
								+ classDef);
			} catch (Exception e) {

				e.printStackTrace();
				return null;
			}

		}

	}

}
