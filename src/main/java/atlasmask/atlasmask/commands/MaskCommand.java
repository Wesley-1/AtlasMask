package atlasmask.atlasmask.commands;

import atlasmask.atlasmask.AtlasMask;
import atlasmask.atlasmask.commands.subcommands.MaskGive;
import atlasmask.atlasmask.menus.MaskMenu;
import me.elapsed.universal.commands.ACommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MaskCommand extends ACommand {

    private final AtlasMask instance;

    public MaskCommand() {
        this.instance = AtlasMask.getPlugin(AtlasMask.class);
        addAlias("mask", "atlasmask", "aleriamask");
        setConsoleSender(false);
        setPermission("AleriaMasks.Main");

        Arrays.asList(new MaskGive()
        ).forEach(this::addSubcommand);
    }

    @Override
    public void perform(CommandSender commandSender, String s, String[] strings) {
        try {
            Player player = (Player) commandSender;

            if (player == null)
                return;

            new MaskMenu(player, instance).displayMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
