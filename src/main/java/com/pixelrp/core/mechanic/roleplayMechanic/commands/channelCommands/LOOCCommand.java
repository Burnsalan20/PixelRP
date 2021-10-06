package com.pixelrp.core.mechanic.roleplayMechanic.commands.channelCommands;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.channel.Channels;
import com.pixelrp.core.utils.ChatUtil;
import com.pixelrp.core.utils.user.User;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

public class LOOCCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            User user = PixelRP.instance.userManager.getUser(player);
            user.setChannel(Channels.LOOC);
            PixelRP.instance.userManager.saveDatabase();
            String message = ChatUtil.GREEN + "Now talking in " + ChatUtil.GRAY + "Local OOC";
            player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
        }
        return CommandResult.empty();
    }
}
