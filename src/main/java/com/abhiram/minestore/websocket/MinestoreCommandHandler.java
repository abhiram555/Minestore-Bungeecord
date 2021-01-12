package com.abhiram.minestore.websocket;

import com.abhiram.minestore.MineStore;
import com.abhiram.minestore.websocket.jsonobjects.Command;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.md_5.bungee.api.config.ServerInfo;

public class MinestoreCommandHandler extends SimpleChannelInboundHandler<String> {
    private Gson gson = new Gson();


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String incoming) throws Exception {
        Command command = gson.fromJson(incoming, Command.class);

        String password = MineStore.getInstance().getConfig().getString("Websocket-password");

        ByteArrayDataOutput sendcontent = ByteStreams.newDataOutput();
        sendcontent.writeUTF(command.getCommand());

        if(command.getPassword().equalsIgnoreCase(password))
        {
            for (ServerInfo server : MineStore.getInstance().getProxy().getServers().values()) {
                server.sendData("my:minestore", sendcontent.toByteArray());
            }
            MineStore.getInstance().getLogger().info("Send Order To Minestore-Spigot/Bukkit plugin!");
        }else
        {
            MineStore.getInstance().getLogger().info("Unable to send order to Minestore-Spigot/Bukkit plugin.(ERROR: Invalid password)");
        }
    }
}
