package atlasmask.atlasmask.listeners;

import atlasmask.atlasmask.AtlasMask;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlace(org.bukkit.event.block.BlockPlaceEvent event) {
        NBTItem nbtItem = new NBTItem(event.getItemInHand());
        if (nbtItem.hasKey("MASK") || nbtItem.hasKey("EMPTY") || nbtItem.hasKey("EMPTY-USED") || nbtItem.hasKey("ATTACHED")) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', AtlasMask.getPlugin(AtlasMask.class).getConfig().getString("Messages.cant-place-mask")));
            event.setCancelled(true);
        }
    }
}
