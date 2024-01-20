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
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.UUID;

public class Unlink extends DiscordCommand {
    @Getter
    private final SlashCommandData commandData =
            Commands.slash("unlink", "Unlinks your Minecraft account from your Discord account.").setGuildOnly(true);


    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        Member member = event.getMember();
        assert member != null;
        File file = new File(ResinBot.getInstance().getDataFolder().getAbsolutePath() + File.separator + "discord" + File.separator + member.getId() + ".json");
        if (!file.exists()) {
            event.getHook().editOriginal("**ERROR** `You are not linked`").queue();
            return;
        }
        JsonObject account = DataUtils.parseJSON(file);
        assert account != null;
        if(new UUID(0, 0).equals(UUID.fromString(account.get("link-uuid").getAsString()))) {
            event.getHook().editOriginal("**ERROR** `You are not linked`").queue();
            return;
        }
        OfflinePlayer player = ResinBot.getInstance().getServer().getOfflinePlayer(UUID.fromString(account.get("link-uuid").getAsString()));
        event.getHook().editOriginal("Unlinking from **"+player.getName()+"**.").queue();
        account.addProperty("link-uuid", new UUID(0, 0).toString());
        DataUtils.writeJSONObject(file, account);
        ResinData.getInstance().getData(player).setDiscordId(0);
        ResinData.getInstance().getData(player).saveData();

        event.getHook().editOriginal("You are no longer linked to **" + player.getName() + "**.").queue();
        if(player.getPlayer() != null) player.getPlayer().sendMessage(Messages.UNLINK_UNLINKED.getMessage());
    }
}
