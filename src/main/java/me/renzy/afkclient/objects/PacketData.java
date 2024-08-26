package me.renzy.afkclient.objects;

import com.github.steveice10.packetlib.packet.Packet;
import me.renzy.afkclient.protocol.packet.PacketBound;
import me.renzy.afkclient.terminable.Terminable;

import javax.annotation.Nonnull;

public interface PacketData<T extends Packet> extends Terminable {

    void unregister();

    @Override
    default void close() throws Exception {
        unregister();
    }

    @Nonnull
    Class<T> getPacketClass();

    @Nonnull
    PacketBound getBound();
}
