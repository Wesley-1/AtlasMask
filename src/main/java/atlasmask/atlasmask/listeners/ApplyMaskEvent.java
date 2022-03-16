package atlasmask.atlasmask.listeners;

import atlasmask.atlasmask.AtlasMask;
import atlasmask.atlasmask.data.AtlasData;
import com.sun.tools.javac.jvm.Items;
import de.tr7zw.nbtapi.NBTItem;
import dev.dbassett.skullcreator.SkullCreator;
import me.elapsed.universal.commons.utils.SkullyUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApplyMaskEvent implements Listener {
    private final AtlasMask instance;

    public ApplyMaskEvent() {
        instance = AtlasMask.getPlugin(AtlasMask.class);
    }

    @EventHandler
    public void onMaskApply(InventoryClickEvent event) {

        if (event.getCurrentItem() == null ||  event.getCurrentItem().getType() == Material.AIR) return;
        if (event.getCursor() == null) return;
        if (!event.getCurrentItem().getType().equals(Material.DIAMOND_HELMET)) return;

        ItemStack current = event.getCurrentItem();
        NBTItem currentNBT = new NBTItem(current, true);

        if (currentNBT.getItem() == null) return;

        if (event.getClick().isLeftClick()) {
            if (!currentNBT.hasKey("ATTACHED")) {
                if (currentNBT.hasKey("EMPTY-USED")) return;
                if (event.getCursor().getType() == Material.AIR) return;
                NBTItem cursorNBT = new NBTItem(event.getCursor(), true);
                if (cursorNBT.getItem() == null) return;
                if (!cursorNBT.hasKey("MASK")) return;
                ItemMeta meta = currentNBT.getItem().getItemMeta();

                List<String> lore;
                if (meta.getLore() == null) {
                    lore = new ArrayList<>();
                } else {
                    lore = meta.getLore();
                }

                for (String loreAddition : instance.getConfig().getStringList("AtlasMasks." + cursorNBT.getString("MASK") + ".lore-when-attached")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', loreAddition));
                }

                meta.setLore(lore);
                currentNBT.getItem().setItemMeta(meta);
                currentNBT.getItem().setItemMeta(meta);
                currentNBT.setString("ATTACHED", cursorNBT.getString("MASK"));
                currentNBT.removeKey("MASK");

                instance.getMaskRepository().getBuffs().get(currentNBT.getString("ATTACHED")).forEach(o -> currentNBT.setDouble(o.split(":")[0], Double.parseDouble(o.split(":")[1])));

                event.setCurrentItem(currentNBT.getItem());
                event.setCursor(new ItemStack(Material.AIR));
                event.setCancelled(true);
            }
        } else {
            if (!currentNBT.hasKey("ATTACHED")) return;
            if (currentNBT.hasKey("EMPTY-USED")) return;

            ItemMeta meta = currentNBT.getItem().getItemMeta();
            ItemStack newMask = instance.getMaskRepository().getMaskMap().get(currentNBT.getString("ATTACHED"));
            List<String> lore;

            if (meta.getLore() == null) {
                lore = new ArrayList<>();
            } else {
                lore = meta.getLore();
            }

            for (String loreRemoval : instance.getConfig().getStringList("AtlasMasks." + currentNBT.getString("ATTACHED") + ".lore-when-attached")) {
                lore.remove(ChatColor.translateAlternateColorCodes('&', loreRemoval));
            }

            meta.setLore(lore);
            currentNBT.getItem().setItemMeta(meta);

            currentNBT.removeKey( "ATTACHED");
            removeAttributes(current);

            NBTItem maskGiven = new NBTItem(newMask);
            maskGiven.setString(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            AtlasData.masksInUse.remove(event.getWhoClicked().getUniqueId());
            event.setCurrentItem(currentNBT.getItem());
            event.getWhoClicked().getInventory().addItem(maskGiven.getItem());
            event.setCancelled(true);


        }
    }

    public void removeAttributes(ItemStack oldItem) {
        NBTItem nbtItem = new NBTItem(oldItem);
        if (nbtItem.hasKey("ExtraHearts")) {
            nbtItem.removeKey("ExtraHearts");
        } else if (nbtItem.hasKey( "OutgoingDamage")) {
            nbtItem.removeKey("OutgoingDamage");
        } else if (nbtItem.hasKey( "IncomingDamage")) {
            nbtItem.removeKey( "IncomingDamage");
        }
    }

}
