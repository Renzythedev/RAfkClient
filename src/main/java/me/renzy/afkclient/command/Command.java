package me.renzy.afkclient.command;

import javax.annotation.Nonnull;

public interface Command {

    @Nonnull
    CommandExecutor getExecutor();

    @Nonnull
    String getDescription();

    @Nonnull
    String[] getAliases();

    @Nonnull
    String getUsage();
}
