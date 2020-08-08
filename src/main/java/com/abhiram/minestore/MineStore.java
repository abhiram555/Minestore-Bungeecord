package com.abhiram.minestore;


import com.abhiram.minestore.websocket.CommandHandler;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class MineStore extends Plugin {
    private File file;
    private Configuration config;


    @Override
    public void onEnable() {
        try{
          LoadConfig();
          int port = config.getInt("Websocket-port");
          String password = config.getString("Websocket-password");
          getProxy().getScheduler().schedule(this,new CommandHandler(this,port,password),2,2, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
        getLogger().info("Enabled Checking for Order.");
    }



    private void LoadConfig() throws Exception{

        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        file = new File(getDataFolder(), "config.yml");


        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
    }
}
