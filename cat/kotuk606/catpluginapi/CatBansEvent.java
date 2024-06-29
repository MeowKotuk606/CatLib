package cat.kotuk606.catpluginapi;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import cat.kotuk606.catpluginapi.Cat.CatBans.Type;

public class CatBansEvent extends Event implements Listener {
	private static final HandlerList handlers = new HandlerList();
	private final Player player;
    private final long time;
    private final String reason;
    private final Type type;
    private final Player admin;
	
    public CatBansEvent(Player admin, Player player, long time, String reason, Type type) {
	    this.player = player;
	    this.time = time;
	    this.reason = reason;
	    this.type = type;
	    this.admin = admin;
	}

    public Player getPlayer() {
        return player;
    }
    
    public Player getAdmin() {
        return admin;
    }
    
    public Type getType() {
    	return type;
    }

    public long getTimeSeconds() {
        return time;
    }
    
    public String getReason() {
        return reason;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
