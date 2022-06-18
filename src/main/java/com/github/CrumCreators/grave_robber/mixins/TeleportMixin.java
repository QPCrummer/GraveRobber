package com.github.CrumCreators.grave_robber.mixins;


import com.github.CrumCreators.grave_robber.Main;
import eu.pb4.graves.ui.GraveListGui;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Mixin(GraveListGui.class)
public abstract class TeleportMixin {

    @ModifyArg(method = "lambda$getElement$0(Leu/pb4/graves/grave/Grave;ILeu/pb4/sgui/api/ClickType;Lnet/minecraft/screen/slot/SlotActionType;)V", at = @At(value = "INVOKE", target = "me/lucko/fabric/api/permissions/v0/Permissions.check(Lnet/minecraft/entity/Entity;Ljava/lang/String;I)Z"), index = 2)
    private int injected(int x) {
        if (Main.perms) {
            return 0;
        } else {
            return 3;
        }
    }


    @Redirect(method = "lambda$getElement$0(Leu/pb4/graves/grave/Grave;ILeu/pb4/sgui/api/ClickType;Lnet/minecraft/screen/slot/SlotActionType;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V"))
    private void redirectTeleport(ServerPlayerEntity player, ServerWorld world, double x, double y, double z, float yaw, float pitch) {
        Objects.requireNonNull(player.getServer()).execute(() -> player.sendMessage(Text.of("Teleporting in " + Main.time + " Seconds")));
        thred.schedule(() -> {
            Objects.requireNonNull(player.getServer()).execute(() -> player.sendMessage(Text.of("Teleporting to " + x +", "+(y+Main.height)+ ", "+z)));
            Objects.requireNonNull(player.getServer()).execute(() -> player.teleport(world, x, y + Main.height, z, yaw, pitch));
            Objects.requireNonNull(player.getServer()).execute(() -> player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.MASTER, 1f, 1f));
            Objects.requireNonNull(player.getServer()).execute(() -> player.setInvulnerable(true));
            try {
                Thread.sleep(Main.invtime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(player.getServer()).execute(() -> player.setInvulnerable(false));
        }, Main.time, TimeUnit.SECONDS);
    }
    private static final ScheduledExecutorService thred = Executors.newScheduledThreadPool(1);
}
