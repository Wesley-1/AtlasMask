package atlasmask.atlasmask.commands.subcommands;

import atlasmask.atlasmask.AtlasMask;
import de.tr7zw.nbtapi.NBTItem;
import me.elapsed.universal.commands.ACommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MaskGive extends ACommand {

    private final AtlasMask instance;

    public MaskGive() {
        this.instance = AtlasMask.getPlugin(AtlasMask.class);
        addAlias("give");
        setPermission("AleriaMasks.Give");
        addAutoComplete("give");
    }

    @Override
    public void perform(CommandSender commandSender, String s, String[] strings) {
        try {
            Player player = (Player) commandSender;

            if (player == null)
                return;

            String mask = strings[1];
            Player player1 = Bukkit.getPlayer(strings[0]);

            if (mask.toLowerCase().equals("empty")) {
                ItemStack givenItem = instance.getEmptyMask();
                NBTItem nbtItem = new NBTItem(givenItem);
                nbtItem.setString(UUID.randomUUID().toString(), UUID.randomUUID().toString());
                player1.getInventory().addItem(nbtItem.getItem());
                player1.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("Messages.mask-given")));
            } else {
                if (instance.getMaskRepository().getMaskMap().containsKey(mask)) {
                    ItemStack givenitem = instance.getMaskRepository().getMaskMap().get(mask);
                    NBTItem nbtItem = new NBTItem(givenitem);
                    nbtItem.setString(UUID.randomUUID().toString(), UUID.randomUUID().toString());
                    player1.getInventory().addItem(nbtItem.getItem());
                    player1.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("Messages.mask-given")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
