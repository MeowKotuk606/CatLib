package cat.kotuk606.catpluginapi;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class Cat extends JavaPlugin implements CommandExecutor {
	private static Map<String, Plugin> plugins = new HashMap<>();
	public static FileConfiguration banCfg;
	public static FileConfiguration muteCfg;
	public static File banFile;
	public static File muteFile;
	public static String CatBans_dir;
	public static FileConfiguration configCATBANS;
	public static FileConfiguration configCATCHAT;
	public static FileConfiguration messagesCATCHAT;
	private static BukkitTask tsk;
	public static String CatChat_dir;
	public static JavaPlugin cat;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new CatChatEvent(null, null, null, null), this);
		getServer().getPluginManager().registerEvents(new CatDSEvent(null, null, null, null), this);
		getServer().getPluginManager().registerEvents(new CatBansEvent(null, null, 0, null, null), this);
		getCommand("catpluginsreload").setExecutor(this);
		hookCatPlugins();
		if (isConnected("CatBans")) {
			Plugin CatPlugin = getConnected("CatBans");
			if (CatPlugin == null) {
				CatBans_dir = "not_connected";
			} else {
				CatBans_dir = CatPlugin.getDataFolder().getAbsolutePath();
				hookCatBans(CatPlugin);
			}
		}
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

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		if (cmd.getName().equalsIgnoreCase("catpluginsreload")) {
			if (sender.hasPermission("cat")) {
				sender.sendMessage(Colors.translateColorCodes("&2Cat плагины успешно перезагружены", "#"));
				reloadCatPlugins();
				return true;
			}
		}
		return false;
	}

	public static boolean isConnected(String pluginName) {
		return plugins.containsKey(pluginName);
	}

	public void reloadCatPlugins() {
		PluginManager m = Bukkit.getPluginManager();
		for (Plugin p : plugins.values()) {
			m.disablePlugin(p);
			tsk = Bukkit.getScheduler().runTaskTimer(this, () -> {
				if (!m.isPluginEnabled(p)) {
					m.enablePlugin(p);
				}
			}, 0L, 100L);
		}
	}

	public static Plugin getConnected(String pluginName) {
		return plugins.get(pluginName);
	}

	private void hookCatPlugins() {
		PluginManager b = Bukkit.getPluginManager();
		Plugin[] pll = b.getPlugins();
		for (Plugin pl : pll) {
			String q = pl.getName();
			if (q.startsWith("Cat")) {
				if (!q.equals("CatLib") && pl.getDescription().getAuthors().contains("Kotuk606")) {
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

	private void hookCatBans(Plugin catbans) {
		if (catbans == null) {
			return;
		}
		tsk = Bukkit.getScheduler().runTaskTimer(this, () -> {
			if (catbans.isEnabled()) {
				banFile = new File(CatBans_dir, "ban.yml");
				if (!banFile.exists()) {
					try {
						Files.createFile(banFile.toPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				banCfg = YamlConfiguration.loadConfiguration(banFile);
				muteFile = new File(CatBans_dir, "mute.yml");
				if (!muteFile.exists()) {
					try {
						Files.createFile(muteFile.toPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				muteCfg = YamlConfiguration.loadConfiguration(muteFile);
				File configFile = new File(CatBans_dir, "config.yml");
				if (!configFile.exists()) {
				} else {
					try (FileReader reader = new FileReader(configFile)) {
						configCATBANS = YamlConfiguration.loadConfiguration(reader);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (banCfg != null && muteCfg != null && configCATBANS != null) {
					tsk.cancel();
				}
			} else if (!catbans.isEnabled()) {
				Bukkit.getPluginManager().enablePlugin(catbans);
			}
		}, 0L, 100L);
	}

	public static boolean isMute(String name) {
		if (!isConnected("CatBans")) {
			return false;
		}
		return muteCfg.contains(name);
	}

	@SuppressWarnings("unchecked")
	public static boolean isPvp(Player p) {
		if (isConnected("CatAntiRelog")) {
			try {
				Plugin ar = plugins.get("CatAntiRelog");
				Class<?> pl = ar.getClass();
				Field pvpField = pl.getDeclaredField("pvp");
				pvpField.setAccessible(true);
				List<Player> pvpList = (List<Player>) pvpField.get(ar);
				return pvpList.contains(p);
			} catch (Exception e1) {
			}
		}
		return false;
	}

	public static boolean isBan(String name) {
		if (!isConnected("CatBans")) {
			return false;
		}
		return banCfg.contains(name);
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

	public enum ChatType {
		LOCAL, PRIVATE_LOCAL, GLOBAL, PRIVATE_GLOBAL, PRIVATE
	}

	public enum DSType {
		Discord, Minecraft
	}

	public class CatBans {
		public enum Type {
			KICK, BAN, MUTE
		}

		public static void AddBan(Player admin, Player p, String reason, long unTime) {
			if (!isConnected("CatBans")) {
				return;
			}
			String playerSection = p.getName();
			String w = getDate();
			banCfg.set(playerSection + ".reason", reason);
			banCfg.set(playerSection + ".banTime", w);
			banCfg.set(playerSection + ".timeToUnban", 0);
			banCfg.set(playerSection + ".unBanTime", unTime);
			banCfg.set(playerSection + ".moder", admin.getName());
			try {
				banCfg.save(banFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			p.kickPlayer(reason);
			long[] time = { 0 };
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncRepeatingTask(cat, () -> {
				time[0]++;
				if (time[0] >= unTime) {
					banCfg.set(playerSection, null);
					try {
						banCfg.save(banFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (time[0] < unTime) {
					banCfg.set(playerSection + ".timeToUnban", time[0]);
					try {
						banCfg.save(banFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, 20L, 20L);
		}

		public static void AddMute(Player admin, Player p, String reason, long unTime) {
			if (!isConnected("CatBans")) {
				return;
			}
			String playerSection = p.getName();
			String w = getDate();
			muteCfg.set(playerSection + ".reason", reason);
			muteCfg.set(playerSection + ".muteTime", w);
			muteCfg.set(playerSection + ".timeToUnmute", 0);
			muteCfg.set(playerSection + ".unMuteTime", unTime);
			muteCfg.set(playerSection + ".moder", admin.getName());
			try {
				muteCfg.save(muteFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			long[] time = { 0 };
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncRepeatingTask(cat, () -> {
				time[0]++;
				if (time[0] == unTime) {
					muteCfg.set(playerSection, null);
					try {
						muteCfg.save(muteFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (time[0] < unTime) {
					muteCfg.set(playerSection + ".timeToUnmute", time[0]);
					try {
						muteCfg.save(muteFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, 20L, 20L);
		}

		public static void RemBan(Player p) {
			if (!isConnected("CatBans")) {
				return;
			}
			String playerSection = p.getName();
			banCfg.set(playerSection, null);
			try {
				banCfg.save(banFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public static void RemMute(Player p) {
			if (!isConnected("CatBans")) {
				return;
			}
			String playerSection = p.getName();
			muteCfg.set(playerSection, null);
			try {
				muteCfg.save(muteFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public static String parseUnTime(long time, long timeTo) {
			if (!isConnected("CatBans")) {
				return "error";
			}
			if (time == 0) {
				return configCATBANS.getString("catbans.format_untime.perm");
			}
			long untime = time - timeTo;
			long sec = 0;
			long min = 0;
			long ho = 0;
			long d = 0;
			long week = 0;
			long mo = 0;
			long y = 0;
			if (untime >= 60) {
				long i = (long) (untime / 60);
				min = i;
				long h = i * 60;
				sec = untime - h;
			}
			if (min >= 60) {
				long i = (long) (min / 60);
				ho = i;
				long h = i * 60;
				min = min - h;
			}
			if (ho >= 24) {
				long i = (long) (ho / 24);
				d = i;
				long h = i * 24;
				ho = ho - h;
			}
			if (d >= 7) {
				long i = (long) (d / 7);
				week = i;
				long h = i * 7;
				d = d - h;
			}
			boolean w = true;
			int forPlus = 0;
			int days = getMonth(0);
			while (w) {
				forPlus++;
				if (d >= days) {
					d = d - days;
					mo++;
					days = getMonth(forPlus);
				} else {
					w = false;
				}
			}
			String yf = configCATBANS.getString("catbans.format_untime.year");
			String mof = configCATBANS.getString("catbans.format_untime.month");
			String wf = configCATBANS.getString("catbans.format_untime.week");
			String df = configCATBANS.getString("catbans.format_untime.day");
			String hf = configCATBANS.getString("catbans.format_untime.hour");
			String mf = configCATBANS.getString("catbans.format_untime.minute");
			String sf = configCATBANS.getString("catbans.format_untime.secund");
			StringBuilder ret = new StringBuilder();
			if (y != 0) {
				yf.replaceAll("%y%", String.valueOf(y));
			} else {
				yf = null;
			}
			if (mo != 0) {
				mof.replaceAll("%mo%", String.valueOf(mo));
			} else {
				mof = null;
			}
			if (week != 0) {
				wf.replaceAll("%w%", String.valueOf(week));
			} else {
				wf = null;
			}
			if (d != 0) {
				df.replaceAll("%d%", String.valueOf(d));
			} else {
				df = null;
			}
			if (ho != 0) {
				hf.replaceAll("%h%", String.valueOf(ho));
			} else {
				hf = null;
			}
			if (min != 0) {
				mf.replaceAll("%m%", String.valueOf(min));
			} else {
				mf = null;
			}
			if (sec != 0) {
				sf.replaceAll("%s%", String.valueOf(sec));
			} else {
				sf = null;
			}
			if (yf != null) {
				ret.append(yf);
			}
			if (mof != null) {
				ret.append(mof);
			}
			if (wf != null) {
				ret.append(wf);
			}
			if (df != null) {
				ret.append(df);
			}
			if (hf != null) {
				ret.append(hf);
			}
			if (mf != null) {
				ret.append(mf);
			}
			if (sf != null) {
				ret.append(sf);
			}
			return ret.toString();
		}

		public static String getUnTime(Player p, Type type) {
			if (!isConnected("CatBans")) {
				return "CatBans не подключен";
			}
			String playerSection = p.getName();
			long timeTo;
			long time;
			if (type == Type.BAN) {
				timeTo = (long) banCfg.get(playerSection + ".timeToUnban");
				time = (long) banCfg.get(playerSection + ".unBanTime");
			} else if (type == Type.MUTE) {
				timeTo = (long) muteCfg.get(playerSection + ".timeToUnmute");
				time = (long) muteCfg.get(playerSection + ".unMuteTime");
			} else {
				return "Ошибка";
			}
			if (time == 0) {
				return configCATBANS.getString("catbans.format_untime.perm");
			}
			long untime = time - timeTo;
			long sec = 0;
			long min = 0;
			long ho = 0;
			long d = 0;
			long week = 0;
			long mo = 0;
			long y = 0;
			if (untime >= 60) {
				long i = (long) (untime / 60);
				min = i;
				long h = i * 60;
				sec = untime - h;
			}
			if (min >= 60) {
				long i = (long) (min / 60);
				ho = i;
				long h = i * 60;
				min = min - h;
			}
			if (ho >= 24) {
				long i = (long) (ho / 24);
				d = i;
				long h = i * 24;
				ho = ho - h;
			}
			if (d >= 7) {
				long i = (long) (d / 7);
				week = i;
				long h = i * 7;
				d = d - h;
			}
			boolean w = true;
			int forPlus = 0;
			int days = getMonth(0);
			while (w) {
				forPlus++;
				if (d >= days) {
					d = d - days;
					mo++;
					days = getMonth(forPlus);
				} else {
					w = false;
				}
			}
			String yf = configCATBANS.getString("catbans.format_untime.year");
			String mof = configCATBANS.getString("catbans.format_untime.month");
			String wf = configCATBANS.getString("catbans.format_untime.week");
			String df = configCATBANS.getString("catbans.format_untime.day");
			String hf = configCATBANS.getString("catbans.format_untime.hour");
			String mf = configCATBANS.getString("catbans.format_untime.minute");
			String sf = configCATBANS.getString("catbans.format_untime.secund");
			StringBuilder ret = new StringBuilder();
			if (y != 0) {
				yf.replaceAll("%y%", String.valueOf(y));
			} else {
				yf = null;
			}
			if (mo != 0) {
				mof.replaceAll("%mo%", String.valueOf(mo));
			} else {
				mof = null;
			}
			if (week != 0) {
				wf.replaceAll("%w%", String.valueOf(week));
			} else {
				wf = null;
			}
			if (d != 0) {
				df.replaceAll("%d%", String.valueOf(d));
			} else {
				df = null;
			}
			if (ho != 0) {
				hf.replaceAll("%h%", String.valueOf(ho));
			} else {
				hf = null;
			}
			if (min != 0) {
				mf.replaceAll("%m%", String.valueOf(min));
			} else {
				mf = null;
			}
			if (sec != 0) {
				sf.replaceAll("%s%", String.valueOf(sec));
			} else {
				sf = null;
			}
			if (yf != null) {
				ret.append(yf);
			}
			if (mof != null) {
				ret.append(mof);
			}
			if (wf != null) {
				ret.append(wf);
			}
			if (df != null) {
				ret.append(df);
			}
			if (hf != null) {
				ret.append(hf);
			}
			if (mf != null) {
				ret.append(mf);
			}
			if (sf != null) {
				ret.append(sf);
			}
			return ret.toString();
		}
	}
}
