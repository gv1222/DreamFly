package net.dream.DreamFly;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class DreamFly extends JavaPlugin implements Listener {
    public final Logger logger = Logger.getLogger("Logger");

    public void onEnable() {
        logger.info("Dreamfly Enabled");
        getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {
        logger.info("Dreamfly Disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        if (!(sender instanceof Player) ||
                !cmd.getName().toLowerCase().contains("fly") ||
                args.length == 0 || args.length > 2)
            return false;
        if (!player.hasPermission("fly.fly"))
            return false;

        if (args[0].equalsIgnoreCase("on")) {
            player.setAllowFlight(true);
            player.setFlySpeed(0.1F);
            player.setFlying(true);
            player.sendMessage(ChatColor.GOLD + "Set fly mode " + ChatColor.RED + "enabled " + ChatColor.GOLD + "for " + ChatColor.WHITE + player.getDisplayName());
            return true;
        }
        if (args[0].equalsIgnoreCase("off")) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.sendMessage(ChatColor.GOLD + "Set fly mode " + ChatColor.RED + "disabled " + ChatColor.GOLD + "for " + ChatColor.WHITE + player.getDisplayName());
        } else
            return false;
        return true;

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHit(EntityDamageByEntityEvent event) {
        if ((event.getDamager() instanceof Player)) {
            Player damager = (Player) event.getDamager();
            if ((damager.getAllowFlight() &&
                    (!damager.hasPermission("fly.hit")) &&
                    (damager.getGameMode() != GameMode.CREATIVE))) {
                event.setCancelled(true);
                damager.sendMessage(ChatColor.DARK_RED + "Error: PVP is disabled when flying.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if ((event.getPlayer().getAllowFlight()) &&
                (!event.getPlayer().hasPermission("fly.bow")) &&
                (event.getPlayer().getGameMode() != GameMode.CREATIVE)) {

            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (event.getPlayer().getItemInHand().getType().equals(Material.BOW))
                    {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.DARK_RED + "Error: PVP is disabled when flying");
                }
            }
        }
    }
}


