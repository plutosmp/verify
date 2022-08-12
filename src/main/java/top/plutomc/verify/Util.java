package top.plutomc.verify;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public final class Util {
    public static int genCode() {
        while (true) {
            StringBuilder stringBuilder = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 6; i++) {
                stringBuilder.append(random.nextInt(10));
            }
            int code = Integer.parseInt(stringBuilder.toString());

            if (!Verify.data().getIntegerList("generatedCodes").contains(code)) {
                List<Integer> list = Verify.data().getIntegerList("generatedCodes");
                list.add(code);
                Verify.data().set("generatedCodes", list);
                Verify.saveData();
                return code;
            }
        }
    }

    public static boolean hasCode(Player player) {
        if (Verify.data().getConfigurationSection("codes") != null) {
            for (String key : Verify.data().getConfigurationSection("codes").getKeys(false)) {
                if (Verify.data().getString("codes." + key + ".name").equalsIgnoreCase(player.getName())) return true;
            }
        }
        return false;
    }

    public static int getCodeByPlayer(Player player) {
        if (Verify.data().getConfigurationSection("codes") != null) {
            for (String key : Verify.data().getConfigurationSection("codes").getKeys(false)) {
                if (Verify.data().getString("codes." + key + ".name").equalsIgnoreCase(player.getName()))
                    return Integer.parseInt(key);
            }
        }
        return -1;
    }
}