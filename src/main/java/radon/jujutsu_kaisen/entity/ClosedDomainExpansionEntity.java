package radon.jujutsu_kaisen.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import radon.jujutsu_kaisen.ability.base.DomainExpansion;
import radon.jujutsu_kaisen.block.DomainBlockEntity;
import radon.jujutsu_kaisen.capability.data.SorcererDataHandler;
import radon.jujutsu_kaisen.entity.base.DomainExpansionEntity;
import radon.jujutsu_kaisen.network.PacketHandler;
import radon.jujutsu_kaisen.network.packet.s2c.SyncSorcererDataS2CPacket;

import java.util.ArrayList;
import java.util.List;

public class ClosedDomainExpansionEntity extends DomainExpansionEntity {
    private static final EntityDataAccessor<Integer> DATA_RADIUS = SynchedEntityData.defineId(ClosedDomainExpansionEntity.class, EntityDataSerializers.INT);
    private static final float STRENGTH = 50.0F;

    private final List<Block> blocks = new ArrayList<>();


    public ClosedDomainExpansionEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ClosedDomainExpansionEntity(LivingEntity owner, DomainExpansion ability, List<Block> blocks, int radius, int duration) {
        super(JJKEntities.CLOSED_DOMAIN_EXPANSION.get(), owner, ability, duration);

        this.moveTo(owner.getX(), owner.getY() - (double) (radius / 2), owner.getZ());

        this.blocks.addAll(blocks);

        this.entityData.set(DATA_RADIUS, radius);

        owner.getCapability(SorcererDataHandler.INSTANCE).ifPresent(cap -> {
            AttributeInstance attribute = this.getAttribute(Attributes.MAX_HEALTH);

            if (attribute != null) {
                attribute.setBaseValue(STRENGTH * cap.getGrade().getPower());
                this.setHealth(this.getMaxHealth());
            }
        });
    }

    @Override
    public AABB getBounds() {
        int radius = this.getRadius();
        return new AABB(this.getX() - radius, this.getY(), this.getZ() - radius,
                this.getX() + radius, this.getY() + radius, this.getZ() + radius);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.entityData.define(DATA_RADIUS, 0);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        for (Tag tag : pCompound.getList("blocks", Tag.TAG_COMPOUND)) {
            BlockState state = NbtUtils.readBlockState(this.level.holderLookup(Registries.BLOCK), (CompoundTag) tag);
            this.blocks.add(state.getBlock());
        }
        this.entityData.set(DATA_RADIUS, pCompound.getInt("radius"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        ListTag blocksTag = new ListTag();

        for (Block block : this.blocks) {
            blocksTag.add(NbtUtils.writeBlockState(block.defaultBlockState()));
        }
        pCompound.put("blocks", blocksTag);

        pCompound.putInt("radius", this.getRadius());
    }

    private int getRadius() {
        return this.entityData.get(DATA_RADIUS);
    }

    private List<DomainExpansionEntity> getDomainsInside() {
        List<DomainExpansionEntity> entities = new ArrayList<>();

        AABB bounds = this.getBounds();

        for (DomainExpansionEntity entity : this.level.getEntitiesOfClass(DomainExpansionEntity.class, bounds)) {
            if (this.isInsideBarrier(entity)) {
                entities.add(entity);
            }
        }
        return entities;
    }

    @Override
    public boolean isInsideBarrier(Entity entity) {
        int radius = this.getRadius();
        BlockPos center = this.blockPosition().offset(0, radius / 2, 0);
        BlockPos relative = entity.blockPosition().subtract(center);
        return relative.distSqr(Vec3i.ZERO) < radius * radius;
    }

    private void createBarrier(Entity owner) {
        if (this.blocks == null) return;

        int radius = this.getRadius();
        BlockPos center = this.blockPosition().offset(0, radius / 2, 0);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);

                    if (distance < radius && distance >= radius - 1) {
                        BlockPos pos = center.offset(x, y, z);
                        BlockState state = this.level.getBlockState(pos);

                        int delay = radius - (pos.getY() - center.getY());

                        owner.getCapability(SorcererDataHandler.INSTANCE).ifPresent(cap -> {
                            cap.delayTickEvent(() -> {
                                if (!this.isRemoved()) {
                                    BlockState original = null;

                                    if (this.level.getBlockEntity(pos) instanceof DomainBlockEntity be) {
                                        original = be.getOriginal();
                                    }

                                    Block block = this.blocks.get(this.random.nextInt(this.blocks.size()));
                                    owner.level.setBlock(pos, block.defaultBlockState(),
                                            Block.UPDATE_ALL | Block.UPDATE_SUPPRESS_DROPS);

                                    if (this.level.getBlockEntity(pos) instanceof DomainBlockEntity be) {
                                        be.create(this.uuid, this.duration, original == null ? state : original);
                                    }
                                }
                            }, delay);
                        });
                    }
                }
            }
        }
    }

    @Override
    public void warn() {
        LivingEntity owner = this.getOwner();

        if (owner != null) {
            AABB bounds = this.getBounds();

            for (Entity entity : this.level.getEntities(this, bounds, this::isAffected)) {
                entity.getCapability(SorcererDataHandler.INSTANCE).ifPresent(cap -> cap.onInsideDomain(this));
            }
        }
        this.warned = true;
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        int radius = this.getRadius() * 2;
        return EntityDimensions.fixed(radius, radius);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void aiStep() {}

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return this.isAlive();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        Entity entity = pSource.getDirectEntity();

        if (!this.isRemovable() || entity != null && this.isInsideBarrier(entity)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    private void doSureHitEffect(@NotNull LivingEntity owner) {
        AABB bounds = this.getBounds();

        for (Entity entity : this.level.getEntities(this, bounds, this::isAffected)) {
            this.ability.onHitEntity(this, owner, entity);
        }

        int radius = this.getRadius();
        BlockPos center = this.blockPosition().offset(0, radius / 2, 0);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);

                    if (distance < radius - 1) {
                        BlockPos pos = center.offset(x, y, z);
                        this.ability.onHitBlock(this, owner, pos);
                    }
                }
            }
        }
    }

    private boolean checkSureHitEffect() {
        List<DomainExpansionEntity> domains = this.getDomainsInside();

        for (DomainExpansionEntity domain : domains) {
            if (domain.getOwner() == this.getOwner()) continue;

            int radius = this.getRadius();

            if (domain instanceof OpenDomainExpansionEntity && this.getStrength() < domain.getStrength() * 2) {
                if (this.isRemovable()) {
                    this.discard();
                }
                return false;
            } else if (domain.getStrength() >= this.getStrength()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();

        if (!this.level.isClientSide) {
            LivingEntity owner = this.getOwner();

            if (owner != null) {
                owner.getCapability(SorcererDataHandler.INSTANCE).ifPresent(cap -> {
                    cap.setBurnout(DomainExpansion.BURNOUT);

                    if (owner instanceof ServerPlayer player) {
                        PacketHandler.sendToClient(new SyncSorcererDataS2CPacket(cap.serializeNBT()), player);
                    }
                });
            }
        }
    }

    @Override
    public boolean isRemovable() {
        int radius = this.getRadius();
        return this.getTime() >= radius * 2;
    }

    @Override
    public void tick() {
        this.refreshDimensions();

        LivingEntity owner = this.getOwner();

        if (owner != null) {
            if (!this.level.isClientSide) {
                if (this.checkSureHitEffect()) {
                    if (!this.warned || this.getTime() % 5 == 0) {
                        this.warn();
                    }
                    if (this.isRemovable()) {
                        this.doSureHitEffect(owner);
                    }
                }

                if (this.getTime() == 0) {
                    this.createBarrier(owner);
                } else if (this.isRemovable() && !this.isInsideBarrier(owner)) {
                    //this.discard();
                }
            }
        }
        super.tick();
    }
}
