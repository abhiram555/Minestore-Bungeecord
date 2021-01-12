package com.abhiram.minestore;


import com.abhiram.minestore.websocket.NettySocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class MineStore extends Plugin {
    private File file;
    private Configuration config;
    private static MineStore instance;


    @Override
    public void onEnable() {
        instance = this;
        LoadConfig();
        try{
          int port = config.getInt("Websocket-port");
          startServer(port);
        }catch (Exception e){
            int port = config.getInt("Websocket-port");
            getLogger().info("Unable to listen for order on: " + port);
        }
        getLogger().info("Enabled Checking for Order.");
    }

    public Configuration getConfig()
    {
        return config;
    }

    private void startServer(int port) throws Exception
    {
        getLogger().info("Starting Server at " + port);

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(eventLoopGroup,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettySocketHandler())
                .childOption(ChannelOption.SO_KEEPALIVE,true);

        serverBootstrap.bind(port).sync();
    }
    private void LoadConfig(){
        try {
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
        }catch (Exception e)
        {
            getLogger().info("Unable to load config...");
        }
    }

    public static MineStore getInstance()
    {
        return instance;
    }
}
