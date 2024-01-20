package dev.cognitivity.resingens.resinbot.utils;

import dev.cognitivity.resingens.resinbot.ResinBot;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

public class TextUtils {
    @SuppressWarnings("deprecation")
    public static String translateAlternateColorCodes(char codeChar, String message) {
        message = message.replace("<RED>", codeChar+"#fd303a")
                .replace("<ORANGE>", codeChar+"#fcba03").replace("<YELLOW>", codeChar+"#fbfe3b")
                .replace("<DARKGREEN>", codeChar+"#2bba7c").replace("<GREEN>", codeChar+"#3afba7")
                .replace("<BLUE>", codeChar+"#31afec").replace("<PURPLE>", codeChar+"#a452e3")
                .replace("<MAGENTA>", codeChar+"#ea4adf").replace("<PINK>", codeChar+"#f6adc6")
                .replace("<GRAY>", codeChar+"#a4c1c2").replace("<DARKGRAY>", codeChar+"#787667");
        char[] chars = message.toCharArray();
        StringBuilder builder = new StringBuilder();
        String colorHex = "";
        boolean isHex = false;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == codeChar && i < chars.length - 1 && chars[i+1] == '#') {
                colorHex = "";
                isHex = true;
            } else if (isHex) {
                colorHex += chars[i];
                isHex = colorHex.length() < 7;
                if (!isHex)
                    builder.append(ChatColor.of(colorHex));
            } else
                builder.append(chars[i]);
        }
        return ChatColor.translateAlternateColorCodes(codeChar, builder.toString());
    }
    public static String readFile(String fileName) {
        return readFile(new File(ResinBot.getInstance().getDataFolder() + File.separator + fileName));
    }
    public static String readFile(File file) {
        try {
            createFile(file);
            StringBuilder text = new StringBuilder();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine()).append("\n");
            }
            if (text.toString().isBlank()) {
                scanner.close();
                return "";
            }
            scanner.close();
            return text.toString();
        } catch (IOException exception) {
            ResinBot.getInstance().getLogger().log(Level.WARNING, exception.getMessage());
            return "null";
        }
    }
    public static void writeFile(String fileName, String string) {
        writeFile(new File(ResinBot.getInstance().getDataFolder() + File.separator + fileName), string);
    }
    public static void writeFile(File file, String string) {
        createFile(file);
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(string);
            fileWriter.close();
        } catch (IOException exception) {
            ResinBot.getInstance().getLogger().log(Level.WARNING, exception.getMessage());
        }
    }
    public static void writeFileFromList(String fileName, List<String> list) {
        writeFileFromList(new File(ResinBot.getInstance().getDataFolder() + File.separator + fileName), list);
    }
    public static void writeFileFromList(File file, List<String> list) {
        StringBuilder string = new StringBuilder();
        for(String line : list) {
            string.append(line).append("\n");
        }
        writeFile(file, string.toString());
    }

    public static boolean createFile(File file) {
        boolean created = false;
        try {
            if (file.getParentFile().mkdirs()) {
                ResinBot.getInstance().getLogger().log(Level.INFO, "Created plugin folder.");
            }
            if (file.createNewFile()) {
                ResinBot.getInstance().getLogger().log(Level.INFO, "Created " + file.getName() + " in the plugin's folder.");
                created = true;
            }
        } catch(IOException exception) {
            ResinBot.getInstance().getLogger().log(Level.WARNING, exception.getMessage());
            return false;
        }
        return created;
    }

    public static boolean hasSetting(List<String> settingsList, String setting) {
        return settingsList.stream().anyMatch(line -> line.startsWith(setting));
    }
}