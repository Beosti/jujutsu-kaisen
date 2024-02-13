package radon.jujutsu_kaisen.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import radon.jujutsu_kaisen.JujutsuKaisen;
import radon.jujutsu_kaisen.network.packet.c2s.*;
import radon.jujutsu_kaisen.network.packet.s2c.*;

@Mod.EventBusSubscriber(modid = JujutsuKaisen.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PacketHandler {
    @SubscribeEvent
    public static void onRegisterPayloadHandler(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar(JujutsuKaisen.MOD_ID)
                .versioned("1.2.3");

        // Clietbound packets
        registrar.play(SyncSorcererDataS2CPacket.IDENTIFIER, SyncSorcererDataS2CPacket::new, handler -> handler
                .client(SyncSorcererDataS2CPacket::handle)
                .server(SyncSorcererDataS2CPacket::handle));
        registrar.play(SyncTenShadowsDataS2CPacket.IDENTIFIER, SyncTenShadowsDataS2CPacket::new, handler -> handler
                .client(SyncTenShadowsDataS2CPacket::handle)
                .server(SyncTenShadowsDataS2CPacket::handle));
        registrar.play(SyncProjectionSorceryDataS2CPacket.IDENTIFIER, SyncProjectionSorceryDataS2CPacket::new, handler -> handler
                .client(SyncProjectionSorceryDataS2CPacket::handle)
                .server(SyncProjectionSorceryDataS2CPacket::handle));
        registrar.play(SyncCurseManipulationDataS2CPacket.IDENTIFIER, SyncCurseManipulationDataS2CPacket::new, handler -> handler
                .client(SyncCurseManipulationDataS2CPacket::handle)
                .server(SyncCurseManipulationDataS2CPacket::handle));
        registrar.play(CameraShakeS2CPacket.IDENTIFIER, CameraShakeS2CPacket::new, handler -> handler
                .client(CameraShakeS2CPacket::handle)
                .server(CameraShakeS2CPacket::handle));
        registrar.play(SetOverlayMessageS2CPacket.IDENTIFIER, SetOverlayMessageS2CPacket::new, handler -> handler
                .client(SetOverlayMessageS2CPacket::handle)
                .server(SetOverlayMessageS2CPacket::handle));
        registrar.play(SetCostS2CPacket.IDENTIFIER, SetCostS2CPacket::new, handler -> handler
                .client(SetCostS2CPacket::handle)
                .server(SetCostS2CPacket::handle));
        registrar.play(SyncVisualDataS2CPacket.IDENTIFIER, SyncVisualDataS2CPacket::new, handler -> handler
                .client(SyncVisualDataS2CPacket::handle)
                .server(SyncVisualDataS2CPacket::handle));
        registrar.play(ReceiveVisualDataS2CPacket.IDENTIFIER, ReceiveVisualDataS2CPacket::new, handler -> handler
                .client(ReceiveVisualDataS2CPacket::handle)
                .server(ReceiveVisualDataS2CPacket::handle));
        registrar.play(ScreenFlashS2CPacket.IDENTIFIER, ScreenFlashS2CPacket::new, handler -> handler
                .client(ScreenFlashS2CPacket::handle)
                .server(ScreenFlashS2CPacket::handle));
        registrar.play(SyncMouthS2CPacket.IDENTIFIER, SyncMouthS2CPacket::new, handler -> handler
                .client(SyncMouthS2CPacket::handle)
                .server(SyncMouthS2CPacket::handle));
        registrar.play(AddChantS2CPacket.IDENTIFIER, AddChantS2CPacket::new, handler -> handler
                .client(AddChantS2CPacket::handle)
                .server(AddChantS2CPacket::handle));
        registrar.play(RemoveChantS2CPacket.IDENTIFIER, RemoveChantS2CPacket::new, handler -> handler
                .client(RemoveChantS2CPacket::handle)
                .server(RemoveChantS2CPacket::handle));

        // Serverbound packets
        registrar.play(TriggerAbilityC2SPacket.IDENTIFIER, TriggerAbilityC2SPacket::new, handler -> handler
                .client(TriggerAbilityC2SPacket::handle)
                .server(TriggerAbilityC2SPacket::handle));
        registrar.play(OpenInventoryCurseC2SPacket.IDENTIFIER, OpenInventoryCurseC2SPacket::new, handler -> handler
                .client(OpenInventoryCurseC2SPacket::handle)
                .server(OpenInventoryCurseC2SPacket::handle));
        registrar.play(CommandableTargetC2SPacket.IDENTIFIER, CommandableTargetC2SPacket::new, handler -> handler
                .client(CommandableTargetC2SPacket::handle)
                .server(CommandableTargetC2SPacket::handle));
        registrar.play(JumpInputListenerC2SPacket.IDENTIFIER, JumpInputListenerC2SPacket::new, handler -> handler
                .client(JumpInputListenerC2SPacket::handle)
                .server(JumpInputListenerC2SPacket::handle));
        registrar.play(RightClickInputListenerC2SPacket.IDENTIFIER, RightClickInputListenerC2SPacket::new, handler -> handler
                .client(RightClickInputListenerC2SPacket::handle)
                .server(RightClickInputListenerC2SPacket::handle));
        registrar.play(SetSizeC2SPacket.IDENTIFIER, SetSizeC2SPacket::new, handler -> handler
                .client(SetSizeC2SPacket::handle)
                .server(SetSizeC2SPacket::handle));
        registrar.play(RequestCostC2SPacket.IDENTIFIER, RequestCostC2SPacket::new, handler -> handler
                .client(RequestCostC2SPacket::handle)
                .server(RequestCostC2SPacket::handle));
        registrar.play(SetTojiBountyC2SPacket.IDENTIFIER, SetTojiBountyC2SPacket::new, handler -> handler
                .client(SetTojiBountyC2SPacket::handle)
                .server(SetTojiBountyC2SPacket::handle));
        registrar.play(KuchisakeOnnaAnswerC2SPacket.IDENTIFIER, KuchisakeOnnaAnswerC2SPacket::new, handler -> handler
                .client(KuchisakeOnnaAnswerC2SPacket::handle)
                .server(KuchisakeOnnaAnswerC2SPacket::handle));
        registrar.play(ShadowInventoryTakeC2SPacket.IDENTIFIER, ShadowInventoryTakeC2SPacket::new, handler -> handler
                .client(ShadowInventoryTakeC2SPacket::handle)
                .server(ShadowInventoryTakeC2SPacket::handle));
        registrar.play(CurseSummonC2SPacket.IDENTIFIER, CurseSummonC2SPacket::new, handler -> handler
                .client(CurseSummonC2SPacket::handle)
                .server(CurseSummonC2SPacket::handle));
        registrar.play(SetCopiedC2SPacket.IDENTIFIER, SetCopiedC2SPacket::new, handler -> handler
                .client(SetCopiedC2SPacket::handle)
                .server(SetCopiedC2SPacket::handle));
        registrar.play(SetAbsorbedC2SPacket.IDENTIFIER, SetAbsorbedC2SPacket::new, handler -> handler
                .client(SetAbsorbedC2SPacket::handle)
                .server(SetAbsorbedC2SPacket::handle));
        registrar.play(RequestVisualDataC2SPacket.IDENTIFIER, RequestVisualDataC2SPacket::new, handler -> handler
                .client(RequestVisualDataC2SPacket::handle)
                .server(RequestVisualDataC2SPacket::handle));
        registrar.play(SetDomainSizeC2SPacket.IDENTIFIER, SetDomainSizeC2SPacket::new, handler -> handler
                .client(SetDomainSizeC2SPacket::handle)
                .server(SetDomainSizeC2SPacket::handle));
        registrar.play(QuestionCreatePactC2SPacket.IDENTIFIER, QuestionCreatePactC2SPacket::new, handler -> handler
                .client(QuestionCreatePactC2SPacket::handle)
                .server(QuestionCreatePactC2SPacket::handle));
        registrar.play(AddBindingVowC2SPacket.IDENTIFIER, AddBindingVowC2SPacket::new, handler -> handler
                .client(AddBindingVowC2SPacket::handle)
                .server(AddBindingVowC2SPacket::handle));
        registrar.play(RemoveBindingVowC2SPacket.IDENTIFIER, RemoveBindingVowC2SPacket::new, handler -> handler
                .client(RemoveBindingVowC2SPacket::handle)
                .server(RemoveBindingVowC2SPacket::handle));
        registrar.play(UnlockAbilityC2SPacket.IDENTIFIER, UnlockAbilityC2SPacket::new, handler -> handler
                .client(UnlockAbilityC2SPacket::handle)
                .server(UnlockAbilityC2SPacket::handle));
        registrar.play(AddChantC2SPacket.IDENTIFIER, AddChantC2SPacket::new, handler -> handler
                .client(AddChantC2SPacket::handle)
                .server(AddChantC2SPacket::handle));
        registrar.play(RemoveChantC2SPacket.IDENTIFIER, RemoveChantC2SPacket::new, handler -> handler
                .client(RemoveChantC2SPacket::handle)
                .server(RemoveChantC2SPacket::handle));
        registrar.play(ChangeOutputC2SPacket.IDENTIFIER, ChangeOutputC2SPacket::new, handler -> handler
                .client(ChangeOutputC2SPacket::handle)
                .server(ChangeOutputC2SPacket::handle));
        registrar.play(UncopyAbilityC2SPacket.IDENTIFIER, UncopyAbilityC2SPacket::new, handler -> handler
                .client(UncopyAbilityC2SPacket::handle)
                .server(UncopyAbilityC2SPacket::handle));
        registrar.play(QuestionRemovePactC2SPacket.IDENTIFIER, QuestionRemovePactC2SPacket::new, handler -> handler
                .client(QuestionRemovePactC2SPacket::handle)
                .server(QuestionRemovePactC2SPacket::handle));
        registrar.play(NyoiStaffSummonLightningC2SPacket.IDENTIFIER, NyoiStaffSummonLightningC2SPacket::new, handler -> handler
                .client(NyoiStaffSummonLightningC2SPacket::handle)
                .server(NyoiStaffSummonLightningC2SPacket::handle));
        registrar.play(SetCursedEnergyColorC2SPacket.IDENTIFIER, SetCursedEnergyColorC2SPacket::new, handler -> handler
                .client(SetCursedEnergyColorC2SPacket::handle)
                .server(SetCursedEnergyColorC2SPacket::handle));
        registrar.play(UntriggerAbilityC2SPacket.IDENTIFIER, UntriggerAbilityC2SPacket::new, handler -> handler
                .client(UntriggerAbilityC2SPacket::handle)
                .server(UntriggerAbilityC2SPacket::handle));
        registrar.play(ClearChantsC2SPacket.IDENTIFIER, ClearChantsC2SPacket::new, handler -> handler
                .client(ClearChantsC2SPacket::handle)
                .server(ClearChantsC2SPacket::handle));
    }

    public static <MSG extends CustomPacketPayload> void broadcast(MSG message) {
        PacketDistributor.ALL.noArg().send(message);
    }

    public static <MSG extends CustomPacketPayload> void sendToServer(MSG message) {
        PacketDistributor.SERVER.noArg().send(message);
    }

    public static <MSG extends CustomPacketPayload> void sendToClient(MSG message, ServerPlayer player) {
        PacketDistributor.PLAYER.with(player).send(message);
    }
}
