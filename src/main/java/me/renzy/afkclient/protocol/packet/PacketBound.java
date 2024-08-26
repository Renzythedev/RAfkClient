package me.renzy.afkclient.protocol.packet;

import com.github.steveice10.packetlib.packet.Packet;

import javax.annotation.Nonnull;

public enum PacketBound {

    CLIENT,SERVER;

    @Nonnull
    public static<T extends Packet> PacketBound forPacket(@Nonnull Class<T> packet) {
        final String packetName = packet.getSimpleName();
        if (packetName.startsWith("Client"))
            return CLIENT;
        else if (packetName.startsWith("Server"))
            return SERVER;
        else
            throw new AssertionError();
    }
}
