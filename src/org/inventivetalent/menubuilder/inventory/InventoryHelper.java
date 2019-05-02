package org.inventivetalent.menubuilder.inventory;

import org.bukkit.inventory.Inventory;
import org.inventivetalent.menubuilder.util.AccessUtil;
import org.inventivetalent.menubuilder.util.Reflection;

public class InventoryHelper {

	static Class<?> obcCraftInventory;
	static Class<?> obcCraftInventoryCustom;
	static Class<?> obcMinecraftInventory;
	static Class<?> nmsChatComponentText;

	static {
		try {
			obcCraftInventory = Reflection.getOBCClass("inventory.CraftInventory");
			obcCraftInventoryCustom = Reflection.getOBCClass("inventory.CraftInventoryCustom");
			nmsChatComponentText = Reflection.getNMSClass("ChatComponentText");
			for (Class<?> c : obcCraftInventoryCustom.getDeclaredClasses()) {
				if (c.getSimpleName().equals("MinecraftInventory")) {
					obcMinecraftInventory = c;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void changeTitle(Inventory inv, String title) {
		try {
			Object minecrafInventory = AccessUtil.setAccessible(obcCraftInventory.getDeclaredField("inventory")).get(inv);
			try {
				AccessUtil.setAccessible(obcMinecraftInventory.getDeclaredField("title")).set(minecrafInventory, nmsChatComponentText.getConstructor(String.class).newInstance(title));
			}catch (IllegalArgumentException e)
			{
				AccessUtil.setAccessible(obcMinecraftInventory.getDeclaredField("title")).set(minecrafInventory, title);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}