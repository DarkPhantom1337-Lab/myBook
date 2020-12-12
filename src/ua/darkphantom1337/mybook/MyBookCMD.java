package ua.darkphantom1337.mybook;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class MyBookCMD implements CommandExecutor {

	private Main plugin;

	public MyBookCMD(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("myBook.admin")) {
			sender.sendMessage("§c[§emyBook§c] §f-> §cУ вас нет доступа к данной команде!");
			return false;
		}
		if (args.length == 0 && sender instanceof Player) {
			sender.sendMessage("§aКоманды плагина myBook:"
					+ "\n§7- /myBook save <id> - §fсохранение завершенной книги которая у Вас в руке в общее хранилище книг. "
					+ "\n§7- /myBook save <id> <author> - §fсохранение завершенной книги которая у Вас в руке в общее хранилище книг. Оригинальный автор книги будет изменён.;"
					+ "\n§7- /myBook edit <id> - §fполучение доступной для редактирования книги; "
					+ "\n§7- /myBook get <id> - §fполучение нередактируемой книги; "
					+ "\n§7- /myBook delete <id> - §fудаление из хранилища сохраненной книги; "
					+ "\n§7- /myBook list - §fсписок имеющихся в хранилище книг; "
					+ "\n§7- /myBook blank - §fполучение пустой, готовой для написания книги;"
					+ "\n§7- /myBook cedit (currentedit) - §fвозможность превратить в редактируемую уже завершенную книгу, находящуюся в руке;"
					+ "\n§7- /myBook version - §fполучение пустой, готовой для написания книги;");
			return true;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("list")) {
				try {
					File booksdir = new File(plugin.getDataFolder() + File.separator + "Books");
					if (!booksdir.exists()) {
						sender.sendMessage("§a[§emyBook§a] §f-> §aВ хранилище ещё нету книг!");
						return false;
					}
					File[] files = booksdir.listFiles();
					String allinfo = "§a[§emyBook§a] §f-> §aКниг в хранилище: §e" + files.length;
					if (files.length == 0) {
						sender.sendMessage("§a[§emyBook§a] §f-> §aВ хранилище ещё нету книг!");
						return false;
					}
					for (File f : files) 
						allinfo += "\n§7- §e" + f.getName().replaceAll(".yml", "");
					sender.sendMessage(allinfo);
					return true;
				} catch (Exception e) {
					sender.sendMessage("§c[§emyBook§c] §f-> §cОшибка при выводе полного списка книг в хранилище."
							+ "\n§cОшибка: §e" + e.getLocalizedMessage());
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("blank")) {
				if (sender instanceof Player) {
					try {
						((Player) sender).getInventory().addItem(new ItemStack(Material.WRITABLE_BOOK, 1));
						sender.sendMessage("§a[§emyBook§a] §f-> §aВам выдана пустая книга.");
						return true;
					} catch (Exception e) {
						sender.sendMessage("§c[§emyBook§c] §f-> §cОшибка при выдаче пустой книги." + "\n§cОшибка: §e"
								+ e.getLocalizedMessage());
						return false;
					}
				} else {
					sender.sendMessage("§c[§emyBook§c] §f-> §cДанная команда доступна только игрокам.");
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("cedit") || args[0].equalsIgnoreCase("currentedit")) {
				if (sender instanceof Player)
					try {
						ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
						if (item != null && item.getType().equals(Material.WRITTEN_BOOK)) {
							item.setType(Material.WRITABLE_BOOK);
							((Player) sender).getInventory().setItemInMainHand(item);
							sender.sendMessage(
									"§a[§emyBook§a] §f-> §aКнига в вашей руке успешно обновлена на редактируемую!");
							return true;
						} else {
							sender.sendMessage(
									"§c[§emyBook§c] §f-> §cЧтобы изменить книгу её нужно держать в главной руке!");
							return false;
						}
					} catch (Exception e) {
						sender.sendMessage("§c[§emyBook§c] §f-> §cОшибка при создании редактируемой книги."
								+ "\n§cОшибка: §e" + e.getLocalizedMessage());
						return false;
					}
				else {
					sender.sendMessage("§c[§emyBook§c] §f-> §cДанная команда доступна только игрокам.");
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("version")) {
				sender.sendMessage("§a[§emyBook§a] §f-> §aВерсия плагина: §e" + plugin.getDescription().getVersion());
				return true;
			}
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("save"))
				return saveBook(sender, args);
			if (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("get")) {
				if (sender instanceof Player) {
					try {
						String bookid = args[1];
						if (plugin.bookIsExist(bookid)) {
							ItemStack book_item = new BookFile(plugin, bookid).getBook();
							if (args[0].equalsIgnoreCase("edit"))
								book_item.setType(Material.WRITABLE_BOOK);
							((Player) sender).getInventory().addItem(book_item);
							sender.sendMessage("§a[§emyBook§a] §f-> §aВам выдана книга §e" + bookid + "§a!");
							return true;
						} else {
							sender.sendMessage(
									"§c[§emyBook§c] §f-> §cКниги с ID §e" + bookid + " §cещё не существует!");
							return false;
						}
					} catch (Exception e) {
						sender.sendMessage("§c[§emyBook§c] §f-> §cОшибка при редактировании книги." + "\n§cОшибка: §e"
								+ e.getLocalizedMessage());
						return false;
					}
				} else {
					sender.sendMessage("§c[§emyBook§c] §f-> §cДанная команда доступна только игрокам.");
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("delete")) {
				try {
					String bookid = args[1];
					if (plugin.bookIsExist(bookid)) {
						new BookFile(plugin, bookid).delete();
						sender.sendMessage("§a[§emyBook§a] §f-> §aКнига §e" + bookid + "§a удалена!");
						return true;
					} else {
						sender.sendMessage("§c[§emyBook§c] §f-> §cКниги с ID §e" + bookid + " §cещё не существует!");
						return false;
					}
				} catch (Exception e) {
					sender.sendMessage("§c[§emyBook§c] §f-> §cОшибка при удалении книги." + "\n§cОшибка: §e"
							+ e.getLocalizedMessage());
					return false;
				}
			}
		}
		if (args.length == 3)
			if (args[0].equalsIgnoreCase("save"))
				return saveBook(sender, args);

		sender.sendMessage("§c[§emyBook§c] §f-> §cТакого аргумента не существует либо Вы не указали дополнительные аргументы. Используйте /myBook для справки.");
		return false;
	}

	private Boolean saveBook(CommandSender sender, String[] args) {
		if (sender instanceof Player)
			try {
				ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
				if (item != null && (item.getType().equals(Material.WRITABLE_BOOK)
						|| item.getType().equals(Material.WRITTEN_BOOK))) {
					String bookid = args[1];
					item.setAmount(1);
					if (args.length == 3) {
						BookMeta meta = (BookMeta) item.getItemMeta();
						meta.setAuthor(args[2]);
						item.setItemMeta(meta);
					}
					new BookFile(plugin, bookid).saveBook(item);
					if (plugin.bookIsExist(bookid))
						sender.sendMessage("§a[§emyBook§a] §f-> §aКнига §e" + bookid + " §aуспешно сохранена!");
					else
						sender.sendMessage("§a[§emyBook§a] §f-> §aКнига §e" + bookid + " §aуспешно обновлена!");
					return true;
				} else {
					sender.sendMessage("§c[§emyBook§c] §f-> §cЧтобы сохранить книгу её нужно держать в главной руке!");
					return false;
				}
			} catch (Exception e) {
				sender.sendMessage("§c[§emyBook§c] §f-> §cОшибка при сохранении книги." + "\n§cОшибка: §e"
						+ e.getLocalizedMessage());
				return false;
			}
		else {
			sender.sendMessage("§c[§emyBook§c] §f-> §cДанная команда доступна только игрокам.");
			return false;
		}
	}

}
