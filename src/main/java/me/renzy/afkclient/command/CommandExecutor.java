package me.renzy.afkclient.command;

import javax.annotation.Nonnull;

public interface CommandExecutor {

    CommandExecutor EMPTY_EXECUTOR = (args,cmd) -> true;

    boolean execute(@Nonnull String[] args,@Nonnull Command command);
}
