package me.renzy.afkclient.protocol.packet.builder;

import com.github.steveice10.packetlib.packet.Packet;
import me.renzy.afkclient.objects.PacketData;
import me.renzy.afkclient.protocol.packet.PacketHandler;
import me.renzy.afkclient.protocol.packet.PacketListener;
import me.renzy.afkclient.protocol.packet.registry.PacketRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

final class PacketHandlerBuilderImpl<T extends Packet> implements PacketHandlerBuilder<T> {

    final Class<T> packetClass;
    final PacketRegistry registry;

    @SuppressWarnings("unchecked")
    PacketListener<T> listener = (PacketListener<T>) PacketListener.EMPTY;

    BiConsumer<? super T, Throwable> exceptionConsumer = DEFAULT_EXCEPTION_CONSUMER;

    PacketHandlerBuilderImpl(Class<T> packetClass, PacketRegistry registry) {
        this.packetClass = packetClass;
        this.registry = registry;
    }

    @Nonnull
    @Override
    public PacketHandlerBuilder<T> listener(@Nonnull PacketListener<T> listener) {
        this.listener = listener;
        return this;
    }

    @Nonnull
    @Override
    public PacketHandlerBuilder<T> exceptionConsumer(@Nonnull BiConsumer<? super T, Throwable> consumer) {
        this.exceptionConsumer = consumer;
        return this;
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public PacketData<T> register() {
        return (PacketData<T>) this.registry.register(this.packetClass,new HandlerImpl<>(this.packetClass,this.listener,this.exceptionConsumer));
    }

    private static final class HandlerImpl<T extends Packet> implements PacketHandler {

        private final Class<T> packetClass;

        private final PacketListener<T> listener;

        private final BiConsumer<? super T, Throwable> exceptionConsumer;

        private HandlerImpl(Class<T> packetClass, PacketListener<T> listener, BiConsumer<? super T, Throwable> exceptionConsumer) {
            this.packetClass = packetClass;
            this.listener = listener;
            this.exceptionConsumer = exceptionConsumer;
        }

        @Override
        public void handle(@NotNull Packet packet) {
            T packetInstance = this.packetClass.cast(packet);
            try {
                this.listener.onReceive(packetInstance);
            }catch (Throwable t) {
                this.exceptionConsumer.accept(packetInstance,t);
            }
        }
    }
}
