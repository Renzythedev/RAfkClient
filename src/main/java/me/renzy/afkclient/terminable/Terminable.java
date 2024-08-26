package me.renzy.afkclient.terminable;

public interface Terminable extends AutoCloseable{

    @Override
    void close() throws Exception;

    boolean isClosed();
}
