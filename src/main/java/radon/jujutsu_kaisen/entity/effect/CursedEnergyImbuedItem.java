package radon.jujutsu_kaisen.entity.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import radon.jujutsu_kaisen.ability.JJKAbilities;
import radon.jujutsu_kaisen.capability.data.ISorcererData;
import radon.jujutsu_kaisen.capability.data.SorcererDataHandler;
import radon.jujutsu_kaisen.client.particle.CursedEnergyParticle;
import radon.jujutsu_kaisen.client.particle.ParticleColors;
import radon.jujutsu_kaisen.entity.JJKEntities;
import radon.jujutsu_kaisen.item.JJKItems;
import radon.jujutsu_kaisen.util.EntityUtil;
import radon.jujutsu_kaisen.util.HelperMethods;
import radon.jujutsu_kaisen.util.RotationUtil;

public class CursedEnergyImbuedItem extends ItemEntity {
    private static final double SPEED = 3.0D;

    public CursedEnergyImbuedItem(EntityType<? extends ItemEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public CursedEnergyImbuedItem(Entity owner, ItemStack stack) {
        super(JJKEntities.CURSED_ENERGY_IMBUED_ITEM.get(), owner.level());

        this.setThrower(owner.getUUID());
        this.setItem(stack);

        Vec3 look = RotationUtil.getTargetAdjustedLookAngle(owner);
        Vec3 spawn = new Vec3(owner.getX(), owner.getEyeY() - (this.getBbHeight() / 2.0F), owner.getZ())
                .add(look);
        this.setPos(spawn.x, spawn.y, spawn.z);

        EntityUtil.applyOffset(this, look);

        this.setDeltaMovement(this.getLookAngle().scale(SPEED));
    }

    @Override
    public void tick() {
        super.tick();

        if (!(this.level() instanceof ServerLevel level)) return;

        Entity owner = this.getOwner();

        if (owner != null) {
            for (int i = 0; i < 12; i++) {
                double x = this.getX() + (HelperMethods.RANDOM.nextDouble() - 0.5D) * (this.getBbWidth() * 1.5F);
                double y = this.getY() + HelperMethods.RANDOM.nextDouble() * this.getBbHeight();
                double z = this.getZ() + (HelperMethods.RANDOM.nextDouble() - 0.5D) * (this.getBbWidth() * 1.5F);
                double speed = (this.getBbHeight() * 0.3F) * HelperMethods.RANDOM.nextDouble();
                level.sendParticles(new CursedEnergyParticle.CursedEnergyParticleOptions(ParticleColors.getCursedEnergyColor(owner), this.getBbWidth() * 0.5F,
                        0.2F, 6), x, y, z, 0, 0.0D, speed, 0.0D, 1.0D);
            }
        }
    }
}
