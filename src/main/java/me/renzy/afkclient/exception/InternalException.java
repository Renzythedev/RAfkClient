package me.renzy.afkclient.exception;

public abstract class InternalException extends RuntimeException{

    public InternalException(String what, Throwable t) {
        super(what,t);
    }
}
