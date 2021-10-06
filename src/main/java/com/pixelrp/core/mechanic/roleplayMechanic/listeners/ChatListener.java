package com.pixelrp.core.mechanic.roleplayMechanic.listeners;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.channel.ChannelManager;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.channel.Channels;
import com.pixelrp.core.utils.ChatUtil;
import com.pixelrp.core.utils.MessageUtil;
import com.pixelrp.core.utils.user.User;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {



    @Listener
    public void onChatListener(MessageChannelEvent.Chat event){
        event.setCancelled(true);
        if(event.getSource() instanceof Player){
            Player  player = (Player) event.getSource();
            String message = event.getRawMessage().toPlain();
            User user = PixelRP.instance.userManager.getUser(player);
            String name = "None";
            if(user.getCard() != null) name = user.getCard().getName();

            if(user.getChannel() == Channels.OOC){
                String prefix_Channel = ChatUtil.DARK_GREEN + "[" + ChatUtil.DARK_GRAY + "OOC" + ChatUtil.DARK_GREEN + "] ";
                String prefix_Rank = ChatUtil.GOLD + "[" + ChatUtil.DARK_GRAY + "Rank" + ChatUtil.GOLD + "] ";
                String player_Name = ChatUtil.GRAY + player.getName();
                String formatted_Message = prefix_Channel + prefix_Rank + player_Name + ": " + ChatUtil.WHITE + message;

                for(Player p : Sponge.getServer().getOnlinePlayers()){
                    User u = PixelRP.instance.userManager.getUser(p);
                    if(user.isOocActive())
                        p.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(formatted_Message));
                }
            }
            else if(user.getChannel() == Channels.LOOC){
                String prefix_Channel = ChatUtil.GREEN + "[" + ChatUtil.DARK_GRAY + "LOOC" + ChatUtil.GREEN + "] ";
                String prefix_Rank = ChatUtil.GOLD + "[" + ChatUtil.DARK_GRAY + "Rank" + ChatUtil.GOLD + "] ";
                String player_Name = ChatUtil.GRAY + player.getName();
                String formatted_Message = prefix_Channel + prefix_Rank + player_Name + ": " + ChatUtil.WHITE + message;

                for(Entity entity : player.getNearbyEntities(20)){
                    if(entity instanceof Player) ((Player) entity).sendMessage(TextSerializers.FORMATTING_CODE.deserialize(formatted_Message));
                }

            }
            else if(user.getChannel() == Channels.SOOC){
                String prefix_Channel = ChatUtil.RED + "[" + ChatUtil.DARK_RED + "SOOC" + ChatUtil.RED + "] ";
                String prefix_Rank = ChatUtil.GOLD + "[" + ChatUtil.DARK_GRAY + "Rank" + ChatUtil.GOLD + "] ";
                String player_Name = ChatUtil.GRAY + player.getName();
                String formatted_Message = prefix_Channel + prefix_Rank + player_Name + ": " + ChatUtil.WHITE + message;

                for(Entity entity : player.getNearbyEntities(50)){
                    if(entity instanceof Player) ((Player) entity).sendMessage(TextSerializers.FORMATTING_CODE.deserialize(formatted_Message));
                }

            }
            else if (user.getChannel() == Channels.RP) {
                if(message.startsWith("*")){
                    message = MessageUtil.replaceIfEven(message, "\"", "\"" + ChatUtil.YELLOW);
                    message = MessageUtil.replaceIfOdd(message, "\"", ChatUtil.WHITE + "\"");

                    String prefix = "";
                    String card_Name = ChatUtil.YELLOW + name + " ";
                    String formatted_Message = prefix + card_Name + message.replaceAll("\\*", "");

                    for(Entity entity : player.getNearbyEntities(20)){
                        if(entity instanceof Player) ((Player) entity).sendMessage(TextSerializers.FORMATTING_CODE.deserialize(formatted_Message));
                    }

                 //   player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(ChatUtil.YELLOW + "NONE " + message.replaceAll("\\*", "")));
                } else {
                    String prefix = "";
                    String card_Name = ChatUtil.YELLOW + name;
                    String formatted_Message = prefix + card_Name + ": " + ChatUtil.WHITE + "''" + message + "''";

                    for(Entity entity : player.getNearbyEntities(20)){
                        if(entity instanceof Player) ((Player) entity).sendMessage(TextSerializers.FORMATTING_CODE.deserialize(formatted_Message));
                    }

              //      player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(ChatUtil.YELLOW + "NONE: " + ChatUtil.WHITE + "''" + message + "''"));
                }

            }
            else if (user.getChannel() == Channels.SRP) {
                if(message.startsWith("*")){
                    message = MessageUtil.replaceIfEven(message, "\"", "\"" + ChatUtil.YELLOW);
                    message = MessageUtil.replaceIfOdd(message, "\"", ChatUtil.WHITE + "\"");

                    String prefix_Channel = ChatUtil.DARK_RED + "[" + ChatUtil.RED + ChatUtil.BOLD + "Yell" + ChatUtil.DARK_RED + "] ";
                    String card_Name = ChatUtil.YELLOW + name + " ";
                    String formatted_Message = prefix_Channel + card_Name + message.replaceAll("\\*", "");

                    for(Entity entity : player.getNearbyEntities(50)){
                        if(entity instanceof Player) ((Player) entity).sendMessage(TextSerializers.FORMATTING_CODE.deserialize(formatted_Message));
                    }

             //       player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(ChatUtil.YELLOW + "NONE " + message.replaceAll("\\*", "")));
                } else {
                    String prefix_Channel = ChatUtil.DARK_RED + "[" + ChatUtil.RED + ChatUtil.BOLD + "Yell" + ChatUtil.DARK_RED + "] ";
                    String card_Name = ChatUtil.YELLOW + " " + name;
                    String formatted_Message = prefix_Channel + card_Name + ": " + ChatUtil.WHITE + "''" + message + "''";

                    for(Entity entity : player.getNearbyEntities(50)){
                        if(entity instanceof Player) ((Player) entity).sendMessage(TextSerializers.FORMATTING_CODE.deserialize(formatted_Message));
                    }

              //      player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(ChatUtil.YELLOW + "NONE: " + ChatUtil.WHITE + "''" + message + "''"));
                }

            }
        }
    }
}
