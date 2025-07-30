package net.sergiu.minecraftmod.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;

import net.sergiu.minecraftmod.entity.ModEntities;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ZarathustraEntity extends Zombie {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.literal("Zarathustra Final Boss"),
            BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_12);
    private Level level;

    public ZarathustraEntity(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
        this.level = level;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ZombieAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH,        300.0D)  // 200 HP!
                .add(Attributes.ATTACK_DAMAGE,     17.0D)   // hits like a truck
                .add(Attributes.MOVEMENT_SPEED,    0.40D)   // faster than a normal zombie
                .add(Attributes.FOLLOW_RANGE,      48.0D)   // spots you from far away
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)  // completely immovable
                .add(Attributes.ARMOR,             15.0D)   // natural damage reduction
                .add(Attributes.ARMOR_TOUGHNESS,   5.0D)
                .add(Attributes.ATTACK_SPEED, 10.0D)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 10.0D);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
            ServerLevelAccessor world,
            DifficultyInstance difficulty,
            MobSpawnType spawnType,
            @Nullable SpawnGroupData data
    ) {
        data = super.finalizeSpawn(world, difficulty, spawnType, data);

        // If this is a baby instance, cut its max health in half
        if (this.isBaby()) {
            // NOTE: max‐health comes from your attribute builder:
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getAttribute(Attributes.MAX_HEALTH).getBaseValue() * 0.1);

            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() * 1.2);

            this.setHealth(this.getMaxHealth());
        }

        return data;
    }


    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if(idleAnimationTimeout-- <= 0) {
            idleAnimationState.start(this.tickCount);
            idleAnimationTimeout = this.random.nextInt(100) + 40;
        }

        if (!level.isClientSide
                && this.getHealth() < this.getMaxHealth() * 0.25
                && this.random.nextDouble() < 0.02)
        {
            if (level instanceof ServerLevel server) {
                ZarathustraEntity baby = ModEntities.ZARATHUSTRA.get().create(level);
                if (baby != null) {
                    baby.setBaby(true);
                    double dx = this.getX() + (this.random.nextDouble() - 0.5);
                    double dy = this.getY();
                    double dz = this.getZ() + (this.random.nextDouble() - 0.5);
                    baby.moveTo(dx, dy, dz, this.getYRot(), this.getXRot());
                    // After baby.moveTo(...)
                    DifficultyInstance diff = server.getCurrentDifficultyAt(baby.blockPosition());
                    baby.finalizeSpawn(
                            server,
                            diff,
                            MobSpawnType.EVENT,
                            null
                    );
                    server.addFreshEntity(baby);
                    this.level.playSound(
                            null,
                            this.blockPosition(),
                            SoundEvents.ZOMBIE_INFECT,
                            SoundSource.HOSTILE,
                            1.0F,
                            1.0F
                    );
                }
            }
        }

    }

    /* BOSS BAR */

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        if (!this.isBaby()) {
            super.startSeenByPlayer(player);
            bossEvent.addPlayer(player);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        if (!this.isBaby()) {
            super.stopSeenByPlayer(player);
            bossEvent.removePlayer(player);
        }
    }


    @Override
    public void aiStep() {
        super.aiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }


    @Override
    public void die(DamageSource cause) {
        super.die(cause);

        // Only run on the server side
        if (!this.level.isClientSide) {
            // Find all baby Zarathustra within, say, 32 blocks
            List<ZarathustraEntity> babies = this.level.getEntitiesOfClass(
                    ZarathustraEntity.class,
                    this.getBoundingBox().inflate(64.0),
                    baby -> baby.isBaby()
            );

            for (ZarathustraEntity baby : babies) {
                // Remove them cleanly as if “killed”
                baby.remove(RemovalReason.KILLED);
            }
        }
    }

}
