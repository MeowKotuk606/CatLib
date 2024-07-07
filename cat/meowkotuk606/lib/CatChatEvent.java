package cat.meowkotuk606.lib;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class CatChatEvent extends Event implements Listener {
	private static final HandlerList handlers = new HandlerList();
	private final Player player;
    private final String rawmessage;
    private final String message;
    private final ChatType type;
	
    public CatChatEvent(Player player, String rawmessage, String message, ChatType type) {
	    this.player = player;
	    this.rawmessage = rawmessage;
	    this.message = message;
	    this.type = type;
	}

    public Player getPlayer() {
        return player;
    }
    
    public ChatType getType() {
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
