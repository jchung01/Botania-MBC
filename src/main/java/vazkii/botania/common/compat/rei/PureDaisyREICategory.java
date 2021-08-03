/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.RecipePureDaisy;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;

@Environment(EnvType.CLIENT)
public class PureDaisyREICategory implements RecipeCategory<PureDaisyREIDisplay> {
	private EntryStack daisy = EntryStack.create(new ItemStack(ModSubtiles.pureDaisy));
	private ResourceLocation OVERLAY = ResourceLocationHelper.prefix("textures/gui/pure_daisy_overlay.png");

	@Override
	public @NotNull ResourceLocation getIdentifier() {
		return RecipePureDaisy.TYPE_ID;
	}

	@Override
	public @NotNull EntryStack getLogo() {
		return daisy;
	}

	@Override
	public @NotNull String getCategoryName() {
		return I18n.get("botania.nei.pureDaisy");
	}

	@Override
	public @NotNull List<Widget> setupDisplay(PureDaisyREIDisplay display, Rectangle bounds) {
		List<Widget> widgets = new ArrayList<>();
		Point center = new Point(bounds.getCenterX() - 8, bounds.getCenterY() - 16);

		widgets.add(CategoryUtils.drawRecipeBackground(bounds));
		widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> CategoryUtils.drawOverlay(helper, matrices, OVERLAY, center.x - 24, center.y - 14, 0, 0, 65, 44))));
		widgets.add(Widgets.createSlot(center).entry(daisy).disableBackground());
		widgets.add(Widgets.createSlot(new Point(center.x - 31, center.y)).entries(display.getInputEntries().get(0)).disableBackground());
		widgets.add(Widgets.createSlot(new Point(center.x + 29, center.y)).entries(display.getResultingEntries().get(0)).disableBackground());
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 72;
	}
}
