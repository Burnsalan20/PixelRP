package com.pixelrp.core.mechanic.LoggerMechanic.commands;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.LoggerMechanic.LoggerMechanic;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.PagedView;
import com.pixelrp.core.mechanic.LoggerMechanic.utils.log.LogManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class PageCommand implements CommandExecutor {

    private static final Text KEY_PAGE = Text.of("page");

    public static CommandSpec createSpec(LogManager logMan) {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.integer(KEY_PAGE)
                )
                .permission(null)
                .executor(new PageCommand(logMan))
                .build();
    }

    private final LogManager logMan;

    public PageCommand(LogManager logMan) {
        this.logMan = logMan;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        LoggerMechanic lm = (LoggerMechanic) PixelRP.instance.mechanicManager.getMechanic(1);
        PagedView view = lm.getViews().get(src.getName());
        if (view == null) {
            src.sendMessage(Text.of(TextColors.RED, "You didn't request any logs recently."));
            return CommandResult.empty();
        }

        Optional<Text> pageText = view.getPage(args.<Integer>getOne(KEY_PAGE).get());

        if (!pageText.isPresent()) {
            src.sendMessage(Text.of(TextColors.RED, "The specified page does not exist."));
            return CommandResult.empty();
        }

        src.sendMessage(pageText.get());
        return CommandResult.success();
    }

}
