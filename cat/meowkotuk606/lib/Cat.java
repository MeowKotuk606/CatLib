package cat.meowkotuk606.lib;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Cat extends JavaPlugin {
	private static Map<String, Plugin> plugins = new HashMap<>();
	public static FileConfiguration configCATCHAT;
	public static FileConfiguration messagesCATCHAT;
	private static BukkitTask tsk;
	public static String CatChat_dir;
	public static Cat cat;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new CatChatEvent(null, null, null, null), this);
		getServer().getPluginManager().registerEvents(new CatDSEvent(null, null, null, null), this);
		hook();
		if (isConnected("CatChat")) {
			Plugin CatPlugin = getConnected("CatChat");
			if (CatPlugin == null) {
				CatChat_dir = "not_connected";
			} else {
				CatChat_dir = CatPlugin.getDataFolder().getAbsolutePath();
				hookCatChat(CatPlugin);
			}
		}
		cat = this;
	}

	public static boolean isConnected(String pluginName) {
		return plugins.containsKey(pluginName);
	}

	public static Plugin getConnected(String pluginName) {
		return plugins.get(pluginName);
	}
	
	public static int getUpdate() {
		return 1;
	}

	private void hook() {
		PluginManager b = Bukkit.getPluginManager();
		Plugin[] pll = b.getPlugins();
		for (Plugin pl : pll) {
			String q = pl.getName();
			if (q.startsWith("Cat")) {
				if (!q.equals("CatLib") && pl.getDescription().getAuthors().contains("MeowKotuk606")) {
					Bukkit.getServer().getLogger()
							.info(ChatColor.DARK_GREEN + "Hooked new plugin: " + ChatColor.GOLD + q);
					plugins.put(q, pl);
				}
			}
		}
	}

	private void hookCatChat(Plugin catchat) {
		if (catchat == null) {
			return;
		}
		tsk = Bukkit.getScheduler().runTaskTimer(this, () -> {
			if (catchat.isEnabled()) {
				File messagesFile = new File(CatChat_dir, "messages.yml");
				if (!messagesFile.exists()) {
				} else {
					try (FileReader reader = new FileReader(messagesFile)) {
						messagesCATCHAT = YamlConfiguration.loadConfiguration(reader);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				File cFile = new File(CatChat_dir, "config.yml");
				if (!cFile.exists()) {
				} else {
					try (FileReader reader = new FileReader(cFile)) {
						configCATCHAT = YamlConfiguration.loadConfiguration(reader);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (messagesCATCHAT != null && configCATCHAT != null) {
					tsk.cancel();
				}
			} else if (!catchat.isEnabled()) {
				Bukkit.getPluginManager().enablePlugin(catchat);
			}
		}, 0L, 100L);
	}

	public static String getDate() {
		TimeZone moscowTimeZone = TimeZone.getTimeZone("Europe/Moscow");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		dateFormat.setTimeZone(moscowTimeZone);
		Date currentDate = new Date();
		String w = dateFormat.format(currentDate);
		return w;
	}

	public static int getMonth(int moplus) {
		ZoneId zoneId = ZoneId.of("Europe/Moscow");
		ZonedDateTime currentDateTimeInMoscow = ZonedDateTime.now(zoneId);
		LocalDate date = currentDateTimeInMoscow.toLocalDate();
		if (moplus != 0) {
			date = date.plusMonths(moplus);
		}
		int d = date.lengthOfMonth();
		return d;
	}
}
