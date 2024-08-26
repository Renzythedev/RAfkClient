package me.renzy.afkclient.command;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

final class CommandBuilderImpl implements CommandBuilder{

    private final String cmd;

    private String description = "";
    private String[] aliases = {};

    private String usage = "";

    private CommandExecutor executor = CommandExecutor.EMPTY_EXECUTOR;

    CommandBuilderImpl(String command) {
        this.cmd = command;
    }

    @Nonnull
    @Override
    public CommandBuilder description(@Nonnull String description) {
        this.description = description;
        return this;
    }

    @Nonnull
    @Override
    public CommandBuilder aliases(@Nonnull String... aliases) {
        this.aliases = aliases;
        return this;
    }

    @Nonnull
    @Override
    public CommandBuilder usage(@Nonnull String usage) {
        this.usage = usage;
        return this;
    }

    @NotNull
    @Override
    public Command executor(@Nonnull CommandExecutor executor) {
        this.executor = executor;
        Command command = new CommandImpl(this.description,this.aliases,this.usage,this.executor);
        Commands.register(this.cmd,command);
        return command;
    }

    private final class CommandImpl implements Command {

        private final String description;
        private final String[] aliases;

        private final String usage;

        private final CommandExecutor executor;

        private CommandImpl(String description, String[] aliases, String usage, CommandExecutor executor) {
            this.description = description;
            this.aliases = aliases;
            this.usage = usage;
            this.executor = executor;
        }

        @NotNull
        @Override
        public CommandExecutor getExecutor() {
            return this.executor;
        }

        @NotNull
        @Override
        public String getDescription() {
            return this.description;
        }

        @NotNull
        @Override
        public String[] getAliases() {
            return this.aliases;
        }

        @NotNull
        @Override
        public String getUsage() {
            return this.usage;
        }
    }
}
