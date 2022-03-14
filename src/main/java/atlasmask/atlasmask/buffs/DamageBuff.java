package atlasmask.atlasmask.buffs;

import atlasmask.atlasmask.AtlasMask;
import atlasmask.atlasmask.buffs.interfaces.Buff;
import atlasmask.atlasmask.data.AtlasData;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import de.tr7zw.nbtapi.NBTItem;
import me.elapsed.universal.commons.utils.SkullyUtility;
import me.elapsed.universal.nms.NMS;
import me.elapsed.universal.nms.NMS_1_8_R3;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DamageBuff implements Buff<ArmorEquipEvent> {

    private final AtlasMask instance;

    public DamageBuff() {
        this.instance = AtlasMask.getPlugin(AtlasMask.class);
    }

    @Override
    public void apply(ArmorEquipEvent event) {
        if(event.getNewArmorPiece() != null && event.getNewArmorPiece().getType() != Material.AIR) {
            if (!new NBTItem(event.getNewArmorPiece()).hasKey( "ATTACHED")) return;
            if (!new NBTItem(event.getNewArmorPiece()).hasKey( "OutgoingDamage")) return;
            if (AtlasData.masksInUse.containsKey(event.getPlayer().getUniqueId())) return;

            AtlasData.masksInUse.put(event.getPlayer().getUniqueId(), event.getNewArmorPiece());
            AtlasData.entityIdMap.put(event.getPlayer().getEntityId(), event.getPlayer());
        }
    }

    @Override
    public void remove(ArmorEquipEvent event) {
        if(event.getOldArmorPiece() != null && event.getOldArmorPiece().getType() != Material.AIR) {
            if (!new NBTItem(event.getOldArmorPiece()).hasKey( "ATTACHED")) return;
            if (!new NBTItem(event.getOldArmorPiece()).hasKey( "OutgoingDamage")) return;
            AtlasData.masksInUse.remove(event.getPlayer().getUniqueId());
            AtlasData.entityIdMap.remove(event.getPlayer().getEntityId(), event.getPlayer());
        }
    }
}
