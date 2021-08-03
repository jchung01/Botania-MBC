/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DetectorRailBlock;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.components.EntityComponents;

import java.util.List;

public class SubTileEntropinnyum extends TileEntityGeneratingFlower {
	private static final int RANGE = 12;
	private static final int EXPLODE_EFFECT_EVENT = 0;
	private static final int ANGRY_EFFECT_EVENT = 1;

	public SubTileEntropinnyum() {
		super(ModSubtiles.ENTROPINNYUM);
	}

	public static boolean isUnethical(Entity e) {
		if (!e.level.getChunkSource().isEntityTickingChunk(e)) {
			return false;
		}

		BlockPos center = e.blockPosition();
		int x = center.getX();
		int y = center.getY();
		int z = center.getZ();
		int range = 3;

		// Should actually check for corals too, but it gets broken when the piston extends
		int movingPistons = 0;
		int rails = 0;
		int slimes = 0;
		for (BlockPos pos : BlockPos.betweenClosed(x - range, y - range, z - range, x + range + 1, y + range + 1, z + range + 1)) {
			BlockState state = e.level.getBlockState(pos);
			if (state.getBlock() == Blocks.MOVING_PISTON) {
				movingPistons++;
				BlockEntity te = e.level.getBlockEntity(pos);
				if (te instanceof PistonMovingBlockEntity) {
					state = ((PistonMovingBlockEntity) te).getMovedState();
				}
			}

			if (state.getBlock() instanceof DetectorRailBlock) {
				rails++;
			} else if (state.getBlock() instanceof SlimeBlock || state.getBlock() instanceof HoneyBlock) {
				slimes++;
			}

			if (movingPistons > 0 && rails > 0 && slimes > 0) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide && getMana() == 0) {
			List<PrimedTnt> tnts = getLevel().getEntitiesOfClass(PrimedTnt.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)));
			for (PrimedTnt tnt : tnts) {
				FluidState fluid = getLevel().getFluidState(tnt.blockPosition());
				if (tnt.getLife() == 1 && tnt.isAlive() && fluid.isEmpty()) {
					boolean unethical = EntityComponents.TNT_ETHICAL.get(tnt).unethical;
					tnt.playSound(unethical ? SoundEvents.GENERIC_EXTINGUISH_FIRE : SoundEvents.GENERIC_EXPLODE, 0.2F, (1F + (getLevel().random.nextFloat() - getLevel().random.nextFloat()) * 0.2F) * 0.7F);
					tnt.remove();
					addMana(unethical ? 3 : getMaxMana());
					sync();

					getLevel().blockEvent(getBlockPos(), getBlockState().getBlock(), unethical ? ANGRY_EFFECT_EVENT : EXPLODE_EFFECT_EVENT, tnt.getId());
					break;
				}
			}
		}
	}

	@Override
	public boolean triggerEvent(int event, int param) {
		if (event == EXPLODE_EFFECT_EVENT) {
			if (getLevel().isClientSide && getLevel().getEntity(param) instanceof PrimedTnt) {
				Entity e = getLevel().getEntity(param);

				for (int i = 0; i < 50; i++) {
					SparkleParticleData data = SparkleParticleData.sparkle((float) (Math.random() * 0.65F + 1.25F), 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 12);
					level.addParticle(data, e.getX() + Math.random() * 4 - 2, e.getY() + Math.random() * 4 - 2, e.getZ() + Math.random() * 4 - 2, 0, 0, 0);
				}

				getLevel().addParticle(ParticleTypes.EXPLOSION_EMITTER, e.getX(), e.getY(), e.getZ(), 1D, 0D, 0D);
			}
			return true;
		} else if (event == ANGRY_EFFECT_EVENT) {
			if (getLevel().isClientSide && getLevel().getEntity(param) instanceof PrimedTnt) {
				Entity e = getLevel().getEntity(param);

				for (int i = 0; i < 50; i++) {
					level.addParticle(ParticleTypes.ANGRY_VILLAGER, e.getX() + Math.random() * 4 - 2, e.getY() + Math.random() * 4 - 2, e.getZ() + Math.random() * 4 - 2, 0, 0, 0);
				}
			}

			return true;
		} else {
			return super.triggerEvent(event, param);
		}
	}

	@Override
	public int getColor() {
		return 0xcb0000;
	}

	@Override
	public int getMaxMana() {
		return 6500;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
	}

}
