package me.renzy.afkclient.exception.types;

import com.github.steveice10.packetlib.packet.Packet;
import me.renzy.afkclient.exception.InternalException;

public final class PacketException extends InternalException {

    public PacketException(Object packet, Throwable t) {
        super("Packet exception for " + packet.getClass().getSimpleName() ,t);
    }
}
