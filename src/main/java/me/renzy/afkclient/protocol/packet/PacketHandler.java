package me.renzy.afkclient.protocol.packet;

import com.github.steveice10.packetlib.packet.Packet;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface PacketHandler {

    void handle(@Nonnull Packet packet);
}
