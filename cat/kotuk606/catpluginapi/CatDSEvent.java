package cat.kotuk606.catpluginapi;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import cat.kotuk606.catpluginapi.Cat.DSType;

public class CatDSEvent extends Event implements Listener {
	private static final HandlerList handlers = new HandlerList();
	private final String user;
    private final String rawmessage;
    private final String message;
    private final DSType type;
	
    public CatDSEvent(String user, String rawmessage, String message, DSType type) {
	    this.user = user;
	    this.rawmessage = rawmessage;
	    this.message = message;
	    this.type = type;
	}

    public String getUserName() {
        return user;
    }
    
    public DSType getType() {
    	return type;
    }

    public String getRawMessage() {
        return rawmessage;
    }
    
    public String getMessage() {
        return message;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
