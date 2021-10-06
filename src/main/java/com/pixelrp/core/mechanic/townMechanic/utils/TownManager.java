package com.pixelrp.core.mechanic.townMechanic.utils;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.utils.Cuboid;
import com.pixelrp.core.utils.Rect2;
import com.pixelrp.core.utils.database.Database;
import com.pixelrp.core.utils.user.User;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.*;

public class TownManager {

    private Database townDatabase;
    private List<Town> towns;
    private Map<String, String> visitations = new HashMap<>();
    private Map<String, Rect2> edit_Mode = new HashMap<>();

    public TownManager(){
        townDatabase = new Database("towns");
        towns = new ArrayList<>();

        try {
            towns = (ArrayList<Town>) townDatabase.loadDataBase();
        } catch (Exception e){}
    }

    public void create(Player player, String name, Location l1, Location l2){
        String uuid = player.getUniqueId().toString();
        if(!intersectsTown("world", l1, l2)){
            Town newTown = new Town(name, l1, l2);
            newTown.getCitizens().put(uuid,
                    new Citizen(uuid, TownRole.MAYOR, true));

            towns.add(newTown);
            this.save();
        }
    }

    public void edit(Player player, Town town){
        Rect2 rect2 = edit_Mode.get(player.getUniqueId().toString());
        town.clearLocation();

        if(!intersectsTown("world", rect2.getLocation1(), rect2.getLocation2())){
            town.setLoc1(rect2.getLocation1());
            town.setLoc2(rect2.getLocation2());

            edit_Mode.remove(player.getUniqueId().toString());

            this.save();
        } else {
            // Tell the player the claim was intersecting another and restore the previous locations
            player.sendMessage(Text.of(TextColors.AQUA, "Your selection overlaps another claim."));
            town.restoreLocation();
        }
    }

    public void delete(Town town){
        towns.remove(town);

        this.save();
    }

    public void enter(Player player, Town town){
        if(!visitations.containsKey(player.getUniqueId().toString())) {
            visitations.put(player.getUniqueId().toString(), town.getName());
            player.sendMessage(Text.of("Welcome to " + town.getName() + "!"));
        }
    }

    public void leave(Player player, Town town){
        if(visitations.containsKey(player.getUniqueId().toString())){
            visitations.remove(player.getUniqueId().toString());
        }
    }

    public void addCitizen(Player player, Town town){
        String uuid = player.getUniqueId().toString();
        if(!town.getCitizens().containsKey(uuid)){
            town.getCitizens().put(uuid,
                    new Citizen(uuid, TownRole.CITIZEN, false));
        }
        save();
    }

    public void removeCitizen(Player player, Town town){
        if(town.getCitizens().containsKey(player.getUniqueId().toString()))
            town.getCitizens().remove(player.getUniqueId().toString());

        save();
    }

    public void broadCastToTown(Town town, String value){
        for(String uuid : town.getCitizens().keySet()){
            Optional<Player> player = Sponge.getServer().getPlayer(UUID.fromString(uuid));
            player.get().sendMessage(Text.of(value));
        }
    }

    public boolean isPlayerInATown(Player player){
        if(towns.size() == 0) return false;

        for(Town town : towns){
            if(town.getCitizens().containsKey(player.getUniqueId().toString())) return true;
        }

        return false;
    }

    public void info(Player player, Town town){
        player.sendMessage(Text.of(TextColors.AQUA, "=============== ", town.getName(), "==============="));
        player.sendMessage(Text.of(TextColors.AQUA, "Mayor: ", Sponge.getServer().getPlayer(UUID.fromString(getMayor(town).getUniqueId().toString())).get().getName()));
        player.sendMessage(Text.of(TextColors.AQUA, "Name: ", town.getName()));
        player.sendMessage(Text.of(TextColors.AQUA, "Description: ", town.getDescription()));
        player.sendMessage(Text.of(TextColors.AQUA, "Date Created: ", town.getDateCreated()));
        player.sendMessage(Text.of(TextColors.AQUA, "Last Active: ", town.getLastActive()));
        if(town.getCitizens().size() > 0){
            player.sendMessage(Text.of(TextColors.AQUA, "Citizens ", "[", town.getCitizens().size() , "]:"));
            for(String uuid : town.getCitizens().keySet()){
                Optional<Player> p = Sponge.getServer().getPlayer(UUID.fromString(uuid));
                player.sendMessage(Text.of(TextColors.AQUA, "- ", p.get().getName()));
            }
        }
        if(getTownVisitorIDs(town).size() > 0){
            player.sendMessage(Text.of(TextColors.AQUA, "Visitors ", "[", getTownVisitorIDs(town).size() , "]:"));
            for(String uuid : getTownVisitorIDs(town)){
                Optional<Player> p = Sponge.getServer().getPlayer(UUID.fromString(uuid));
                player.sendMessage(Text.of(TextColors.AQUA, "- ", p.get().getName()));
            }
        }
    }

    public Town getTownByName(String name){
        if(towns.size() > 0){
            for(Town town : towns){
                if(town.getName().equals(name)) return town;
            }
        }

        return null;
    }

    public Town getPlayerTown(Player player){
        if(towns.size() == 0) return null;

        for(Town town : towns){
            if(town.getCitizens().containsKey(player.getUniqueId().toString())) return town;
        }

        return null;
    }

    public void changeMayor(Player newMayor, Town town){
        if(getMayor(town) != null){
            Player oldMayor = getMayor(town);
            town.getCitizens().get(oldMayor.getUniqueId().toString()).setTownRole(TownRole.CITIZEN);
            town.getCitizens().get(newMayor.getUniqueId().toString()).setTownRole(TownRole.MAYOR);
        } else town.getCitizens().get(newMayor.getUniqueId().toString()).setTownRole(TownRole.MAYOR);
    }

    public Player getMayor(Town town){
        for(String uuid : town.getCitizens().keySet()){
            Optional<Player> player = Sponge.getServer().getPlayer(UUID.fromString(uuid));
            if(isMayor(player.get(), town)) return player.get();
        }
        return null;
    }

    public boolean isMayor(Player player, Town town){
        if(town.getCitizens().containsKey(player.getUniqueId().toString()) &&
        town.getCitizens().get(player.getUniqueId().toString()).getTownRole() == TownRole.MAYOR)
            return true;

        return false;
    }

    public void changeGymLeader(Player newGL, Town town){
        if(getGymLeader(town) != null){
            Player oldGL = getGymLeader(town);
            town.getCitizens().get(oldGL.getUniqueId().toString()).setTownRole(TownRole.CITIZEN);
            town.getCitizens().get(newGL.getUniqueId().toString()).setTownRole(TownRole.GYM_LEADER);
        } else town.getCitizens().get(newGL.getUniqueId().toString()).setTownRole(TownRole.GYM_LEADER);
    }

    public boolean isGymLeader(Player player, Town town){
        if(town.getCitizens().containsKey(player.getUniqueId().toString()) &&
                town.getCitizens().get(player.getUniqueId().toString()).getTownRole() == TownRole.GYM_LEADER)
            return true;

        return false;
    }

    public Player getGymLeader(Town town){
        for(String uuid : town.getCitizens().keySet()){
            Optional<Player> player = Sponge.getServer().getPlayer(UUID.fromString(uuid));
            if(isGymLeader(player.get(), town)) return player.get();
        }
        return null;
    }

    public void toggleStewart(Player player, Player newStewart, Town town){
        if(town.getCitizens().containsKey(newStewart.getUniqueId().toString())){
            if(town.getCitizens().get(newStewart.getUniqueId().toString()).getTownRole() != TownRole.STEWARD){
                town.getCitizens().get(newStewart.getUniqueId().toString()).setTownRole(TownRole.STEWARD);

                player.sendMessage(Text.of(TextColors.AQUA, "You have been given the job of Stewart to ", newStewart.getName(), "!"));
                newStewart.sendMessage(Text.of(TextColors.AQUA, "You are now a Stewart!"));
            } else {
                town.getCitizens().get(newStewart.getUniqueId().toString()).setTownRole(TownRole.CITIZEN);
                player.sendMessage(Text.of(TextColors.AQUA, "You have demoted ", newStewart.getName(), " to citizen."));
                newStewart.sendMessage(Text.of(TextColors.AQUA, "You are no longer a Stewart."));
            }
        }
    }

    public boolean isStewart(Player player, Town town){
        if(town.getCitizens().containsKey(player.getUniqueId().toString()) &&
                town.getCitizens().get(player.getUniqueId().toString()).getTownRole() == TownRole.STEWARD)
            return true;

        return false;
    }

    public void setBuilder(Player owner, Player player, Town town){
        if(town.getCitizens().containsKey(player.getUniqueId().toString())) {
            if(!town.getCitizens().get(player.getUniqueId().toString()).canBuild()){
                town.getCitizens().get(player.getUniqueId().toString()).setBuilder(true);
                owner.sendMessage(Text.of(TextColors.AQUA, "You have granted build permissions to ", player.getName()));
                player.sendMessage(Text.of(TextColors.AQUA, "You have been granted build permissions in ", town.getName()));
            } else {
                town.getCitizens().get(player.getUniqueId().toString()).setBuilder(false);
                owner.sendMessage(Text.of(TextColors.AQUA, "You have revoked build permissions for ", player.getName()));
                player.sendMessage(Text.of(TextColors.AQUA, "Your build permissions have been revoked in ", town.getName()));
            }
        }
        save();
    }

    public boolean isBuilder(Player player, Town town){
        return town.getCitizens().containsKey(player.getUniqueId().toString()) &&
                town.getCitizens().get(player.getUniqueId().toString()).canBuild();
    }

    public Town getTownAt(Location loc){
        World world = Sponge.getServer().getWorld("world").get();
        for(Town town : towns){
            Location l1 = new Location(world, town.getLoc1()[0], town.getLoc1()[1], town.getLoc1()[2]);
            Location l2 = new Location(world, town.getLoc2()[0], town.getLoc2()[1], town.getLoc2()[2]);
            Cuboid cuboid = new Cuboid("world", l1, l2);
            if(cuboid.inRegion(loc)) return town;
        }
        return null;
    }

    //uuid,townname
    public Map<String, String> getVisitations(){
        return visitations;
    }

    public List<String> getTownVisitorIDs(Town town){
        List<String> uuids = new ArrayList<>();
        for(String key : visitations.keySet()){
            if(town.getName().equals(visitations.get(key))) uuids.add(key);
        }

        return uuids;
    }

    public boolean intersectsTown(String world, Location l1, Location l2){
        World w = Sponge.getServer().getWorld(world).get();
        for(Town other : towns){
            Cuboid c1 = new Cuboid("world", l1, l2);
            Cuboid c2 = new Cuboid("world", new Location(w, other.getLoc1()[0], other.getLoc1()[1], other.getLoc1()[2]), new Location(w, other.getLoc2()[0], other.getLoc2()[1], other.getLoc2()[2]));

            if(c1.intersects(c2)) return true;
            if(c2.intersects(c1)) return true;
        }
        return false;
    }

    public Cuboid getTownCuboid(Town town){
        World world = Sponge.getServer().getWorld("world").get();
        Location l1 = new Location(world, town.getLoc1()[0], town.getLoc1()[1], town.getLoc1()[2]);
        Location l2 = new Location(world, town.getLoc2()[0], town.getLoc2()[1], town.getLoc2()[2]);

        return new Cuboid("world", l1, l2);
    }

    public Map<String, Rect2> getEditors(){
        return edit_Mode;
    }

    public boolean isInEditMode(Player player){
        return edit_Mode.containsKey(player.getUniqueId().toString());
    }

    public void editTown(Player player, Town town){
        Rect2 rect2 = edit_Mode.get(player.getUniqueId().toString());
        town.clearLocation();

        if(!intersectsTown("world", rect2.getLocation1(), rect2.getLocation2())){
            town.setLoc1(rect2.getLocation1());
            town.setLoc2(rect2.getLocation2());

            edit_Mode.remove(player.getUniqueId().toString());

            this.save();
        } else {
            // Tell the player the claim was intersecting another and restore the previous locations
            player.sendMessage(Text.of(TextColors.AQUA, "Your selection overlaps another claim."));
            town.restoreLocation();
        }
    }

    public void save(){
        this.townDatabase.saveDataBase(towns);
    }
}
