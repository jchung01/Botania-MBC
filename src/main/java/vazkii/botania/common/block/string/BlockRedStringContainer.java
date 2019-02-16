/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 14, 2014, 5:29:56 PM (GMT)]
 */
package vazkii.botania.common.block.string;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.block.tile.string.TileRedStringContainer;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockRedStringContainer extends BlockRedString {

	public BlockRedStringContainer(Block.Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.FACING, EnumFacing.DOWN));
	}

	@Nonnull
	@Override
	public TileRedString createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileRedStringContainer();
	}

}
