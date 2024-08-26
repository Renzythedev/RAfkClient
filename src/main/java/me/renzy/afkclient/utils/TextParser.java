package me.renzy.afkclient.utils;

import me.renzy.afkclient.exception.Exceptions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import javax.annotation.Nonnull;
import java.util.Map;

public final class TextParser {

    private static final Map<Character, String> COLOR_CODES = Map.ofEntries(
            Map.entry('0', "\033[30m"),  // Black
            Map.entry('1', "\033[34m"),  // Dark Blue
            Map.entry('2', "\033[32m"),  // Dark Green
            Map.entry('3', "\033[36m"),  // Dark Aqua
            Map.entry('4', "\033[31m"),  // Dark Red
            Map.entry('5', "\033[35m"),  // Dark Purple
            Map.entry('6', "\033[33m"),  // Gold
            Map.entry('7', "\033[37m"),  // Gray
            Map.entry('8', "\033[90m"),  // Dark Gray
            Map.entry('9', "\033[94m"),  // Blue
            Map.entry('a', "\033[92m"),  // Green
            Map.entry('b', "\033[96m"),  // Aqua
            Map.entry('c', "\033[91m"),  // Red
            Map.entry('d', "\033[95m"),  // Light Purple
            Map.entry('e', "\033[93m"),  // Yellow
            Map.entry('f', "\033[97m"),  // White
            Map.entry('l', "\033[1m"),   // Bold
            Map.entry('m', "\033[9m"),   // Strikethrough
            Map.entry('n', "\033[4m"),   // Underline
            Map.entry('o', "\033[3m"),   // Italic
            Map.entry('r', "\033[0m")    // Reset
    );

    @Nonnull
    public static String parse(@Nonnull Component component) {
        StringBuilder result = new StringBuilder();

        if (!(component instanceof TextComponent)) {
            LogManager.err("That component isn't a TextComponent.");
            return "";
        }

        TextComponent textComponent = (TextComponent) component;

        TextColor color = textComponent.color();
        if (color != null) {
            result.append(getColor(color));
        }
        for (Map.Entry<TextDecoration, TextDecoration.State> d : component.decorations().entrySet()) {
            if (TextDecoration.State.TRUE == d.getValue()) {
                result.append(getDecoration(d.getKey()));
            }
        }
        String content = textComponent.content();
        result.append(parseMinecraftColors(content));

        result.append("\033[0m");

        for (Component child : component.children()) {
            result.append(parse(child));
        }

        return result.toString();
    }

    @Nonnull
    public static String parse(@Nonnull String content) {
        StringBuilder result = new StringBuilder();

        result.append(parseMinecraftColors(content));

        result.append("\033[0m");

        return result.toString();
    }


    private static String parseMinecraftColors(String content) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < content.length(); i++) {
            char currentChar = content.charAt(i);

            if (currentChar == '§' && i + 1 < content.length()) {
                char colorCode = content.charAt(i + 1);
                String ansiCode = COLOR_CODES.get(colorCode);

                if (ansiCode != null) {
                    result.append(ansiCode);
                    i++;
                }
            } else {
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    private static String getColor(TextColor color) {
        int red = color.red();
        int green = color.green();
        int blue = color.blue();
        return String.format("\033[38;2;%d;%d;%dm", red, green, blue);
    }

    private static String getDecoration(TextDecoration decoration) {
        switch (decoration) {
            case BOLD:
                return "\033[1m";
            case ITALIC:
                return "\033[3m";
            case OBFUSCATED:
                return "\033[8m";
            case UNDERLINED:
                return "\033[4m";
            case STRIKETHROUGH:
                return "\033[9m";
            default:
                throw new AssertionError();
        }
    }

    private TextParser() {
        throw Exceptions.CLASS_CANNOT_BE_INSTANTIATED;
    }
}
