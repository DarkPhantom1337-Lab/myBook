/*
 * @Author DarkPhantom1337
 * @Version 1.0.0
 */
package ua.darkphantom1337.mybook;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class BookFile {

	private FileConfiguration filecfg;
	private Main plugin;
	private File file;
	private String filename;
	private String pluginname;

	public BookFile(Main plugin, String bookid) {
		this.plugin = plugin;
		this.filename = bookid + ".yml";
		this.pluginname = plugin.getName();
		setupBookFile();
		if (getBookFile().isSet(pluginname))
			saveBookFile();
		else
			firstFill();
	}

	private void setupBookFile() {
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		String dirpath = plugin.getDataFolder() + File.separator + "Books";
		if (!new File(dirpath).exists())
			new File(dirpath).mkdir();
		file = new File(dirpath, filename);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error in creating book file " + filename + "!");
				e.printStackTrace();
			}
		filecfg = YamlConfiguration.loadConfiguration(file);
	}

	private FileConfiguration getBookFile() {
		return filecfg;
	}

	public void saveBookFile() {
		try {
			filecfg.save(file);
		} catch (IOException localIOException) {
			System.out.println("Error in saving book file " + filename + "!");
		}
	}

	public void reloadBookFile() {
		filecfg = YamlConfiguration.loadConfiguration(file);
	}

	private void firstFill() {
		getBookFile().set(pluginname, " File: " + filename + " || Author: DarkPhantom1337");
		saveBookFile();
	}
	
	public void delete() {
		this.file.delete();
	}

	public void saveBook(ItemStack book_item) {
		getBookFile().set("BookItem", book_item);
		saveBookFile();
	}

	public ItemStack getBook() {
		return getBookFile().getItemStack("BookItem");
	}

	public String getStr(String path) {
		return getBookFile().getString(path);
	}

}
