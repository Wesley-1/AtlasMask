package atlasmask.atlasmask.buffs;

import atlasmask.atlasmask.AtlasMask;
import atlasmask.atlasmask.buffs.interfaces.Buff;
import atlasmask.atlasmask.data.AtlasData;
import atlasmask.atlasmask.packets.ManipulateSkull;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import de.tr7zw.nbtapi.NBTItem;
import dev.dbassett.skullcreator.SkullCreator;
import me.elapsed.universal.nms.NMS;
import me.elapsed.universal.nms.NMS_1_8_R3;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HeartsBuff implements Buff<ArmorEquipEvent> {

    private final AtlasMask instance;

    public HeartsBuff() {
        this.instance = AtlasMask.getPlugin(AtlasMask.class);
    }

    @Override
    public void apply(ArmorEquipEvent event) {
        event.getPlayer().getInventory().addItem(instance.getMaskRepository().getMaskMap().get("Test"));
        if(event.getNewArmorPiece() != null && event.getNewArmorPiece().getType() != Material.AIR) {
            if (!new NBTItem(event.getNewArmorPiece()).hasKey( "ATTACHED")) return;
            if (!new NBTItem(event.getNewArmorPiece()).hasKey( "ExtraHearts")) return;
            if (AtlasData.masksInUse.containsKey(event.getPlayer().getUniqueId())) return;
            AtlasData.masksInUse.put(event.getPlayer().getUniqueId(), event.getNewArmorPiece());
            event.getPlayer().setMaxHealth(20 + new NBTItem(event.getNewArmorPiece()).getDouble("ExtraHearts"));
            AtlasData.entityIdMap.put(event.getPlayer().getEntityId(), event.getPlayer());
        }
    }

    @Override
    public void remove(ArmorEquipEvent event) {
        if(event.getOldArmorPiece() != null && event.getOldArmorPiece().getType() != Material.AIR) {
            if (!new NBTItem(event.getOldArmorPiece()).hasKey("ATTACHED")) return;
            if (!new NBTItem(event.getOldArmorPiece()).hasKey("ExtraHearts")) return;
            event.getPlayer().resetMaxHealth();
            AtlasData.masksInUse.remove(event.getPlayer().getUniqueId());
            AtlasData.entityIdMap.remove(event.getPlayer().getEntityId(), event.getPlayer());

        }
    }
}
