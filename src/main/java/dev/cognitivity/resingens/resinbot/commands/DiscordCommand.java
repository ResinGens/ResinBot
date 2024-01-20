package dev.cognitivity.resingens.resinbot.commands;

import dev.cognitivity.resingens.resinbot.ResinBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class DiscordCommand {
    public ResinBot instance = ResinBot.getInstance();
    public BukkitScheduler scheduler = instance.getServer().getScheduler();

    public abstract SlashCommandData getCommandData();
    public abstract void run(SlashCommandInteractionEvent event);
}

