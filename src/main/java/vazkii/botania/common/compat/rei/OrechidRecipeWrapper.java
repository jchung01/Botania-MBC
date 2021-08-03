/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import java.util.Map;

public class OrechidRecipeWrapper implements Comparable<OrechidRecipeWrapper> {
	public final Map.Entry<ResourceLocation, Integer> entry;

	public OrechidRecipeWrapper(Map.Entry<ResourceLocation, Integer> entry) {
		this.entry = entry;
	}

	@Override
	public int compareTo(@Nonnull OrechidRecipeWrapper o) {
		return Integer.compare(o.entry.getValue(), entry.getValue());
	}
}
