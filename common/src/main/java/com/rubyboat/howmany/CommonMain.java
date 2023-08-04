package com.rubyboat.howmany;

import com.rubyboat.howmany.entry.TrackedItemEntry;
import com.rubyboat.howmany.entry.TrackedScriptEntry;
import com.rubyboat.howmany.gui.EditScreen;
import com.rubyboat.howmany.gui.HowManyGUI;
import com.rubyboat.howmany.save.Serializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CommonMain
{
	/**
	 * 1.2 - Scripting & New UI
	 * 1.3 - Templates
	 * 1.4 - Shulker Boxes + Open Inventories
	 * 1.5 - Networking
	 */
	public static KeyBinding RELOAD_CONFIG_KEYBIND = new KeyBinding(
			"key.howmany.reload",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_R,
			"key.category.howmany.general"
	);
	public static KeyBinding OPEN_EDIT_MENU_KEYBIND = new KeyBinding(
			"key.howmany.edit",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_H,
			"key.category.howmany.general"
	);

	public static final boolean registerKeybind = true;

	public static InputUtil.Key trackBindKeycode = InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_L);

	public static final String MOD_ID = "howmany";

	public static void init() {
		Serializer.typeToEntry.put("item", TrackedItemEntry.class);
		Serializer.typeToEntry.put("inline_scripted", TrackedScriptEntry.class);

		try {
			Serializer.Load();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void keybindTick() {
		if(RELOAD_CONFIG_KEYBIND.wasPressed()) {
			try {
				Serializer.Load();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(OPEN_EDIT_MENU_KEYBIND.wasPressed() && HowManyGUI.trackedItems.size() >= 1) {
			MinecraftClient.getInstance().setScreen(new EditScreen());
		}
	}
}