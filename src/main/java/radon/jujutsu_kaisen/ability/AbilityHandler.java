package radon.jujutsu_kaisen.ability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import radon.jujutsu_kaisen.capability.data.SorcererDataHandler;
import radon.jujutsu_kaisen.effect.JJKEffects;

import java.util.concurrent.atomic.AtomicReference;

public class AbilityHandler {
    public static Ability.Status trigger(LivingEntity owner, Ability ability) {
        AtomicReference<Ability.Status> result = new AtomicReference<>(Ability.Status.SUCCESS);

        if (!owner.hasEffect(JJKEffects.UNLIMITED_VOID.get())) {
            if (ability.getActivationType(owner) == Ability.ActivationType.INSTANT) {
                Ability.Status status;

                if ((status = ability.checkTriggerable(owner)) == Ability.Status.SUCCESS) {
                    MinecraftForge.EVENT_BUS.post(new AbilityTriggerEvent(owner, ability));
                    ability.run(owner);
                }
                result.set(status);
            } else if (ability.getActivationType(owner) == Ability.ActivationType.TOGGLED) {
                owner.getCapability(SorcererDataHandler.INSTANCE).ifPresent(cap -> {
                    Ability.Status status;

                    if ((status = ability.checkToggleable(owner)) == Ability.Status.SUCCESS || cap.hasToggled(ability)) {
                        MinecraftForge.EVENT_BUS.post(new AbilityTriggerEvent(owner, ability));
                        cap.toggle(owner, ability);
                    }
                    result.set(status);
                });
            } else if (ability.getActivationType(owner) == Ability.ActivationType.CHANNELED) {
                owner.getCapability(SorcererDataHandler.INSTANCE).ifPresent(cap -> {
                    Ability.Status status;

                    if ((status = ability.checkChannelable(owner)) == Ability.Status.SUCCESS || cap.isChanneling(ability)) {
                        cap.channel(owner, ability);
                    }
                    result.set(status);
                });
            }
        }
        return result.get();
    }
}
