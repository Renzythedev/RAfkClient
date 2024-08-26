package me.renzy.afkclient.command;

import javax.annotation.Nonnull;

public interface CommandBuilder {

    @Nonnull
    static CommandBuilder newBuilder(@Nonnull String command) {
        return new CommandBuilderImpl(command);
    }

    @Nonnull
    CommandBuilder description(@Nonnull String description);

    @Nonnull
    CommandBuilder aliases(@Nonnull String... aliases);

    @Nonnull
    CommandBuilder usage(@Nonnull String usage);

    @Nonnull
    Command executor(@Nonnull CommandExecutor executor);
}
