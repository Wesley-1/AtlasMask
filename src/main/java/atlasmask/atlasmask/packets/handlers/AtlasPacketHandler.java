package atlasmask.atlasmask.packets.handlers;

import atlasmask.atlasmask.packets.packets.PacketEvent;
import atlasmask.atlasmask.packets.subscription.EventSubscriptions;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

public class AtlasPacketHandler extends ChannelDuplexHandler {

    private Player player;

    public AtlasPacketHandler(Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PacketEvent<?> event = PacketEvent.get(player, msg);
        if (event == null) {
            super.channelRead(ctx, msg);
            return;
        }
        EventSubscriptions.instance.publish(event);
        if (!event.isCancelled()) {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        PacketEvent<?> event = PacketEvent.get(player, msg);
        if (event == null) {
            super.write(ctx, msg, promise);
            return;
        }
        EventSubscriptions.instance.publish(event);
        if (!event.isCancelled()) {
            super.write(ctx, event.getPacket(), promise);
        }
    }
}