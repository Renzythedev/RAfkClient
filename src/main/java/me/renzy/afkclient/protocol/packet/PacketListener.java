package me.renzy.afkclient.protocol.packet;

import com.github.steveice10.packetlib.packet.Packet;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface PacketListener<T extends Packet> {

    PacketListener<?> EMPTY = p -> {};

    void onReceive(@Nonnull T packet);
}
