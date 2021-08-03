/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.recipe.StateIngredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class StateIngredientBlocks implements StateIngredient {
	protected final ImmutableSet<Block> blocks;

	public StateIngredientBlocks(Collection<Block> blocks) {
		this.blocks = ImmutableSet.copyOf(blocks);
	}

	@Override
	public boolean test(BlockState state) {
		return blocks.contains(state.getBlock());
	}

	@Override
	public BlockState pick(Random random) {
		return blocks.asList().get(random.nextInt(blocks.size())).defaultBlockState();
	}

	@Override
	public JsonObject serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", "blocks");
		JsonArray array = new JsonArray();
		for (Block block : blocks) {
			array.add(Registry.BLOCK.getKey(block).toString());
		}
		object.add("blocks", array);
		return object;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		List<Block> blocks = getBlocks();
		buffer.writeVarInt(0);
		buffer.writeVarInt(blocks.size());
		for (Block block : blocks) {
			buffer.writeVarInt(Registry.BLOCK.getId(block));
		}
	}

	@Override
	public List<BlockState> getDisplayed() {
		return blocks.stream().map(Block::defaultBlockState).collect(Collectors.toList());
	}

	@Nullable
	@Override
	public StateIngredient resolveAndFilter(UnaryOperator<List<Block>> operator) {
		List<Block> list = operator.apply(this.getBlocks());
		if (list != null) {
			return list.isEmpty() ? null : StateIngredientHelper.of(list);
		}
		return this;
	}

	@Nonnull
	protected List<Block> getBlocks() {
		return blocks.asList();
	}

	@Override
	public String toString() {
		return "StateIngredientBlocks{" + blocks.toString() + "}";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		return blocks.equals(((StateIngredientBlocks) o).blocks);
	}

	@Override
	public int hashCode() {
		return Objects.hash(blocks);
	}
}
