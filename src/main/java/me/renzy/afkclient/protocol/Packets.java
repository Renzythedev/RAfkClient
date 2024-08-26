package me.renzy.afkclient.protocol;

import com.github.steveice10.packetlib.packet.Packet;
import me.renzy.afkclient.exception.Exceptions;
import me.renzy.afkclient.protocol.packet.PacketHandler;
import me.renzy.afkclient.protocol.packet.builder.PacketHandlerBuilder;
import me.renzy.afkclient.protocol.packet.registry.PacketRegistry;

import javax.annotation.Nonnull;
import java.util.List;

public final class Packets {

    private static final PacketRegistry REGISTRY = new PacketRegistry();

    @Nonnull
    public static <T extends Packet> PacketHandlerBuilder<T> subscribe(@Nonnull Class<T> packetClass) {
        return PacketHandlerBuilder.newBuilder(packetClass,REGISTRY);
    }

    public static void callPacket(@Nonnull Packet packet) {

        Class<? extends Packet> packetClass = packet.getClass();

        List<PacketHandler> handlers= REGISTRY.getHandlers().get(packetClass);

        if (handlers == null) {
            return;
        }
        for (PacketHandler handler : handlers) {
            handler.handle(packet);
        }
    }

    private Packets() {
        throw Exceptions.CLASS_CANNOT_BE_INSTANTIATED;
    }
}
