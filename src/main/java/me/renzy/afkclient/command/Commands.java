package me.renzy.afkclient.command;

import me.renzy.afkclient.Client;
import me.renzy.afkclient.command.Command;
import me.renzy.afkclient.exception.Exceptions;
import me.renzy.afkclient.utils.Colors;
import me.renzy.afkclient.utils.LogManager;

import javax.annotation.Nonnull;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Commands {

    private static final Map<String, Command> commands = new HashMap<>();

    public static final Command HELP_COMMAND = Commands.getCommand("help")
            .description("shows all command.")
            .executor((args,cmd) -> {
                LogManager.info(Colors.CYAN +"All commands");
                for (Map.Entry<String,Command> entry : commands.entrySet())
                    if (!"help".equals(entry.getKey()))
                        LogManager.info(entry.getKey() + ": " + entry.getValue().getDescription());
                return true;
            });

    static void register(String c,Command command) {
        commands.put(c,command);
    }

    @Nonnull
    public static CommandBuilder getCommand(@Nonnull String command) {
        return CommandBuilder.newBuilder(command);
    }

    public static boolean invokeCommand(@Nonnull String command) {
        String[] args = command.split(" ");

        for (Map.Entry<String,Command> entry : commands.entrySet()) {

            if (entry.getKey().equals(args[0])) {
                if (!entry.getValue().getExecutor().execute(Arrays.copyOfRange(args,1,args.length),entry.getValue()))
                    LogManager.info(entry.getValue().getUsage());
                return true;
            }


            for(String aliasesCmd : entry.getValue().getAliases()) {

                if (aliasesCmd.equals(args[0]))
                    if (!entry.getValue().getExecutor().execute(Arrays.copyOfRange(args,1,args.length),entry.getValue())) {
                        LogManager.info("usage: "+entry.getValue().getUsage());
                        return true;
                    }
            }
        }

        return false;
    }

    private Commands() {
        throw Exceptions.CLASS_CANNOT_BE_INSTANTIATED;
    }
}
