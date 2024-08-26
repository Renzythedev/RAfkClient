package me.renzy.afkclient.protocol.packet.registry;

import com.github.steveice10.packetlib.packet.Packet;
import me.renzy.afkclient.exception.Exceptions;
import me.renzy.afkclient.objects.PacketData;
import me.renzy.afkclient.protocol.packet.PacketBound;
import me.renzy.afkclient.protocol.packet.PacketHandler;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PacketRegistry {

    private final Map<Class<? extends Packet>, List<PacketHandler>> handlers;

    public PacketRegistry() {
        this.handlers = new HashMap<>();
    }

    @Nonnull
    public PacketData<? extends Packet> register(@Nonnull Class<? extends Packet> packet, @Nonnull PacketHandler handler) {
        this.handlers.computeIfAbsent(packet, k -> new ArrayList<>());

        this.handlers.get(packet).add(handler);
        return new PacketDataImpl<>(packet,this);
    }

    public void unregister(@Nonnull Class<? extends Packet> packetClass) {
        handlers.remove(packetClass);
    }

    public Map<Class<? extends Packet>, List<PacketHandler>> getHandlers() {
        return this.handlers;
    }

    private static final class PacketDataImpl<T extends Packet> implements PacketData<T> {

        final Class<T> packetClass;
        final PacketRegistry registry;

        final AtomicBoolean active = new AtomicBoolean(false);

        PacketDataImpl(Class<T> packetClass, PacketRegistry registry) {
            this.packetClass = packetClass;
            this.registry = registry;
        }

        @Override
        public void unregister() {
            if (!this.active.compareAndSet(false,true))
                throw Exceptions.TERMINABLE_ALREADY_CLOSED;
            this.registry.unregister(this.packetClass);
        }

        @Nonnull
        @Override
        public Class<T> getPacketClass() {
            return this.packetClass;
        }

        @Nonnull
        @Override
        public PacketBound getBound() {
            return PacketBound.forPacket(this.packetClass);
        }

        @Override
        public boolean isClosed() {
            return this.active.get();
        }
    }

}
