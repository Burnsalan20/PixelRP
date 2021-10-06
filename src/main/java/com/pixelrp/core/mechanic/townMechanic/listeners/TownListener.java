package com.pixelrp.core.mechanic.townMechanic.listeners;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.townMechanic.utils.Town;
import com.pixelrp.core.mechanic.townMechanic.utils.TownManager;
import com.pixelrp.core.utils.Helper;
import com.pixelrp.core.utils.user.User;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

public class TownListener {

    private TownManager tm;

    public TownListener(TownManager tm){
        this.tm = tm;
    }

    @Listener
    public void onPlayerEnterClaim(MoveEntityEvent event){
        if(event.getSource() instanceof Player){
            Player player = (Player) event.getSource();
            User user = PixelRP.instance.userManager.getUser(player);
            if(tm.getTownAt(player.getLocation()) != null){
                Town town = tm.getTownAt(player.getLocation());
                tm.enter(player, town);
            }
        }
    }

    @Listener
    public void onPlayerLeaveClaim(MoveEntityEvent event){
        if(event.getSource() instanceof Player){
            Player player = (Player) event.getSource();
            User user = PixelRP.instance.userManager.getUser(player);
            if(tm.getTownByName(tm.getVisitations().get(player.getUniqueId().toString())) != null){
                Town town = tm.getTownByName(tm.getVisitations().get(player.getUniqueId().toString()));
                if(!tm.getTownCuboid(town).isPlayerInRegion(player)) {
                    tm.leave(player, town);
                    player.sendMessage(Text.of("Leaving " + town.getName() + "!"));
                }
            } else {
            }
        }
    }

    @Listener
    public void onPlayerBuildClaim(ChangeBlockEvent event){
        if(event.getSource() instanceof Player){
            Player player = (Player) event.getSource();
            User user = PixelRP.instance.userManager.getUser(player);
            for(Transaction<BlockSnapshot> transaction : event.getTransactions()){
                Optional<Location<World>> l = transaction.getFinal().getLocation();

                if(!player.hasPermission("rcplugin.admin")){
                    if(tm.getTownAt(l.get()) != null){
                        Town town = tm.getTownAt(l.get());
                        if(!town.getCitizens().containsKey(player.getUniqueId().toString())){
                            player.sendMessage(Text.of("You do not have permission to build here."));
                            event.setCancelled(true);
                        } else {
                            if(!town.getCitizens().get(player.getUniqueId().toString()).canBuild()){
                                player.sendMessage(Text.of("You do not have permission to build here."));
                                event.setCancelled(true);
                            }
                        }
                    } else {
                        player.sendMessage(Text.of("You do not have permission to build here."));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @Listener
    public void onPlayerInteractClaim(InteractBlockEvent event){
        if(event.getSource() instanceof Player){
            Player player = (Player) event.getSource();
            User user = PixelRP.instance.userManager.getUser(player);
            Optional<Location<World>> location = event.getTargetBlock().getLocation();
            Optional<ItemStack> hand = player.getItemInHand(HandTypes.MAIN_HAND);

            if(hand.get().getType().equals(ItemTypes.STICK) && tm.getTownAt(location.get()) != null){
                Town town = tm.getTownAt(location.get());
                tm.info(player, town);
            }
            else if(hand.get().getType().equals(ItemTypes.GOLDEN_SHOVEL)){
                if(tm.isInEditMode(player)){
                    Town town = tm.getPlayerTown(player);
                    if(!Helper.isSneaking(player)){
                        tm.getEditors().get(player.getUniqueId().toString()).setLocation1(location.get());
                        player.sendMessage(Text.of(TextColors.AQUA, "Set location 1"));
                        event.setCancelled(true);
                    } else {
                        tm.getEditors().get(player.getUniqueId().toString()).setLocation2(location.get());
                        player.sendMessage(Text.of(TextColors.AQUA, "Set location 2"));
                        event.setCancelled(true);
                    }
                } else {
                    player.sendMessage(Text.of(TextColors.AQUA, "You must be in edit mode to use this tool here."));
                    event.setCancelled(true);
                }
            }
        }
    }
}
