package net.zuperz.aurora.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProoerties {
    public static final FoodProperties RESIN = new FoodProperties.Builder().nutrition(4).saturationModifier(1f).build();

    public static final FoodProperties RAPTOR_CHOPS = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.7F)
            .effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F)
            .build();

    public static final FoodProperties COOKED_RAPTOR_CHOPS = new FoodProperties.Builder().nutrition(20).saturationModifier(2.0F).build();
}
