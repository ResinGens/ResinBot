package dev.cognitivity.resingens.resinbot.commands.member;

import com.google.gson.JsonObject;
import dev.cognitivity.resingens.resinbot.Messages;
import dev.cognitivity.resingens.resinbot.ResinBot;
import dev.cognitivity.resingens.resinbot.commands.DiscordCommand;
import dev.cognitivity.resingens.resindata.DataUtils;
import dev.cognitivity.resingens.resindata.ResinData;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Objects;

public class Link extends DiscordCommand {
    @Getter
    private final SlashCommandData commandData =
            Commands.slash("link", "Links your Minecraft account to your Discord account.")
                    .setGuildOnly(true)
                    .addOption(OptionType.INTEGER, "code", "Link code from \"/link\" in-game.");


    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        if (event.getOption("code", OptionMapping::getAsString) == null) {
            event.getHook().editOriginal("**ERROR** `No code specified`").queue();
            return;
        }

        Short code;
        try {
            code = Objects.requireNonNull(event.getOption("code", OptionMapping::getAsInt)).shortValue();
        } catch (ArithmeticException exception) {
            event.getHook().editOriginal("**ERROR** `Not an integer`").queue();
            return;
        }
        Player player = instance.getBot().getLinkCode(code);
        Member member = event.getMember();
        assert member != null;
        if(player == null) {
            event.getHook().editOriginal("**ERROR** `Invalid Code`").queue();
            return;
        }
        player.sendMessage(Messages.LINK_LINKING.getMessage());
        event.getHook().editOriginal("Linking to **" + player.getName() + "**... (`" + player.getUniqueId() + "`)").queue();
        instance.getBot().removeLinkCode(code);
        ResinData.getInstance().getData(player).setDiscordId(member.getIdLong());
        ResinData.getInstance().getData(player).saveData();

        JsonObject discordData = new JsonObject();
        discordData.addProperty("link-uuid", player.getUniqueId().toString());


        File file = new File(ResinBot.getInstance().getDataFolder().getAbsolutePath() + File.separator + "discord" + File.separator + member.getId() + ".json");
        DataUtils.createFile(file);
        DataUtils.writeJSONObject(file, discordData);

        event.getHook().editOriginal("You are now linked to **" + player.getName() + "**! (`" + player.getUniqueId() + "`)").queue();
        player.sendMessage(Messages.LINK_LINKED.getMessage(member.getUser().getName()));
    }
}