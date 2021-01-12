package com.abhiram.minestore.websocket;

import com.abhiram.minestore.MineStore;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

@Deprecated
public class CommandHandler implements Runnable{
    private ServerSocket websocket;
    private MineStore plugin;
    private String websocket_password;

    public CommandHandler(MineStore plugin,int port,String websocket_password)throws Exception{
        this.plugin = plugin;
        this.websocket_password = websocket_password;
        websocket = new ServerSocket(port);
    }
    public void run(){
        try{
            Socket soc = websocket.accept();
            BufferedReader read = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            String content = read.readLine();
            read.close();

            final String[] pass = content.split("  ");

            // Check if there is any command
            if(pass.length == 1){
                plugin.getLogger().info("Got an order from minestore but unable to send it because there are no commands");
                return;
            }


            final ByteArrayDataOutput sendcontent = ByteStreams.newDataOutput();
            sendcontent.writeUTF(pass[1]);

            // plugin.getLogger().info("Debug mode password : " + pass[0] + " Command: " + pass[1]);

            if(pass[0].equalsIgnoreCase(websocket_password)) {
                for (ServerInfo server : plugin.getProxy().getServers().values()) {
                    server.sendData("my:minestore", sendcontent.toByteArray());
                }
                plugin.getLogger().info("Send Order To Minestore-Spigot/Bukkit plugin!");
            }else {
                plugin.getLogger().info("Unable to send order to Minestore-Spigot/Bukkit plugin.(ERROR: Invalid password)");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
