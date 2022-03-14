package atlasmask.atlasmask;

import atlasmask.atlasmask.data.AtlasData;
import atlasmask.atlasmask.listeners.*;
import atlasmask.atlasmask.mask.MaskRepository;
import atlasmask.atlasmask.packets.ManipulateSkull;
import atlasmask.atlasmask.packets.injector.EquipmentInjector;
import atlasmask.atlasmask.packets.subscription.EventSubscriptions;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import lombok.var;
import me.elapsed.universal.nms.NMS;
import me.elapsed.universal.nms.NMS_1_8_R3;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class AtlasMask extends JavaPlugin {

    @Getter private MaskRepository maskRepository;
    @Getter private EquipmentInjector injector;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.maskRepository = new MaskRepository();
        setupMasks();
        new EventSubscriptions(this);
        injector = new EquipmentInjector();
        Bukkit.getPluginManager().registerEvents(new ArmorEquipEvent(), this);
        Bukkit.getPluginManager().registerEvents(new DamageEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ApplyMaskEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ApplyEmptyMask(), this);
        Bukkit.getPluginManager().registerEvents(new ApplyEmptyToGear(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void setupMasks() {
        for (String key : getConfig().getConfigurationSection("AtlasMasks").getKeys(false)) {
            // ItemBuilder maskItem = new ItemBuilder(this, "AtlasMasks." + key + ".item");
         //   SkullyUtility.getCustomSkull("AtlasMasks." + key + ".item.url")
            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM);
            String maskDisplayName = getConfig().getString("AtlasMasks." + key + ".item.title");
            List<String> loreList = getConfig().getStringList("AtlasMasks." + key + ".item.lore");
            List<String> maskBuffs = getConfig().getStringList("AtlasMasks." + key + ".buffs");

            ItemMeta meta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();

            for (String string : loreList) {
                lore.add(ChatColor.translateAlternateColorCodes('&', string));
            }

            meta.setLore(lore);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', maskDisplayName));
            itemStack.setItemMeta(meta);

            NBTItem nms = new NBTItem(itemStack, true);
            nms.setString("MASK", key);

            maskBuffs.forEach(o -> nms.setDouble(o.split(":")[0], Double.parseDouble(o.split(":")[1])));

            getMaskRepository().getMaskMap().put(key, nms.getItem());
            getMaskRepository().getBuffs().put(key, maskBuffs);
        }
    }

    public ItemStack getEmptyMask() {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM);
        String maskDisplayName = getConfig().getString("Empty.item.title");
        List<String> loreList = getConfig().getStringList("Empty.item.lore");
        int numberOfMasksToHold = 2;

        NBTItem nms = new NBTItem(itemStack, true);

        nms.setString("EMPTY", "");
        nms.setInteger("ALLOWED", numberOfMasksToHold);
        nms.setString("2", "Empty");
        nms.setString("1", "Empty");

        ItemMeta meta = nms.getItem().getItemMeta();
        List<String> lore = new ArrayList<>();

        for (String string : loreList) {
            lore.add(ChatColor.translateAlternateColorCodes('&', string).replace("%1%", nms.getString("2")).replace("%2%", nms.getString("1")));
        }

        meta.setLore(lore);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', maskDisplayName));
        nms.getItem().setItemMeta(meta);


        return nms.getItem();
    }

}
