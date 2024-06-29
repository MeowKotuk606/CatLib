package cat.kotuk606.catpluginapi;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;

import cat.kotuk606.catpluginapi.CatBarAPI.CatBarStyle;

public class CatBar {
	private BarColor color;
	private CatBarStyle style;
	private Double progress;
	private String title;
	private BarFlag[] flags;
	
	public CatBar(String title, BarColor color, CatBarStyle style, Double progress, BarFlag[] flags) {
		this.color = color;
		this.style = style;
		this.title = Colors.translateColorCodes(title, "#&");
		this.progress = progress;
		this.flags = flags;
	}
	
	public CatBar(String title, BarColor color, CatBarStyle style, Double progress) {
		this.color = color;
		this.style = style;
		this.title = Colors.translateColorCodes(title, "#&");
		this.progress = progress;
		this.flags = null;
	}
	
	public CatBar(String title, BarColor color, CatBarStyle style, BarFlag[] flags) {
		this.color = color;
		this.style = style;
		this.title = Colors.translateColorCodes(title, "#&");
		this.progress = 100.0;
		this.flags = flags;
	}
	
	public CatBar(String title, BarColor color, CatBarStyle style) {
		this.color = color;
		this.style = style;
		this.title = Colors.translateColorCodes(title, "#&");
		this.progress = 100.0;
		this.flags = null;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public BarColor getColor() {
		return this.color;
	}
	
	public CatBarStyle getStyle() {
		return this.style;
	}
	
	public Double getProgress() {
		return this.progress;
	}
	
	public BarFlag[] getFlags() {
		return this.flags;
	}
	
	public void setColor(BarColor color) {
		this.color = color;
	}
	
	public void setStyle(CatBarStyle style) {
		this.style = style;
	}
	
	public void setProgress(double progress) {
		this.progress = progress;
	}
	
	public void setTitle(String title) {
		this.title = Colors.translateColorCodes(title, "#&");
	}
	
	public void setFlags(BarFlag[] flags) {
		this.flags = flags;
	}
}
