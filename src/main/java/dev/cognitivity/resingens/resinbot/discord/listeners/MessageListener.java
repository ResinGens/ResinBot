package dev.cognitivity.resingens.resinbot.discord.listeners;

import com.google.gson.JsonObject;
import dev.cognitivity.resingens.resinbot.Messages;
import dev.cognitivity.resingens.resinbot.ResinBot;
import dev.cognitivity.resingens.resindata.DataUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Server server = ResinBot.getInstance().getServer();
        if(event.getChannel().getIdLong() == 1195842894976790558L) {
            if(!event.getAuthor().isBot()) {
                String message = event.getMessage().getContentDisplay().startsWith("> say ")
                        ? event.getMessage().getContentDisplay().replaceFirst("> say ", "")
                        : event.getMessage().getContentDisplay();
                if (message.startsWith("> say ") || !message.startsWith("> ")) {
                    server.getOnlinePlayers().forEach(player -> player.sendMessage(
                            Messages.DISCORD_MINECRAFT_CHAT.getMessage("<#" + Integer.toHexString(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getColor())
                                    .getRGB()).substring(2) + "><b>" + event.getMember().getRoles().get(0).getName() + "</b> " + event.getAuthor().getName(), message)));
                } else {
                    String command = message.replace("> ", "");
                    File file = new File(ResinBot.getInstance().getDataFolder().getAbsolutePath() + File.separator + "discord" + File.separator + event.getAuthor().getId() + ".json");
                    if (!file.exists()) {
                        event.getMessage().reply("**CANNOT SEND COMMAND**  `You are not linked`").queue();
                        return;
                    }
                    JsonObject account = DataUtils.parseJSON(file);
                    assert account != null;
                    if(new UUID(0, 0).equals(UUID.fromString(account.get("link-uuid").getAsString()))) {
                        event.getMessage().reply("**CANNOT SEND COMMAND**  `You are not linked`").queue();
                        return;
                    }
                    OfflinePlayer linkedPlayer = ResinBot.getInstance().getServer().getOfflinePlayer(UUID.fromString(account.get("link-uuid").getAsString()));
                    net.luckperms.api.model.user.User lpUser = Objects.requireNonNull(LuckPermsProvider.get().getUserManager().getUser(linkedPlayer.getUniqueId()));
                    if(!lpUser.getCachedData().getPermissionData().checkPermission("resin.bot.console").asBoolean()) {
                        event.getMessage().reply("**CANNOT SEND COMMAND**  `No permission`").queue();
                    }
                    server.getScheduler().runTask(ResinBot.getInstance(), () -> server.dispatchCommand(server.getConsoleSender(), command));
                    server.getOnlinePlayers().stream().filter(player -> player.hasPermission("resin.bot.console")).forEach(staff -> staff.sendMessage(
                                    Messages.DISCORD_CONSOLE_SENT.getMessage("<#" + Integer.toHexString(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getColor())
                                            .getRGB()).substring(2) + "><b>" + event.getMember().getRoles().get(0).getName() + "</b> " + event.getAuthor().getName(), command)));
                }
            }
        }
    }
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        ResinBot.getInstance().getCommandManager().getCommands().stream().filter(command -> event.getName().equals(command.getCommandData().getName()))
                .forEach(command -> {
                    long start = System.currentTimeMillis();
                    User user = Objects.requireNonNull(event.getMember()).getUser();
                    ResinBot.getInstance().getLogger().log(Level.INFO, "Running command \"/"+event.getName()+"\" for "+ user.getName() + " in #" + event.getChannel().getName()+"...");
                    command.run(event);
                    ResinBot.getInstance().getLogger().log(Level.INFO, "Successfully ran command \"/"+event.getName()+"\" for "+ user.getName() + " in #" +
                            event.getChannel().getName()+"! Finished in "+(System.currentTimeMillis()-start)+" ms.");
                });
    }
}
