package com.pixelrp.core.mechanic.roleplayMechanic.commands;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.card.Card;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.card.CardManager;
import com.pixelrp.core.utils.user.User;
import org.spongepowered.api.Sponge;
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

import java.util.UUID;

public class ListCommand implements CommandExecutor {



    public static CommandSpec createSpec(CardManager cm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new ListCommand(cm))
                .arguments()
                .build();

        return spec;
    }

    private final CardManager cm;

    public ListCommand(CardManager cm){ this.cm = cm;}

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            User user = PixelRP.instance.userManager.getUser(player);

            if(user.getCards().size() > 0){
                src.sendMessage(Text.of(TextColors.YELLOW, "=============== " + player.getName() + "'s Card List ==============="));
                for(int i = 0; i < user.getCards().size(); i++){
                    Card card = user.getCards().get(i);
                    src.sendMessage(Text.of(TextColors.GREEN, "[", TextColors.GRAY, i, TextColors.GREEN, "] ", TextColors.GRAY, card.getName()));
                }
            } else src.sendMessage(Text.of(TextColors.YELLOW, "You have no cards."));
        }
        return CommandResult.empty();
    }
}
