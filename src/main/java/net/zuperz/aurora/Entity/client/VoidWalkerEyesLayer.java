package net.zuperz.aurora.Entity.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zuperz.aurora.aurora;

@OnlyIn(Dist.CLIENT)
public class VoidWalkerEyesLayer<T extends Monster> extends EyesLayer<T, VoidWalkerModel<T>> {
    private static final RenderType VOID_WALKER_EYES = RenderType.eyes(ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID,"textures/entity/void_walker/void_walker_eyes.png"));

    public VoidWalkerEyesLayer(RenderLayerParent<T, VoidWalkerModel<T>> p_116964_) {
        super(p_116964_);
    }

    @Override
    public RenderType renderType() {
        return VOID_WALKER_EYES;
    }
}