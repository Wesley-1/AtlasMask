package atlasmask.atlasmask.packets.injector;

import atlasmask.atlasmask.packets.handlers.AtlasPacketHandler;
import atlasmask.atlasmask.packets.subscription.EventSubscription;
import atlasmask.atlasmask.packets.subscription.EventSubscriptions;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EquipmentInjector {

    public EquipmentInjector() {
        EventSubscriptions.instance.subscribe(this, getClass());
    }

    @EventSubscription
    private void onJoin(PlayerJoinEvent event) {
        inject(event.getPlayer());
    }

    @EventSubscription
    private void onLeave(PlayerQuitEvent event) {
        unInject(event.getPlayer());
    }

    private void inject(Player player) {
        //Inject a packet listener into the player's connection.
        CraftPlayer craftPlayer = (CraftPlayer) player;
        PlayerConnection connection = craftPlayer.getHandle().playerConnection;
        connection.networkManager.channel.eventLoop().submit(() -> {
            if (connection.networkManager.channel.pipeline().get("atlas_packet_listener") != null) {
                //Remove it.
                connection.networkManager.channel.pipeline().remove("atlas_packet_listener");

            }
            //Add it.
            connection.networkManager.channel.pipeline().addBefore("packet_handler", "atlas_packet_listener", new AtlasPacketHandler(player));
        });
    }

    private void unInject(Player player) {
        //Remove the packet listener from the player's connection.
        CraftPlayer craftPlayer = (CraftPlayer) player;
        PlayerConnection connection = craftPlayer.getHandle().playerConnection;
        connection.networkManager.channel.eventLoop().submit(() -> {
            if (connection.networkManager.channel.pipeline().get("atlas_packet_listener") != null) {
                //Remove it.
                connection.networkManager.channel.pipeline().remove("atlas_packet_listener");
            }
        });
    }
}

