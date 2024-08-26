package me.renzy.afkclient.utils;

import org.jetbrains.annotations.NotNull;

public final class LogManager {

    public static void info(@NotNull String info) {
        System.out.println("LOGGER " + Colors.BLUE + "INFO " + Colors.RESET + info + Colors.RESET);
    }

    public static void warn(@NotNull String warn) {
        System.out.println("LOGGER " + Colors.YELLOW + "WARN " + Colors.RESET + warn + Colors.RESET);
    }

    public static void err(@NotNull String error) {
        System.out.println("LOGGER " + Colors.RED + "ERROR " + Colors.RESET + error + Colors.RESET);
    }

}
