package <Your Package>;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class main extends JavaPlugin implements Listener {
	
	public static Map<Player, Integer> tagged = new HashMap<Player, Integer>();
	
	public static main plugin;

	@Override
	public void onEnable() {
		plugin = this;
		getLogger().info("Plugin enabled! :D");
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Plugin disabled. :(");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ct")) {
			if (sender instanceof Player) {
				if (isTagged((Player) sender)) {
					String secs = String.valueOf(tagged.get((Player) sender));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lCT &8| &7You are tagged for &c&l" + secs + " &7more seconds!"));
				}
				else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lCT &8| &7You are &aNOT &7currently Combat Tagged&7!"));
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "Only players can use this command.");
			}
		}
		return false;
	}
	
	public static boolean isTagged(Player p) {
		if (tagged.containsKey(p)) {return true;}
		else {return false;}		
	}
	
	public static void tag(Player p) {
		tagged.put(p, 30);
		
		new BukkitRunnable() {
	        
            @Override
            public void run() {
            	if (tagged.containsKey(p)) {
            		if (tagged.get(p).equals(1)) {
            			tagged.remove(p);
            			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lCT &8| &7You are &aNO LONGER &7Combat Tagged&7!"));
            			this.cancel();								
            		}
            		else {
            			int current = tagged.get(p);
            			tagged.remove(p);
            			tagged.put(p, current - 1);
            		}
            	}
            	else {
            		this.cancel();
            	}
            }
            
        }.runTaskTimerAsynchronously(plugin, 0, 20);
	}
	
	public static void resetTag(Player p) {
		tagged.remove(p);
		tagged.put(p, 30);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player d = (Player) e.getDamager();
			Player p = (Player) e.getEntity();
		
			if (<PVP Timer Check>) {return;}
			else {
				if (isTagged(d)) {resetTag(d);}
				else {
					d.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lCT &8| &7You have been &cCombat Tagged&7!"));
					tag(d);
				}
				
				if (isTagged(p)) {resetTag(p);}
				else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lCT &8| &7You have been &cCombat Tagged&7!"));
					tag(p);
				}
			}
			
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (isTagged(p)) {p.setHealth(0);}
		else {return;}
	}
	
}
