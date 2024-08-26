package me.renzy.afkclient.protocol.packet.builder;

import com.github.steveice10.packetlib.packet.Packet;
import me.renzy.afkclient.exception.Exceptions;
import me.renzy.afkclient.objects.PacketData;
import me.renzy.afkclient.protocol.packet.PacketListener;
import me.renzy.afkclient.protocol.packet.registry.PacketRegistry;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public interface PacketHandlerBuilder<T extends Packet> {

    BiConsumer<Object,Throwable> DEFAULT_EXCEPTION_CONSUMER = Exceptions::reportPacket;

    static <T extends Packet> PacketHandlerBuilder<T> newBuilder(@Nonnull Class<T> packetClass, @Nonnull PacketRegistry registry) {
        return new PacketHandlerBuilderImpl<>(packetClass,registry);
    }

    @Nonnull
    PacketHandlerBuilder<T> listener(@Nonnull PacketListener<T> listener);

    @Nonnull
    PacketHandlerBuilder<T> exceptionConsumer(@Nonnull BiConsumer<? super T,Throwable> consumer);

    @Nonnull
    PacketData<T> register();
}
