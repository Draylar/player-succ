package draylar.playersucc.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends BlockEntity {

    private HopperBlockEntityMixin(BlockEntityType<?> type) {
        super(type);
    }

    @Inject(
            method = "getInventoryAt(Lnet/minecraft/world/World;DDD)Lnet/minecraft/inventory/Inventory;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"
            ), cancellable = true
    )
    private static void injectInventoryComponents(World world, double x, double y, double z, CallbackInfoReturnable<Inventory> info) {
        List<Entity> list = world.getEntities((Entity) null, new Box(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), entity -> entity instanceof PlayerEntity);

        if (!list.isEmpty()) {
            PlayerEntity playerEntity = (PlayerEntity) list.get(world.random.nextInt(list.size()));
            info.setReturnValue(playerEntity.inventory);
        }
    }
}
