package dev.cognitivity.resingens.resinbot;

import dev.cognitivity.resingens.resinbot.discord.Bot;
import dev.cognitivity.resingens.resinbot.managers.CommandManager;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;


public final class ResinBot extends JavaPlugin {
    @Getter private static ResinBot instance;
    @Getter private Bot bot;
    @Getter private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        commandManager = new CommandManager();
        getLogger().log(Level.INFO, "Starting bot...");
        bot = new Bot(Dotenv.configure().load().get("TOKEN"));
        bot.build();
        getLogger().log(Level.INFO, "ResinBot has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Stopping bot...");
        bot.getJda().shutdown();
        getLogger().log(Level.INFO, "ResinBot has been disabled.");
    }
}