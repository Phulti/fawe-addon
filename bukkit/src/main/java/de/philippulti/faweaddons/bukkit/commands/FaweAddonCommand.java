package de.philippulti.faweaddons.bukkit.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEditException;
import de.philippulti.faweaddons.bukkit.FaweAddonPlugin;
import lombok.SneakyThrows;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public abstract class FaweAddonCommand implements CommandExecutor {

  private final FaweAddonPlugin plugin;
  private final String permission;
  private final boolean playerExecutable;

  public FaweAddonCommand(final FaweAddonPlugin plugin, final String permission, final boolean playerExecutable) {
    this.plugin = plugin;
    this.permission = permission;
    this.playerExecutable = playerExecutable;
  }

  @SneakyThrows
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (this.playerExecutable) {
      if (sender instanceof ConsoleCommandSender) {
        Audience.audience(sender).sendMessage(Component.text().append(plugin.getPrefix())
            .append(Component.text("Only Players can perform this command!")
                .color(NamedTextColor.RED)));
        return false;
      }

      if (sender.hasPermission(permission)) {
        onCommand(sender, label, args);
      } else {
        Audience.audience(sender).sendMessage(Component.text().append(plugin.getPrefix())
            .append(Component.text("You do not have enough permissions to perform this command!")
                .color(NamedTextColor.RED)));
      }
    }
    return true;
  }

  public abstract void onCommand(CommandSender commandSender, String label, String[] args)
      throws WorldEditException;

}
