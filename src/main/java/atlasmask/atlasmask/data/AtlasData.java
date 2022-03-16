package atlasmask.atlasmask.data;

import me.elapsed.universal.AtlasComponent;
import me.elapsed.universal.database.json.serializer.Persist;
import me.elapsed.universal.objects.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AtlasData implements AtlasComponent {
    private static final transient AtlasData INSTANCE = new AtlasData();

    public static ConcurrentHashMap<UUID, ItemStack> masksInUse = new ConcurrentHashMap<>();

    private AtlasData() {}

    public static void load() { Persist.getInstance().loadOrSaveDefault(INSTANCE, AtlasData.class, "MasksInUse"); }
    public static void save() {
        Persist.getInstance().save(INSTANCE, "MasksInUse");
    }
}
