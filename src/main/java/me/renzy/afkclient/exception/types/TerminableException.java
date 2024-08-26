package me.renzy.afkclient.exception.types;

import me.renzy.afkclient.exception.InternalException;
import me.renzy.afkclient.terminable.Terminable;

public final class TerminableException extends InternalException {
    public TerminableException(Terminable terminable, Throwable t) {
        super(terminable.getClass().getSimpleName() + ": " + t.getMessage(), t);
    }
}
