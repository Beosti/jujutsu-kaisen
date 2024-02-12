package radon.jujutsu_kaisen.data.curse_manipulation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import radon.jujutsu_kaisen.JujutsuKaisen;
import radon.jujutsu_kaisen.damage.JJKDamageSources;
import radon.jujutsu_kaisen.data.JJKAttachmentTypes;
import radon.jujutsu_kaisen.data.projection_sorcery.IProjectionSorceryData;
import radon.jujutsu_kaisen.data.projection_sorcery.ProjectionSorceryData;
import radon.jujutsu_kaisen.network.PacketHandler;
import radon.jujutsu_kaisen.network.packet.s2c.SyncCurseManipulationDataS2CPacket;
import radon.jujutsu_kaisen.network.packet.s2c.SyncProjectionSorceryDataS2CPacket;
import radon.jujutsu_kaisen.network.packet.s2c.SyncTenShadowsDataS2CPacket;

@Mod.EventBusSubscriber(modid = JujutsuKaisen.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CurseManipulationDataProvider {
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity owner = event.getEntity();

        if (!owner.hasData(JJKAttachmentTypes.CURSE_MANIPULATION)) return;

        ICurseManipulationData data = owner.getData(JJKAttachmentTypes.CURSE_MANIPULATION);
        data.tick(owner);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        ICurseManipulationData data = player.getData(JJKAttachmentTypes.CURSE_MANIPULATION);
        data.init(player);

        if (!player.level().isClientSide) return;

        PacketHandler.sendToClient(new SyncCurseManipulationDataS2CPacket(data.serializeNBT()), (ServerPlayer) player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ICurseManipulationData data = player.getData(JJKAttachmentTypes.CURSE_MANIPULATION);
        PacketHandler.sendToClient(new SyncCurseManipulationDataS2CPacket(data.serializeNBT()), player);
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, ICurseManipulationData> {
        @Override
        public ICurseManipulationData read(CompoundTag tag) {
            ICurseManipulationData data = new CurseManipulationData();
            data.deserializeNBT(tag);
            return data;
        }

        @Override
        public CompoundTag write(ICurseManipulationData attachment) {
            return attachment.serializeNBT();
        }
    }
}