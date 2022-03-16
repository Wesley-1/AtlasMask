package atlasmask.atlasmask.menus;

import atlasmask.atlasmask.AtlasMask;
import me.elapsed.universal.commons.objects.Placeholder;
import me.elapsed.universal.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public class MaskMenu extends Menu {
    private final JavaPlugin plugin;
    private final AtlasMask instance;
    private final Player player;

    public MaskMenu(Player player, JavaPlugin instance) {
        super(player, "MASK_MENU", instance);
        this.plugin = instance;
        this.player = player;
        this.instance = AtlasMask.getPlugin(AtlasMask.class);
    }

    @Override
    public void run() {
        int i = 0;
        for (ItemStack itemStack : instance.getMaskRepository().getMaskMap().values()) {
            this.setItem(i, itemStack, (inventoryClickEvent -> player.closeInventory()));
            i++;
        };
        this.fillSlots();
    }
}
