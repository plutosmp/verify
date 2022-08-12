package top.plutomc.verify;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Command implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 2 && args[0].equalsIgnoreCase("lookup")) {
            int code = Integer.parseInt(args[1]);
            if (Verify.data().contains("codes." + code)) {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<white>" + code + " <gray>对应的玩家为：<white>" + Verify.data().getString("codes." + code + ".name") + "。"));
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<gray>生成时间：<white>" + Verify.data().getString("codes." + code + ".time") + "。"));
            } else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>没有找到对应的数据。"));
            }
            return true;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("remove")) {
            int code = Integer.parseInt(args[1]);
            if (Verify.data().contains("codes." + code)) {
                Verify.data().set("codes." + code, null);
                List<Integer> list = Verify.data().getIntegerList("generatedCodes");
                list.remove(Integer.valueOf(code));
                Verify.data().set("generatedCodes", list);
                Verify.saveData();
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>已移除。"));
            } else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>没有找到对应的数据。"));
            }
            return true;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("dontCreate") && args[1].equalsIgnoreCase("list")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(
                    "<gray>不会创建验证码的用户："
            ));
            for (String s : Verify.data().getStringList("dontCreate")) {
                UUID uuid = UUID.fromString(s);
                sender.sendMessage(
                        MiniMessage.miniMessage().deserialize("<white>" + s + ", " + Bukkit.getOfflinePlayer(uuid).getName())
                );
            }
            return true;
        }

        if (args.length >= 3 && args[0].equalsIgnoreCase("dontCreate") && args[1].equalsIgnoreCase("add") && Bukkit.getPlayer(args[2]) != null) {
            Player player = Bukkit.getPlayer(args[2]);
            if (!Verify.data().getStringList("dontCreate").contains(player.getUniqueId().toString())) {
                List<String> list = Verify.data().getStringList("dontCreate");
                list.add(player.getUniqueId().toString());
                Verify.data().set("dontCreate", list);
                Verify.saveData();
                sender.sendMessage(MiniMessage.miniMessage().deserialize(
                        "<green>完成。"
                ));
            } else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize(
                        "<red>这个玩家已经在名单里了。"
                ));
            }
            return true;
        }

        if (args.length >= 3 && args[0].equalsIgnoreCase("dontCreate") && args[1].equalsIgnoreCase("remove") && Bukkit.getPlayer(UUID.fromString(args[2])) != null) {
            Player player = Bukkit.getPlayer(UUID.fromString(args[2]));
            if (Verify.data().getStringList("dontCreate").contains(player.getUniqueId().toString())) {
                List<String> list = Verify.data().getStringList("dontCreate");
                list.remove(player.getUniqueId().toString());
                Verify.data().set("dontCreate", list);
                Verify.saveData();
                sender.sendMessage(MiniMessage.miniMessage().deserialize(
                        "<green>完成。"
                ));
            } else {
                sender.sendMessage(MiniMessage.miniMessage().deserialize(
                        "<red>这个玩家没在名单里。"
                ));
            }
            return true;
        }

        sender.sendMessage(MiniMessage.miniMessage().deserialize(
                "<red>请确认您没有输错指令。"
        ));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return List.of("lookup", "remove", "reload", "dontCreate");

        if (args.length == 2 && args[0].equalsIgnoreCase("lookup")) {
            if (Verify.data().getConfigurationSection("codes") != null) {
                return new ArrayList<>(Verify.data().getConfigurationSection("codes").getKeys(false));
            }
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            if (Verify.data().getConfigurationSection("codes") != null) {
                return new ArrayList<>(Verify.data().getConfigurationSection("codes").getKeys(false));
            }
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("dontCreate")) {
            return List.of("add", "remove", "list");
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("dontCreate") && args[1].equalsIgnoreCase("remove")) {
            return Verify.data().getStringList("dontCreate");
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("dontCreate") && args[1].equalsIgnoreCase("add")) {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            List<String> playerNames = new ArrayList<>();
            players.forEach(player -> playerNames.add(player.getName()));
            return playerNames;
        }

        return null;
    }
}