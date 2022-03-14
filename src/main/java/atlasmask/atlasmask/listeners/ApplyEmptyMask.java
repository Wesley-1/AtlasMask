package atlasmask.atlasmask.listeners;

import atlasmask.atlasmask.AtlasMask;
import de.tr7zw.nbtapi.NBTItem;
import lombok.NonNull;
import lombok.var;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApplyEmptyMask implements Listener {

    private final AtlasMask instance;

    public ApplyEmptyMask() {
        instance = AtlasMask.getPlugin(AtlasMask.class);
    }

    @EventHandler
    public void applyEmpty(InventoryClickEvent event) {
        if (event.getCursor() == null || event.getCursor().getType() == Material.AIR) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        NBTItem current = new NBTItem(Objects.requireNonNull(event.getCurrentItem()), true);
        NBTItem cursor = new NBTItem(Objects.requireNonNull(event.getCursor()), true);

        if (current.getItem() == null) return;
        if (cursor.getItem() == null) return;

        if (event.getClick().isLeftClick()) {
            if (!current.hasKey("EMPTY")) return;
            if (!cursor.hasKey("MASK")) return;
            if (current.getInteger("ALLOWED") == 0) return;

            current.setString(String.valueOf(current.getInteger("ALLOWED")), cursor.getString("MASK"));
            current.setInteger("ALLOWED", current.getInteger("ALLOWED") - 1);

            if (cursor.hasKey("ExtraHearts")) {
                if (current.hasKey("ExtraHearts")) {
                    current.setDouble("ExtraHearts", current.getDouble("ExtraHearts") + cursor.getDouble("ExtraHearts"));
                } else {
                    current.setDouble("ExtraHearts", cursor.getDouble("ExtraHearts"));
                }
            }

            if (cursor.hasKey("OutgoingDamage")) {
                if (current.hasKey("OutgoingDamage")) {
                    current.setDouble("OutgoingDamage", current.getDouble("OutgoingDamage") + cursor.getDouble("OutgoingDamage"));
                } else {
                    current.setDouble("OutgoingDamage", cursor.getDouble("OutgoingDamage"));
                }
            }

            if (cursor.hasKey("IncomingDamage")) {
                if (current.hasKey("IncomingDamage")) {
                    current.setDouble("IncomingDamage", current.getDouble("IncomingDamage") + cursor.getDouble("IncomingDamage"));
                } else {
                    current.setDouble("IncomingDamage", cursor.getDouble("IncomingDamage"));
                }
            }

            ItemMeta meta = current.getItem().getItemMeta();

            List<String> lore = new ArrayList<>();

            for (String loreAddition : instance.getConfig().getStringList("Empty.item.lore")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', loreAddition).replace("%1%", current.getString("2")).replace("%2%", current.getString("1")));
            }

            meta.setLore(lore);
            current.getItem().setItemMeta(meta);

            event.setCurrentItem(current.getItem());
            event.setCursor(new ItemStack(Material.AIR));
            event.setCancelled(true);

        }
    }
}
