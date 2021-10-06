package com.pixelrp.core.mechanic.roleplayMechanic.commands;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.card.Card;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.card.CardManager;
import com.pixelrp.core.utils.Helper;
import com.pixelrp.core.utils.user.User;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class NewCommand implements CommandExecutor {

    private static final Text KEY_NAME = Text.of("name");

    public static CommandSpec createSpec(CardManager cm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new NewCommand(cm))
                .arguments(GenericArguments.remainingJoinedStrings(KEY_NAME))
                .build();

        return spec;
    }

    private final CardManager cm;

    public NewCommand(CardManager cm){ this.cm = cm; }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = (String) args.getOne(KEY_NAME).get();
        if(src instanceof Player){
            Player player = (Player) src;
            User user = PixelRP.instance.userManager.getUser(player);
            Card newCard = new Card(player.getUniqueId().toString(), Helper.getCurrentTimeStamp(), name, 5, "");
            user.setCard(newCard);
            user.getCards().add(newCard);
            PixelRP.instance.userManager.saveDatabase();
//            cm.createCard(newCard);

            src.sendMessage(Text.of(TextColors.GREEN, "New card Created [" + name + "]"));
        } else src.sendMessage(Text.of(TextColors.RED, "You must be a player to create a card."));
        return CommandResult.empty();
    }
}
