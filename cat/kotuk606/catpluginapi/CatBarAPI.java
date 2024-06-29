package cat.kotuk606.catpluginapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class CatBarAPI {
	private static Map<Player,List<CatBar>> playerCatBars = new HashMap<>();
	
	public enum CatBarStyle {
		PROGRESS,
		NOTCHED_6,
		NOTCHED_10,
		NOTCHED_12,
		NOTCHED_20
	}
	
	public static CatBar createBar(String title, BarColor color, CatBarStyle style, Double progress, BarFlag[] flags) {
		return new CatBar(title,color,style,progress,flags);
	}
	
	public static CatBar createBar(String title, BarColor color, CatBarStyle style, Double progress) {
		return new CatBar(title,color,style,progress);
	}
	
	public static CatBar createBar(String title, BarColor color, CatBarStyle style, BarFlag[] flags) {
		return new CatBar(title,color,style,flags);
	}
	
    public static CatBar createBar(String title, BarColor color, CatBarStyle style) {
    	return new CatBar(title,color,style);
	}
    
    public static BossBar catbarToBossbar(CatBar bar) {
    	BossBar bbar;
    	if(bar.getFlags() == null) {
    		bbar = Bukkit.createBossBar(bar.getTitle(), bar.getColor(), catBarStyleToBarStyle(bar.getStyle()), bar.getFlags());
    	} else {
    		bbar = Bukkit.createBossBar(bar.getTitle(), bar.getColor(), catBarStyleToBarStyle(bar.getStyle()));
    	}
    	bbar.setProgress(bar.getProgress());
    	return bbar;
    }
    
    public static void displayCatbar(CatBar bar, Player p) {
    	BossBar bbar = catbarToBossbar(bar);
    	if(!bbar.getPlayers().contains(p)) {
    		bbar.addPlayer(p);
    		List<CatBar> bars = playerCatBars.get(p);
    		bars.add(bar);
    		playerCatBars.put(p, bars);
    		bbar.setVisible(true);
    	}
    	bbar.setVisible(true);
    }
    
    public static void hideCatbar(CatBar bar, Player p) {
    	BossBar bbar = catbarToBossbar(bar);
    	bbar.setVisible(false);
    	if(bbar.getPlayers().contains(p)) {
    		bbar.removePlayer(p);
    		List<CatBar> bars = playerCatBars.get(p);
    		bars.remove(bar);
    		playerCatBars.put(p, bars);
    	}
    }
    
    public static CatBar updateCatbar(CatBar bar, Player p, String title, BarColor color, CatBarStyle style, Double progress, BarFlag[] flags) {
    	List<CatBar> bars = playerCatBars.get(p);
		bars.remove(bar);
		BossBar bbar = catbarToBossbar(bar);
		bbar.setVisible(false);
		bbar.removePlayer(p);
    	bar.setColor(color);
    	bar.setFlags(flags);
    	bar.setProgress(progress);
    	bar.setStyle(style);
    	bar.setTitle(title);
    	bbar = catbarToBossbar(bar);
    	bbar.addPlayer(p);
    	playerCatBars.put(p, bars);
    	bbar.setVisible(true);
    	return bar;
    }
    
    public static CatBar updateCatbar(CatBar bar, Player p, String title, BarColor color, CatBarStyle style, Double progress) {
    	List<CatBar> bars = playerCatBars.get(p);
		bars.remove(bar);
		BossBar bbar = catbarToBossbar(bar);
		bbar.setVisible(false);
		bbar.removePlayer(p);
    	bar.setColor(color);
    	bar.setProgress(progress);
    	bar.setStyle(style);
    	bar.setTitle(title);
    	bbar = catbarToBossbar(bar);
    	bbar.addPlayer(p);
    	playerCatBars.put(p, bars);
    	bbar.setVisible(true);
    	return bar;
    }
    
    public static List<CatBar> getPlayerCatbars(Player p) {
    	List<CatBar> bars = playerCatBars.get(p);
    	return bars;
    }
    
    public static BarStyle catBarStyleToBarStyle(CatBarStyle s) {
    	if(s == CatBarStyle.PROGRESS) {
    		return BarStyle.SOLID;
    	} else if(s == CatBarStyle.NOTCHED_6) {
    		return BarStyle.SEGMENTED_6;
    	} else if(s == CatBarStyle.NOTCHED_10) {
    		return BarStyle.SEGMENTED_10;
    	} else if(s == CatBarStyle.NOTCHED_12) {
    		return BarStyle.SEGMENTED_12;
    	} else if(s == CatBarStyle.NOTCHED_20) {
    		return BarStyle.SEGMENTED_20;
    	}
    	return null;
    }
}
