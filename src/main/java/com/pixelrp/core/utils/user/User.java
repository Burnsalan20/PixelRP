package com.pixelrp.core.utils.user;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.UUID;

public class User {

    private String uuid;

    public User(String uuid){
        this.uuid = uuid;
    }

    public Optional<Player> getPlayer(){
        return Sponge.getServer().getPlayer(UUID.fromString(uuid));
    }
}
