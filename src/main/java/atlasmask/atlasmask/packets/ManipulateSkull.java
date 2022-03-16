package atlasmask.atlasmask.packets;

import atlasmask.atlasmask.AtlasMask;
import atlasmask.atlasmask.data.AtlasData;
import atlasmask.atlasmask.packets.packets.PacketEventOutEquipment;
import atlasmask.atlasmask.packets.subscription.EventSubscription;
import atlasmask.atlasmask.packets.subscription.EventSubscriptions;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.utils.ReflectionUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import me.elapsed.universal.commons.utils.SkullyUtility;
import me.elapsed.universal.nms.NMS;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ManipulateSkull {

    private static Field entityId;
    private static Field slotField;
    private static Field itemField;
    private final AtlasMask instance;

    static {
        try {
            slotField = PacketPlayOutEntityEquipment.class.getDeclaredField("b");
            itemField = PacketPlayOutEntityEquipment.class.getDeclaredField("c");
            entityId = PacketPlayOutEntityEquipment.class.getDeclaredField("a");
            slotField.setAccessible(true);
            itemField.setAccessible(true);
            entityId.setAccessible(true);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public ManipulateSkull() {
        EventSubscriptions.instance.subscribe(this, getClass());
        this.instance = AtlasMask.getPlugin(AtlasMask.class);
    }

    @EventSubscription
    private void onPacket(PacketEventOutEquipment event) throws IllegalAccessException {
        PacketPlayOutEntityEquipment packet = event.getPacket();
        try {
            ItemStack equipment = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_8_R3.ItemStack) itemField.get(packet));

           if (equipment.getType() == Material.AIR) return;

            NBTItem item = new NBTItem(equipment);

            if (!item.hasKey("ATTACHED")) return;
            if (item.hasKey("EMPTY-USED")) {
                itemField.set(packet, CraftItemStack.asNMSCopy(SkullyUtility.getCustomSkull(instance.getConfig().getString("Empty.skull-url"))));
            } else {
                if (item.getItem() == null) return;
                System.out.println("TEST");
                itemField.set(packet, CraftItemStack.asNMSCopy(SkullyUtility.getCustomSkull(instance.getConfig().getString("AtlasMasks." + item.getString("ATTACHED") + ".item.skull-url"))));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}