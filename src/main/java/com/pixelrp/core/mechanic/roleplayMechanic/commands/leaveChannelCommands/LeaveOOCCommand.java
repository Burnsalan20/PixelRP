package com.pixelrp.core.mechanic.roleplayMechanic.commands.leaveChannelCommands;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.roleplayMechanic.commands.ListCommand;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.card.CardManager;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.channel.Channels;
import com.pixelrp.core.utils.ChatUtil;
import com.pixelrp.core.utils.user.User;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

public class LeaveOOCCommand implements CommandExecutor {

    public static CommandSpec createSpec(){

        CommandSpec spec = CommandSpec.builder()
                .executor(new LeaveOOCCommand())
                .arguments()
                .build();

        return spec;
    }

    public LeaveOOCCommand(){}

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            User user = PixelRP.instance.userManager.getUser((Player) src);
            user.setOocActive(false);

            if(user.getChannel() == Channels.OOC) user.setChannel(Channels.LOOC);
            PixelRP.instance.userManager.saveDatabase();
            String message = ChatUtil.GREEN + "No longer looking at " + ChatUtil.GRAY + "Global OOC";
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
        }
        return CommandResult.empty();
    }
}
