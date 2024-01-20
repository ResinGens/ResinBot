package dev.cognitivity.resingens.resinbot.managers;

import dev.cognitivity.resingens.resinbot.commands.DiscordCommand;
import dev.cognitivity.resingens.resinbot.commands.member.Link;
import dev.cognitivity.resingens.resinbot.commands.member.Unlink;
import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;

@Getter
public class CommandManager {
    private ArrayList<DiscordCommand> commands = new ArrayList<>() {{
        add(new Link());
        add(new Unlink());
    }};
    private ArrayList<SlashCommandData> commandsData = new ArrayList<>();


    public CommandManager() {
        for(DiscordCommand command : commands) {
            commandsData.add(command.getCommandData());
        }
    }
}
