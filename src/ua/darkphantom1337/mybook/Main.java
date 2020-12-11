/*
 * @Author DarkPhantom1337
 * @Version 1.0.0
 */
package ua.darkphantom1337.mybook;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {
	
	
	public void onEnable() {
		try {
			getCommand("mybook").setExecutor(new MyBookCMD(this));
			Bukkit.getConsoleSender().sendMessage(
					"§a[§emyBook§a] §f-> §aPlugin succesfully enabled! // by Darkphantom1337, 2020");
		} catch (Exception e) {
			Bukkit.getConsoleSender()
					.sendMessage("§c[§emyBook§c] §f-> §cError in enabling plugin! Plugin disabled!\nError:"
							+ e.getLocalizedMessage());
			this.setEnabled(false);
		}
	}

	public void onDisable() {
		Bukkit.getConsoleSender()
				.sendMessage("§c[§emyBook§c] §f-> §cPlugin succesfully disabled! // by Darkphantom1337, 2020");
	}
	
	public Boolean bookIsExist(String bookid) {
		return new File(this.getDataFolder() + "/Books", bookid + ".yml").exists();
	}

}