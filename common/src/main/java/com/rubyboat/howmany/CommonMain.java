package com.rubyboat.howmany;

import com.rubyboat.howmany.save.Serializer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CommonMain
{
	public static KeyBinding TRACK_ITEM_KEYBIND = new KeyBinding(
			"key.howmany.reload",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_R,
			"key.category.howmany.general"
	);

	public static final boolean registerKeybind = true;

	public static InputUtil.Key trackBindKeycode = InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_L);

	public static final String MOD_ID = "howmany";

	public static void init() {
		try {
			Serializer.Load();
		}catch (Exception ignored) {

		}
	}

	public static void tick() {
		if(TRACK_ITEM_KEYBIND.wasPressed()) {
			try {
				Serializer.Load();
			}catch (Exception ignored) {

			}
		}
	}
}