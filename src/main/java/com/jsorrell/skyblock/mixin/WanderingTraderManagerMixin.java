package com.jsorrell.skyblock.mixin;

import carpet.CarpetServer;
import com.jsorrell.skyblock.SkyBlockSettings;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WanderingTraderManager;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(WanderingTraderManager.class)
public class WanderingTraderManagerMixin {
    private final String message = """
        [{
        	"text": "<流浪商人来了！！！>",
        	"color": "gold",
        	"hoverEvent": {
        		"action": "show_text",
        		"contents": [{
        			"text": "坐标："
        		}, {
        			"text": "[%d, %d, %d]",
        			"color": "green"
        		}]
        	},
        	"clickEvent": {
                "action": "run_command",
                "value": "/highlightWaypoint %d %d %d"
            }
        }]
        """;

    @Inject(method = "trySpawn", at = @At(value = "RETURN", ordinal = 3), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onTrySpawn(
        ServerWorld world,
        CallbackInfoReturnable<Boolean> cir,
        PlayerEntity playerEntity,
        BlockPos blockPos,
        int i,
        PointOfInterestStorage pointOfInterestStorage,
        Optional<BlockPos> optional,
        BlockPos blockPos2,
        BlockPos blockPos3,
        WanderingTraderEntity wanderingTraderEntity
    ) {
        if (!SkyBlockSettings.wanderingTraderAlert) {
            return;
        }
        int x = (int) wanderingTraderEntity.getX();
        int y = (int) wanderingTraderEntity.getY();
        int z = (int) wanderingTraderEntity.getZ();
        CarpetServer.minecraft_server.getPlayerManager().getPlayerList().forEach(
            player -> player.sendMessage(Text.Serializer.fromJson(message.formatted(x, y, z, x, y, z)), false)
        );
    }
}
