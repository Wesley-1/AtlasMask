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
    }

    @EventSubscription
    private void onPacket(PacketEventOutEquipment event) {
        PacketPlayOutEntityEquipment packet = event.getPacket();
        try {
            ItemStack equipment = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_8_R3.ItemStack) itemField.get(packet));

          //  SkinInfo skinInfo = SkinInfo.read(equipment);
        //    if (skinInfo != null) {
          //      for (Skin skin : skinInfo.getSkins()) {
             //       if (skin.getEquipDisguise() != null) {

             //   itemField.set(packet, CraftItemStack.asNMSCopy(equipment));
                //    }
             //   }
          //  }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}