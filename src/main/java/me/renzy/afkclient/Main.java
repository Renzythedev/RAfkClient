package me.renzy.afkclient;

import com.github.steveice10.mc.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import com.github.steveice10.packetlib.packet.Packet;
import me.renzy.afkclient.protocol.MinecraftBot;
import me.renzy.afkclient.protocol.Packets;

public class Main {

    public static void main(String[] args) {
        Client.getClient().start();

        Runtime.getRuntime().addShutdownHook(new Thread(Client.getClient()::shutdown));
    }
}
