package top.plutomc.verify;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.LocalDate;

public final class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerJoinEvent(PlayerJoinEvent event) {
        event.joinMessage(null);

        if (event.getPlayer().isInvisible()) event.getPlayer().setInvisible(false);

        event.getPlayer().hidePlayer(Verify.instance(), event.getPlayer());

        event.getPlayer().setGameMode(GameMode.ADVENTURE);

        event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());

        if (!Verify.data().getStringList("dontCreate").contains(event.getPlayer().getUniqueId().toString())) {
            if (!Util.hasCode(event.getPlayer())) {
                int code = Util.genCode();
                Verify.data().set("codes." + code + ".name", event.getPlayer().getName().toLowerCase());
                LocalDate localDate = LocalDate.now();
                Verify.data().set("codes." + code + ".time", localDate.getYear() + "-" + localDate.getMonthValue() + "-" + localDate.getDayOfMonth());
                Verify.saveData();
            }
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(
                    "<newline><gray>你的验证码为：<white>" +
                            Util.getCodeByPlayer(event.getPlayer()) + "。<newline>")
            );
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerQuitEvent(PlayerQuitEvent event) {
        event.quitMessage(null);
    }

    @EventHandler
    public void asyncPlayerChatEvent(AsyncChatEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void playerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        if (!event.getPlayer().hasPermission("verify.bypass")) event.setCancelled(true);
    }

    @EventHandler
    public void playerCommandSendEvent(PlayerCommandSendEvent event) {
        if (!event.getPlayer().hasPermission("verify.bypass")) event.getCommands().clear();
    }
}