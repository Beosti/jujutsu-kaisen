package radon.jujutsu_kaisen.ability.curse_manipulation;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import radon.jujutsu_kaisen.JujutsuKaisen;
import radon.jujutsu_kaisen.ability.base.Ability;
import radon.jujutsu_kaisen.ability.JJKAbilities;
import radon.jujutsu_kaisen.data.sorcerer.ISorcererData;
import radon.jujutsu_kaisen.data.JJKAttachmentTypes;
import radon.jujutsu_kaisen.data.sorcerer.AbsorbedCurse;
import radon.jujutsu_kaisen.data.sorcerer.JujutsuType;
import radon.jujutsu_kaisen.item.CursedSpiritOrbItem;
import radon.jujutsu_kaisen.item.JJKItems;
import radon.jujutsu_kaisen.util.DamageUtil;
import radon.jujutsu_kaisen.util.EntityUtil;

public class CurseAbsorption extends Ability implements Ability.IToggled {
    @Override
    public boolean isScalable(LivingEntity owner) {
        return false;
    }

    @Override
    public boolean shouldTrigger(PathfinderMob owner, @Nullable LivingEntity target) {
        if (target == null) return false;

        ISorcererData data = target.getData(JJKAttachmentTypes.SORCERER);

        if (data == null) return false;

        return data.getType() == JujutsuType.CURSE;
    }

    @Override
    public ActivationType getActivationType(LivingEntity owner) {
        return ActivationType.TOGGLED;
    }

    private static boolean canAbsorb(LivingEntity owner, LivingEntity target) {
        ISorcererData ownerData = owner.getData(JJKAttachmentTypes.SORCERER);
        ISorcererData targetData = target.getData(JJKAttachmentTypes.SORCERER);

        if (ownerData == null || targetData == null) return false;

        return (targetData.getType() == JujutsuType.CURSE && (!(target instanceof TamableAnimal tamable) || !tamable.isTame())) &&
                (ownerData.getExperience() / targetData.getExperience() >= 2 || target.isDeadOrDying());
    }

    @Override
    public void run(LivingEntity owner) {

    }

    @Override
    public float getCost(LivingEntity owner) {
        return 0;
    }

    @Override
    public void onEnabled(LivingEntity owner) {

    }

    @Override
    public void onDisabled(LivingEntity owner) {

    }

    private static void check(LivingEntity victim, DamageSource source) {
        if (!DamageUtil.isMelee(source)) return;

        if (!(source.getEntity() instanceof LivingEntity attacker)) return;

        if (!canAbsorb(attacker, victim)) return;

        ISorcererData data = attacker.getData(JJKAttachmentTypes.SORCERER);


        if (!data.hasToggled(JJKAbilities.CURSE_ABSORPTION.get())) return;

        ISorcererData victimData = victim.getData(JJKAttachmentTypes.SORCERER);

        if (victimData == null) return;

        attacker.swing(InteractionHand.MAIN_HAND, true);

        ItemStack stack = new ItemStack(JJKItems.CURSED_SPIRIT_ORB.get());

        if (victim instanceof Player player) {
            CursedSpiritOrbItem.setAbsorbed(stack, new AbsorbedCurse(victim.getName(), victim.getType(), victimData.serializeNBT(), player.getGameProfile()));
        } else {
            CursedSpiritOrbItem.setAbsorbed(stack, new AbsorbedCurse(victim.getName(), victim.getType(), victimData.serializeNBT()));
        }

        if (attacker instanceof Player player) {
            player.addItem(stack);
        } else {
            attacker.setItemSlot(EquipmentSlot.MAINHAND, stack);
        }
        EntityUtil.makePoofParticles(victim);

        if (!(victim instanceof Player)) {
            victim.discard();
        } else {
            victim.kill();
        }
    }

    @Mod.EventBusSubscriber(modid = JujutsuKaisen.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent event) {
            check(event.getEntity(), event.getSource());
        }

        @SubscribeEvent
        public static void onLivingDeath(LivingDeathEvent event) {
            check(event.getEntity(), event.getSource());
        }
    }
}
