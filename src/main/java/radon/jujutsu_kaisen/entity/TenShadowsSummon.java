package radon.jujutsu_kaisen.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import radon.jujutsu_kaisen.ability.JJKAbilities;
import radon.jujutsu_kaisen.capability.data.SorcererDataHandler;
import radon.jujutsu_kaisen.capability.data.sorcerer.CursedTechnique;
import radon.jujutsu_kaisen.entity.base.ICommandable;
import radon.jujutsu_kaisen.entity.base.SummonEntity;
import radon.jujutsu_kaisen.network.PacketHandler;
import radon.jujutsu_kaisen.network.packet.s2c.SyncSorcererDataS2CPacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public abstract class TenShadowsSummon extends SummonEntity implements ICommandable {
    protected final List<UUID> participants = new ArrayList<>();

    protected TenShadowsSummon(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private void spawnParticles() {
        this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < this.getBbHeight() * this.getBbHeight(); j++) {
                this.level.addParticle(ParticleTypes.SMOKE, this.getX() + (this.getBbWidth() * this.random.nextGaussian() * 0.1F), this.getY(),
                        this.getZ() + (this.getBbWidth() * this.random.nextGaussian() * 0.1F),
                        this.random.nextGaussian() * 0.075F, this.random.nextGaussian() * 0.25F, this.random.nextGaussian() * 0.075F);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + (this.getBbWidth() * this.random.nextGaussian() * 0.1F), this.getY(),
                        this.getZ() + (this.getBbWidth() * this.random.nextGaussian() * 0.1F),
                        this.random.nextGaussian() * 0.075F, this.random.nextGaussian() * 0.25F, this.random.nextGaussian() * 0.075F);
            }
        }
    }

    @Override
    public boolean changeTarget(LivingEntity target) {
        if (this.isTame()) {
            this.setTarget(target);
            return true;
        }
        return false;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        this.spawnParticles();

        if (!this.isTame()) {
            Vec3 center = this.position();
            AABB area = new AABB(center.x() - 16.0D, center.y() - 16.0D, center.z() - 16.0D,
                    center.x() + 16.0D, center.y() + 16.0D, center.z() + 16.0D);

            for (LivingEntity participant : this.level.getEntitiesOfClass(LivingEntity.class, area)) {
                if (participant == this) continue;

                if (!participant.getCapability(SorcererDataHandler.INSTANCE).isPresent()) continue;

                this.participants.add(participant.getUUID());
            }
        }
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();

        this.spawnParticles();
    }

    @Override
    public void die(@NotNull DamageSource pCause) {
        super.die(pCause);

        LivingEntity owner = this.getOwner();

        if (owner != null && !owner.level.isClientSide) {
            owner.getCapability(SorcererDataHandler.INSTANCE).ifPresent(cap -> {
                if (cap.getTechnique() == CursedTechnique.TEN_SHADOWS) {
                    if (!this.isTame()) {
                        if (pCause.getEntity() == owner) {
                            cap.tame(this.level.registryAccess().registryOrThrow(Registries.ENTITY_TYPE), this.getType());

                            if (owner instanceof ServerPlayer player) {
                                PacketHandler.sendToClient(new SyncSorcererDataS2CPacket(cap.serializeNBT()), player);
                            }
                        }
                    } else {
                        if (this.getAbility().canDie()) {
                            cap.kill(this.level.registryAccess().registryOrThrow(Registries.ENTITY_TYPE), this.getType());
                        }
                    }
                }
            });
        }
    }

    private void checkParticipants() {
        Iterator<UUID> iter = this.participants.iterator();

        while (iter.hasNext()) {
            UUID identifier = iter.next();

            LivingEntity participant = (LivingEntity) ((ServerLevel) this.level).getEntity(identifier);

            if (participant == null || participant.isRemoved() || !participant.isAlive() || participant.distanceTo(this) > this.getAttributeValue(Attributes.FOLLOW_RANGE)) {
                iter.remove();
            }
        }
    }

    @Override
    public void tick() {
        LivingEntity owner = this.getOwner();

        if (this.isTame() && !this.level.isClientSide && (owner == null || owner.isRemoved() || !owner.isAlive() ||
                (!this.isDeadOrDying() && !JJKAbilities.hasToggled(owner, this.getAbility())))) {
            this.discard();
        } else {
            super.tick();

            if (!this.level.isClientSide) {
                if (!this.isTame()) {
                    this.checkParticipants();

                    boolean disappear = this.participants.isEmpty();

                    if (disappear) {
                        this.discard();
                    }
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        ListTag participantsTag = new ListTag();

        for (UUID identifier : this.participants) {
            participantsTag.add(NbtUtils.createUUID(identifier));
        }
        pCompound.put("participants", participantsTag);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        for (Tag key : pCompound.getList("participants", Tag.TAG_INT_ARRAY)) {
            this.participants.add(NbtUtils.loadUUID(key));
        }
    }

    @Override
    public boolean isOwnedBy(@NotNull LivingEntity pEntity) {
        return this.isTame() && super.is(pEntity);
    }
}
