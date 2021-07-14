package com.pixelrp.core.utils.configuration;

import com.google.inject.Inject;
import com.pixelrp.core.PixelRP;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.config.ConfigDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration {

    public PixelRP plugin = PixelRP.instance;

    private Path configFile;

    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private CommentedConfigurationNode configNode;

    public Configuration(String filename){
        configFile = plugin.getConfigDir().resolve(filename);
        configLoader = HoconConfigurationLoader.builder().setPath(configFile).build();

        if(!Files.exists(plugin.getConfigDir())){
            try{
                Files.createDirectories(plugin.getConfigDir());
            }catch(IOException io){
                io.printStackTrace();
            }
        }
        setup();
    }

    public void setup(){
        if(!Files.exists(configFile)){
            try{
                Files.createFile(configFile);
                load();
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            load();
        }
    }

    //Loads config file if it already exists
    public void load(){
        try{
            configNode = configLoader.load();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    //And obviously saves config whenever needed.
    public void save(){
        try{
            configLoader
                    .save(configNode);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Path getConfigDir(){
        return plugin.getConfigDir();
    }
    public CommentedConfigurationNode get(){
        return configNode;
    }
}
