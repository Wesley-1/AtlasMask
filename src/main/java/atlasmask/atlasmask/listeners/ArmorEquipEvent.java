package atlasmask.atlasmask.listeners;

import atlasmask.atlasmask.AtlasMask;
import atlasmask.atlasmask.buffs.DamageBuff;
import atlasmask.atlasmask.buffs.HeartsBuff;
import atlasmask.atlasmask.buffs.IncomingDamageBuff;
import atlasmask.atlasmask.data.AtlasData;
import com.sun.tools.javac.jvm.Items;
import me.elapsed.universal.commons.utils.SkullyUtility;
import me.elapsed.universal.nms.NMS;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ArmorEquipEvent implements Listener {

    private final AtlasMask instance;

    public ArmorEquipEvent() {
        this.instance = AtlasMask.getPlugin(AtlasMask.class);
    }

    @EventHandler
    public void onEquip(com.codingforcookies.armorequip.ArmorEquipEvent event) {

        new HeartsBuff().apply(event);
        new DamageBuff().apply(event);
        new IncomingDamageBuff().apply(event);
        new IncomingDamageBuff().remove(event);
        new DamageBuff().remove(event);
        new HeartsBuff().remove(event);

    }
}
