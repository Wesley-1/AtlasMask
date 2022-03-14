package atlasmask.atlasmask.packets.packets;


import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.entity.Player;

public class PacketEventOutEquipment extends PacketEvent<PacketPlayOutEntityEquipment> {

    private PacketPlayOutEntityEquipment packet;

    public PacketEventOutEquipment(Player player, PacketPlayOutEntityEquipment packet) {
        super(player);
        this.packet = packet;
    }

    @Override
    public PacketPlayOutEntityEquipment getPacket() {
        return this.packet;
    }

    @Override
    public void setPacket(PacketPlayOutEntityEquipment packet) {
        this.packet = packet;
    }
}
