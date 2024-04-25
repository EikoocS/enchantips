package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.ItemStackAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.TooltipHelper;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackAccess {

    @Inject(method = "getTooltip(Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipType;)Ljava/util/List;", at = @At(value = "INVOKE", target = "java/util/List.add (Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void enchantipsAddExtraTooltips(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, List<Text> list, MutableText mutableText) {
        ItemStack t = (ItemStack)(Object)this;

        if(t.getItem().isEnchantable(t) && ModOption.SHOW_ENCHANTABILITY.getValue()) {
            list.add(TooltipHelper.buildEnchantability(t.getItem().getEnchantability()));
        }

        Integer cost = t.get(DataComponentTypes.REPAIR_COST);
        if(!(t.getItem() instanceof EnchantedBookItem) && cost != null && ModOption.SHOW_REPAIRCOST.getValue()) {
            list.add(TooltipHelper.buildRepairCost(cost));
        }
    }
}
