package radon.jujutsu_kaisen.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import radon.jujutsu_kaisen.JujutsuKaisen;
import radon.jujutsu_kaisen.data.sorcerer.ISorcererData;
import radon.jujutsu_kaisen.data.JJKAttachmentTypes;
import radon.jujutsu_kaisen.data.sorcerer.BindingVow;
import radon.jujutsu_kaisen.data.sorcerer.Pact;
import radon.jujutsu_kaisen.damage.JJKDamageSources;

public class PactEventHandler {
    @Mod.EventBusSubscriber(modid = JujutsuKaisen.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent event) {
            LivingEntity victim = event.getEntity();

            if (victim.level().isClientSide) return;

            DamageSource source = event.getSource();

            if (!(source.getEntity() instanceof LivingEntity attacker)) return;

            // Check for BindingVow.RECOIL
            if (source.is(JJKDamageSources.JUJUTSU)) {
                ISorcererData data = attacker.getData(JJKAttachmentTypes.SORCERER);

                if (data.hasBindingVow(BindingVow.RECOIL)) {
                    attacker.hurt(JJKDamageSources.self(victim), event.getAmount() * 0.25F);
                    event.setAmount(event.getAmount() * 1.25F);
                }
            }
        }

        @SubscribeEvent
        public static void onLivingAttack(LivingAttackEvent event) {
            LivingEntity victim = event.getEntity();

            if (victim.level().isClientSide) return;

            DamageSource source = event.getSource();

            if (!(source.getEntity() instanceof LivingEntity attacker)) return;

            while (attacker instanceof TamableAnimal tamable && tamable.isTame()) {
                attacker = tamable.getOwner();

                if (attacker == null) return;
            }

            while (victim instanceof TamableAnimal tamable && tamable.isTame()) {
                victim = tamable.getOwner();

                if (victim == null) return;
            }

            // Check for Pact.INVULNERABILITY
            ISorcererData victimData = victim.getData(JJKAttachmentTypes.SORCERER);
            ISorcererData attackerData = attacker.getData(JJKAttachmentTypes.SORCERER);

            if (victimData == null || attackerData == null) return;

            if (victimData.hasPact(attacker.getUUID(), Pact.INVULNERABILITY) && attackerData.hasPact(victim.getUUID(), Pact.INVULNERABILITY)) {
                event.setCanceled(true);
            }
        }
    }
}