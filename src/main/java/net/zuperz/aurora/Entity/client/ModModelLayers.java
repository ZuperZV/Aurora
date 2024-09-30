package net.zuperz.aurora.Entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.zuperz.aurora.aurora;

public class ModModelLayers {
    public static final ModelLayerLocation CHAIN = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "chain"), "main");

    public static final ModelLayerLocation VOID_WALKER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "void_walker"), "main");
}