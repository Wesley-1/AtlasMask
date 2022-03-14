package atlasmask.atlasmask.listeners;

import atlasmask.atlasmask.data.AtlasData;
import de.tr7zw.nbtapi.NBTItem;
import me.elapsed.universal.nms.NMS;
import me.elapsed.universal.nms.NMS_1_8_R3;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageEvent implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (AtlasData.masksInUse == null || AtlasData.masksInUse.isEmpty()) return;

        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (AtlasData.masksInUse.containsKey(player.getUniqueId())) {
                if (new NBTItem(AtlasData.masksInUse.get(player.getUniqueId())).hasKey("OutgoingDamage")) {
                    event.setDamage(event.getDamage() + (event.getDamage() * (new NBTItem(AtlasData.masksInUse.get(player.getUniqueId())).getDouble("OutgoingDamage") / 100)));
                }
            }
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (AtlasData.masksInUse.containsKey(player.getUniqueId())) {
                if (new NBTItem(AtlasData.masksInUse.get(player.getUniqueId())).hasKey("IncomingDamage")) {
                    event.setDamage(event.getDamage() - (event.getDamage() * (new NBTItem(AtlasData.masksInUse.get(player.getUniqueId())).getDouble("IncomingDamage") / 100)));
                }
            }
        }
    }
}
