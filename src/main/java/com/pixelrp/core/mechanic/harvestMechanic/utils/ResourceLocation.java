package com.pixelrp.core.mechanic.harvestMechanic.utils;

import java.io.Serializable;

public class ResourceLocation implements Serializable {

    public String world;
    public int blockX, blockY, blockZ;

    public ResourceLocation(String world, int blockX, int blockY, int blockZ){
        this.world = world;

        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }
}
