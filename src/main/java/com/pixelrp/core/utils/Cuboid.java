package com.pixelrp.core.utils;

import jdk.nashorn.internal.ir.Block;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;

public class Cuboid {

    private String world;
    private Location l1, l2;

    public Cuboid(String world, Location l1, Location l2){
        this.world = world;
        this.l1 = l1;
        this.l2 = l2;


    }

    public boolean isPlayerInRegion(Player player){
        return inRegion(player.getLocation());
    }

    public boolean inRegion(Location loc){
        double x1, z1, x2, z2;
        x1 = l1.getX();
        z1 = l1.getZ();

        x2 = l2.getX();
        z2 = l2.getZ();

        double xP = loc.getX();
        double zP = loc.getZ();

        if(((x1 <= xP && xP <= x2) || x1 >= xP && xP >= x2) && ((z1 <= zP && zP <= z2) || z1 >= zP && zP >= z2)){
            return true;
        } else {
            return false;
        }
    }

    public BlockState[] getCornerBlocks(){
        BlockState[] res = new BlockState[8];
        World w = Sponge.getServer().getWorld(world).get();

        double x1, y1, z1, x2, y2, z2;
        x1 = l1.getX();
        y1 = l1.getY();
        z1 = l1.getZ();

        x2 = l2.getX();
        y2 = l2.getY();
        z2 = l2.getZ();

        res[0] = w.getBlock((int)x1, (int)y1, (int)z1);
        res[1] = w.getBlock((int)x1, (int)y1, (int)z2);
        res[2] = w.getBlock((int)x1, (int)y2, (int)z1);
        res[3] = w.getBlock((int)x1, (int)y2, (int)z2);
        res[4] = w.getBlock((int)x2, (int)y1, (int)z1);
        res[5] = w.getBlock((int)x2, (int)y1, (int)z2);
        res[6] = w.getBlock((int)x2, (int)y2, (int)z1);
        res[7] = w.getBlock((int)x2, (int)y2, (int)z2);
        return res;
    }

    public Location[] getCornerLocations(){
        Location[] res = new Location[8];
        World w = Sponge.getServer().getWorld(world).get();

        double x1, y1, z1, x2, y2, z2;
        x1 = l1.getX();
        y1 = l1.getY();
        z1 = l1.getZ();

        x2 = l2.getX();
        y2 = l2.getY();
        z2 = l2.getZ();

        res[0] = w.getLocation((int)x1, (int)y1, (int)z1);
        res[1] = w.getLocation((int)x1, (int)y1, (int)z2);
        res[2] = w.getLocation((int)x1, (int)y2, (int)z1);
        res[3] = w.getLocation((int)x1, (int)y2, (int)z2);
        res[4] = w.getLocation((int)x2, (int)y1, (int)z1);
        res[5] = w.getLocation((int)x2, (int)y1, (int)z2);
        res[6] = w.getLocation((int)x2, (int)y2, (int)z1);
        res[7] = w.getLocation((int)x2, (int)y2, (int)z2);
        return res;
    }

    public List<BlockState> getBlocks(){
        List<BlockState> tmpBlocks = new ArrayList<>();
        int x1 = l1.getBlockX();
        int y1 = l1.getBlockY();
        int z1 = l1.getBlockZ();
        int x2 = l2.getBlockX();
        int y2 = l2.getBlockY();
        int z2 = l2.getBlockZ();
        for (int x = x1;x < x2; x++) {
            for (int y = y1;y < y2; y++) {
                for (int z = z1;z < z2; z++) {
                    tmpBlocks.add(Sponge.getServer().getWorld(world).get().getBlock(x,y,z));
                }
            }
        }
        return tmpBlocks;
    }

    public boolean intersects(Cuboid cuboid){
        for(Location loc : getCornerLocations()){
            if(inRegion(loc)) return true;
        }
        return false;
    }

    public boolean intersectsDimension(int aMin, int aMax, int bMin, int bMax)
    {
        return aMin <= bMax && aMax >= bMin;
    }

    public BlockState getHighestBlock(Location loc){
        World w = Sponge.getServer().getWorld(world).get();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        int i = 255;
        while(i>0){
            if(new Location(w, x, i, z).getBlock().getType() != ItemTypes.AIR)
                return new Location(w, x, i, z).add(0,1,0).getBlock();
            i--;
        }
        return new Location(w, x, 1, z).getBlock();
    }

}
