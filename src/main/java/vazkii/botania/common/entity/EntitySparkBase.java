/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

public abstract class EntitySparkBase extends Entity {
	private static final String TAG_INVIS = "invis";
	private static final String TAG_NETWORK = "network";
	private static final EntityDataAccessor<Integer> NETWORK = SynchedEntityData.defineId(EntitySparkBase.class, EntityDataSerializers.INT);

	public EntitySparkBase(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(NETWORK, 0);
	}

	public DyeColor getNetwork() {
		return DyeColor.byId(entityData.get(NETWORK));
	}

	public void setNetwork(DyeColor color) {
		entityData.set(NETWORK, color.getId());
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		setInvisible(compound.getBoolean(TAG_INVIS));
		setNetwork(DyeColor.byId(compound.getInt(TAG_NETWORK)));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putBoolean(TAG_INVIS, isInvisible());
		compound.putInt(TAG_NETWORK, getNetwork().getId());
	}
}
