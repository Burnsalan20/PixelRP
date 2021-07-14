package com.pixelrp.core.temp;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class TestCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        String x = args.<String>getOne(Text.of("x")).get();
        String y = args.<String>getOne(Text.of("y")).get();

        System.out.println("You have selected X:" + x + " Y:" + y);

        return CommandResult.success();
    }
}
