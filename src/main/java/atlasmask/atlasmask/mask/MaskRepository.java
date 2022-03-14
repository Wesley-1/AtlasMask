package atlasmask.atlasmask.mask;

import lombok.Getter;
import me.elapsed.universal.objects.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class MaskRepository {
    @Getter
    private final HashMap<String, ItemStack> maskMap;
    @Getter
    private final HashMap<String, List<String>> buffs;

    public MaskRepository() {
        maskMap = new HashMap<>();
        buffs = new HashMap<>();
    }
}
