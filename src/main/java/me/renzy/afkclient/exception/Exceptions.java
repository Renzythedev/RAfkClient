package me.renzy.afkclient.exception;

import me.renzy.afkclient.exception.types.PacketException;
import me.renzy.afkclient.exception.types.TerminableException;
import me.renzy.afkclient.terminable.Terminable;
import me.renzy.afkclient.utils.LogManager;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class Exceptions {

    public static final UnsupportedOperationException CLASS_CANNOT_BE_INSTANTIATED = new UnsupportedOperationException("This class cannot be instantiated");
    public static final IllegalStateException TERMINABLE_ALREADY_CLOSED = new IllegalStateException("This terminable already closed");

    public static void report(@Nonnull InternalException exception) {
        LogManager.err(Objects.requireNonNull(exception, "exception").getMessage());
    }

    public static void reportPacket(@Nonnull Object packet, @Nonnull Throwable throwable) {
        report(new PacketException(packet,throwable));
    }

    public static void reportTerminable(@Nonnull Terminable terminable, @Nonnull Throwable throwable) {
        report(new TerminableException(terminable,throwable));
    }


    private Exceptions() {
        throw  CLASS_CANNOT_BE_INSTANTIATED;
    }
}
