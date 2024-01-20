package dev.cognitivity.resingens.resinbot;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Getter
public enum Messages {
    DISCORD_MINECRAFT_CHAT(" <#5865F2><b>DISCORD</b>  <white>%s<white>: %s"),
    DISCORD_CONSOLE_SENT("<red><b>CONSOLE</b>  <white>%s <gray>has sent a command from Discord: <white>/%s"),
    LINK_LINKING("<gray>Attempting to link your Discord account..."),
    LINK_LINKED("<gray>You are now linked to <#5865F2>%s<gray>."),
    UNLINK_UNLINKED("<red>You have been unlinked from your Discord account."),
    ;

    private final String string;

    Messages(@NotNull String string) {
        this.string = string;
    }

    public void send(Object... args) {
        send(ResinBot.getInstance().getServer().getOnlinePlayers(), args);
    }
    public void send(Player player, Object... args) {
        player.sendMessage(getMessage(args));
    }
    public void send(Collection<? extends Player> players, Object... args) {
        players.forEach(player -> send(player, args));
    }

    public Component getMessage(Object... args) {
        return MiniMessage.miniMessage().deserialize(String.format(string, args));
    }
    public String getString(Object... args) {
        return String.format(string, args);
    }
}
