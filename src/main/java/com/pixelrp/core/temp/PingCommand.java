package com.pixelrp.core.temp;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class PingCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            Player player = (Player) src;
            player.sendMessage(Text.of("[Player Ping] Pong!"));
        }
        else if(src instanceof ConsoleSource) {
            src.sendMessage(Text.of("[Console Ping] Pong!"));
        }
        else if(src instanceof CommandBlockSource) {
            src.sendMessage(Text.of("[CommandBlock Ping] Pong!"));
        }
        return CommandResult.success();
    }
}
