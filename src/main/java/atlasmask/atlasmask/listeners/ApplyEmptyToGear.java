package atlasmask.atlasmask.listeners;

import atlasmask.atlasmask.AtlasMask;
import atlasmask.atlasmask.data.AtlasData;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ApplyEmptyToGear implements Listener {

    private final AtlasMask instance;

    public ApplyEmptyToGear() {
        instance = AtlasMask.getPlugin(AtlasMask.class);
    }

    @EventHandler
    public void onApplyEmptyToArmor(InventoryClickEvent event) {
        if (event.getCursor() == null) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        NBTItem current = new NBTItem(Objects.requireNonNull(event.getCurrentItem()), true);

        if (current.getItem() == null) return;

        if (event.isLeftClick()) {
            if (event.getCursor().getType() == Material.AIR) return;
            NBTItem cursor = new NBTItem(Objects.requireNonNull(event.getCursor()), true);
            if (cursor.getItem() == null) return;

            if (current.hasKey("EMPTY")) return;
            if (current.hasKey("MASK")) return;
            if (!cursor.hasKey("EMPTY")) return;
            if (current.hasKey("ATTACHED")) return;
            if (!(cursor.getInteger("ALLOWED") == 0)) return;
            if (!current.getItem().getType().equals(Material.DIAMOND_HELMET)) return;

            ItemMeta meta = current.getItem().getItemMeta();

            List<String> lore;
            if (meta.getLore() == null) {
                lore = new ArrayList<>();
            } else {
                lore = meta.getLore();
            }

            for (String loreAddition : instance.getConfig().getStringList("Empty.lore-when-attached")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', loreAddition).replace("%1%", cursor.getString("2")).replace("%2%", cursor.getString("1")));
            }

            meta.setLore(lore);
            current.getItem().setItemMeta(meta);
            current.getItem().setItemMeta(meta);
            current.setString("ATTACHED", cursor.getString("2") + ":" + cursor.getString("1"));
            current.setString("2", cursor.getString("2"));
            current.setString("1", cursor.getString("1"));
            current.setString("EMPTY-USED", "");

            if (cursor.hasKey("ExtraHearts")) {
                current.setInteger("ExtraHearts", cursor.getInteger("ExtraHearts"));
            }

            if (cursor.hasKey("OutgoingDamage")) {
                current.setInteger("OutgoingDamage", cursor.getInteger("OutgoingDamage"));
            }

            if (cursor.hasKey("IncomingDamage")) {
                current.setInteger("IncomingDamage", cursor.getInteger("IncomingDamage"));
            }

            event.setCancelled(true);
            event.setCurrentItem(current.getItem());
            event.setCursor(new ItemStack(Material.AIR));
        } else {
            if (!current.hasKey("ATTACHED")) return;

            ItemMeta meta = current.getItem().getItemMeta();
            ItemStack newMask1 = instance.getMaskRepository().getMaskMap().get(current.getString("2"));
            ItemStack newMask2 = instance.getMaskRepository().getMaskMap().get(current.getString("1"));
            List<String> lore;

            if (meta.getLore() == null) {
                lore = new ArrayList<>();
            } else {
                lore = meta.getLore();
            }

            for (String loreRemoval : instance.getConfig().getStringList("Empty.lore-when-attached")) {
                lore.remove(ChatColor.translateAlternateColorCodes('&', loreRemoval).replace("%1%", current.getString("2")).replace("%2%", current.getString("1")));
            }

            meta.setLore(lore);
            current.getItem().setItemMeta(meta);

            current.removeKey( "ATTACHED");
            current.removeKey("EMPTY-USED");
            current.removeKey("2");
            current.removeKey("1");


            removeAttributes(current.getItem());
            event.setCancelled(true);
            event.setCurrentItem(current.getItem());

            AtlasData.masksInUse.remove(event.getWhoClicked().getUniqueId());

            NBTItem mask2 = new NBTItem(newMask2);
            NBTItem mask1 = new NBTItem(newMask1);
            NBTItem empty = new NBTItem(instance.getEmptyMask());
            mask2.setString(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            mask1.setString(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            empty.setString(UUID.randomUUID().toString(), UUID.randomUUID().toString());

            event.getWhoClicked().getInventory().addItem(mask1.getItem());
            event.getWhoClicked().getInventory().addItem(mask2.getItem());
            event.getWhoClicked().getInventory().addItem(empty.getItem());
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
