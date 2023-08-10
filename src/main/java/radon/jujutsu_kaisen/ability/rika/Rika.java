package radon.jujutsu_kaisen.ability.rika;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import org.jetbrains.annotations.Nullable;
import radon.jujutsu_kaisen.ability.misc.Summon;
import radon.jujutsu_kaisen.capability.data.SorcererDataHandler;
import radon.jujutsu_kaisen.capability.data.sorcerer.SorcererGrade;
import radon.jujutsu_kaisen.entity.JJKEntities;
import radon.jujutsu_kaisen.entity.curse.RikaEntity;

import java.util.concurrent.atomic.AtomicBoolean;

public class Rika extends Summon<RikaEntity> {
    public Rika() {
        super(RikaEntity.class);
    }

    @Override
    public boolean shouldTrigger(PathfinderMob owner, @Nullable LivingEntity target) {
        return true;
    }

    @Override
    public void run(LivingEntity owner) {

    }

    @Override
    public EntityType<RikaEntity> getType() {
        return JJKEntities.RIKA.get();
    }

    @Override
    protected boolean isTamed(LivingEntity owner) {
        AtomicBoolean result = new AtomicBoolean();

        owner.getCapability(SorcererDataHandler.INSTANCE).ifPresent(cap ->
                result.set(cap.getGrade().ordinal() >= SorcererGrade.GRADE_1.ordinal()));
        return result.get();
    }

    @Override
    public float getCost(LivingEntity owner) {
        return 0.0F;
    }

    @Override
    protected RikaEntity summon(int index, LivingEntity owner) {
        return new RikaEntity(owner, this.isTamed(owner));
    }

    @Override
    public int getCooldown() {
        return 30 * 20;
    }

    @Override
    public boolean isTechnique() {
        return true;
    }
}
