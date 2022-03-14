package atlasmask.atlasmask.buffs.interfaces;


import org.bukkit.event.Event;

public interface Buff<O extends Event> {

    void apply(O event);
    void remove(O event);
}
