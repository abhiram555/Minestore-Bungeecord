package com.abhiram.minestore.websocket;

import com.abhiram.minestore.MineStore;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class CommandHandler implements Runnable{
    private ServerSocket websocket;
    private MineStore plugin;


    public CommandHandler(MineStore plugin,int port)throws Exception{
        this.plugin = plugin;
        websocket = new ServerSocket(port);
    }
    public void run(){
        try{
            Socket soc = websocket.accept();
            BufferedReader read = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            String content = read.readLine();
            read.close();

            final String[] pass = content.split("  ");

            final ByteArrayDataOutput sendcontent = ByteStreams.newDataOutput();
            sendcontent.writeUTF(pass[1]);
            for(ServerInfo server : plugin.getProxy().getServers().values()){
                server.sendData("my:minestore",sendcontent.toByteArray());
            }
            plugin.getLogger().info("Send Order To Minestore-Spigot/Bukkit!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
