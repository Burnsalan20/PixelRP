package com.pixelrp.core.mechanic.harvestMechanic.utils;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.utils.ChatUtil;
import com.pixelrp.core.utils.database.Database;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class HarvestManager {

    public Database database;
    public Map<String, Resource> custom_Resources = new HashMap<>();
    public Map<ResourceLocation, Resource> active_Resources;
    private Map<String, Location> resourceEdits = new HashMap<>();

    private boolean isSchedulerRunning = false;
    private Map<RespawnTask, Integer> respawnTasks = new HashMap<>();

    public HarvestManager(){
        database = new Database("resources");
        active_Resources = new HashMap<>();
        try {
            active_Resources = (Map<ResourceLocation, Resource>) database.loadDataBase();
        } catch (Exception e) { }

        initializeResources();
        startRespawnTimer();
    }

    private void initializeResources(){
        custom_Resources.put("IRON_ORE", new Resource("IRON_ORE", 50, ChatUtil.DARK_GRAY + "Iron Ore", ChatUtil.GRAY + "An ore block with specs of iron."));
        custom_Resources.put("GOLD_ORE", new Resource("GOLD_ORE", 50, ChatUtil.GOLD + "Gold Ore", ChatUtil.GRAY + "An ore block with specs of gold."));
        custom_Resources.put("DIAMOND_ORE", new Resource("DIAMOND_ORE", 50, ChatUtil.AQUA + "Diamond Ore", ChatUtil.GRAY + "An ore block with specs of diamond."));
        custom_Resources.put("LAPIS_ORE", new Resource("LAPIS_ORE", 50, ChatUtil.DARK_BLUE + "Lapis Ore", ChatUtil.GRAY + "An ore block with specs of lapis."));
    }

    public Map<ResourceLocation, Resource> getActiveResources(){
        return active_Resources;
    }

    public Map<RespawnTask, Integer> getRespawnTasks(){
        return respawnTasks;
    }

    public void addToRespawnTimer(Player player, Resource resource, ResourceLocation location){
        respawnTasks.put(new RespawnTask(location, player.getUniqueId().toString()), resource.getRespawn());
    }

    public boolean canBreakResource(Player player, ResourceLocation location){
        for(RespawnTask task : respawnTasks.keySet()){
            if(task.location.equals(location) && task.uuid.equals(player.getUniqueId().toString())) return false;
        }

        return true;
    }

    public void startRespawnTimer(){
        final Task.Builder taskBuilder = Sponge.getScheduler().createTaskBuilder();

        taskBuilder.interval(1, TimeUnit.SECONDS).execute(task ->
        {
            if(respawnTasks.size() != 0){
                RespawnTask temp = null;
                for(RespawnTask respawnTask : respawnTasks.keySet()){

                    int time = respawnTasks.get(respawnTask);
                    time = time - 1;
                    respawnTasks.put(respawnTask, time);
                    temp = respawnTask;
                }
                if(respawnTasks.get(temp) <= 0){
                    respawnTasks.remove(temp);
                }
            }
        }).submit(PixelRP.instance);
    }

    public boolean isEditingResources(Player player){
        return resourceEdits.containsKey(player.getUniqueId().toString());
    }

    public Map<String, Location> getResourceEdits(){
        return resourceEdits;
    }

    public boolean setResourceBlock(String resourceName, Location location){
        System.out.println("ResourceBlock called");
        for(String res : custom_Resources.keySet()){
            System.out.println(res + ": Resource block listed");
            if(res.equalsIgnoreCase(resourceName)){
                active_Resources.put(new ResourceLocation("world", location.getBlockX(), location.getBlockY(), location.getBlockZ()), custom_Resources.get(res));
                save();
                return true;
            }
        }
        return false;
    }

    public void respawnBlock(Location location, Resource resource){
        String itemString = resource.getItemType().toUpperCase();
        ItemType itemType = Sponge.getRegistry().getType(ItemType.class, itemString).get();
        location.setBlockType(itemType.getBlock().get());
    }

    public ItemStack newItemStack(ItemType itemType, Text customName, List<Text> itemLore){
        ItemStack stack = ItemStack.builder().itemType(itemType).build();
        stack.offer(Keys.DISPLAY_NAME, customName);
        stack.offer(Keys.ITEM_LORE, itemLore);
        //if(stack.get(Keys.ITEM_LORE).isPresent())


        return stack;
    }

    public ResourceLocation toResourceLocation(Location location){
        return new ResourceLocation("world", location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public Location getResourceLocation(ResourceLocation resLocation){
        Optional<World> world = Sponge.getServer().getWorld(resLocation.world);
        Location<World> location = new Location<World>(world.get(), resLocation.blockX, resLocation.blockY, resLocation.blockY);
        return location;
    }

    public boolean isSimilarLocations(ResourceLocation loc1, ResourceLocation loc2){
        if(loc1.world.equals(loc2.world)
                && loc1.blockX == loc2.blockX
                && loc1.blockY == loc2.blockY
                && loc1.blockZ == loc2.blockZ) return true;

        return false;
    }

    public void setToBedrock(Location<World> blockLoc) {
        blockLoc.setBlockType(BlockTypes.BEDROCK);
    }

    public void save(){
        this.database.saveDataBase(active_Resources);
    }
}
