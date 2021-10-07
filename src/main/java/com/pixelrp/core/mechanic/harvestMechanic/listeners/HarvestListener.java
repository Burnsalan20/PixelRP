package com.pixelrp.core.mechanic.harvestMechanic.listeners;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.harvestMechanic.utils.HarvestManager;
import com.pixelrp.core.mechanic.harvestMechanic.utils.Resource;
import com.pixelrp.core.mechanic.harvestMechanic.utils.ResourceLocation;
import com.pixelrp.core.mechanic.townMechanic.TownMechanic;
import jdk.nashorn.internal.ir.Block;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.mutable.item.BlockItemData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HarvestListener {

    private HarvestManager hm;

    public HarvestListener(HarvestManager hm){
        this.hm = hm;
    }

    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event){
 //       TownMechanic townMechanic = (TownMechanic) PixelRP.instance.mechanicManager.getMechanic(4);
        if(event.getSource() instanceof Player){
            Player player = (Player) event.getSource();

            if(hm.isEditingResources(player)){
                Optional<ItemStack> hand = player.getItemInHand(HandTypes.MAIN_HAND);
                if(hand.get().getType().equals(ItemTypes.GOLDEN_PICKAXE)){
                    for(Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                        Optional<Location<World>> l = transaction.getFinal().getLocation();
                        hm.getResourceEdits().put(player.getUniqueId().toString(), l.get());
                        player.sendMessage(Text.of(TextColors.AQUA, "You have selected a block!"));
                        event.setCancelled(true);
                    }
                }
            }

            for(Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                BlockState blockState = transaction.getFinal().getState();
                Optional<Location<World>> l = transaction.getFinal().getLocation();

                ResourceLocation loc1 = hm.toResourceLocation(l.get());
                for(ResourceLocation loc2 : hm.getActiveResources().keySet()){
                    if(hm.isSimilarLocations(loc1, loc2)){
                        if(hm.canBreakResource(player, loc2)){
                            Resource resource = hm.getActiveResources().get(loc2);
                            event.setCancelled(true);
                            hm.addToRespawnTimer(player, resource, loc2);
                            List<Text> lore = new ArrayList<Text>();
                            lore.add(TextSerializers.FORMATTING_CODE.deserialize(resource.getDesc()));
                            String itemString = resource.getItemType().toUpperCase();
                            player.getInventory().offer(hm.newItemStack(Sponge.getRegistry().getType(ItemType.class, itemString).get(), TextSerializers.FORMATTING_CODE.deserialize(resource.getName()), lore));
                        } else {
                            event.setCancelled(true);
                            player.sendMessage(Text.of("You have already harvested from this deposit. Please wait."));
                        }
                    }
                }

            }
        }
    }

}
