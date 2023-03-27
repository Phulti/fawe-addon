package de.philippulti.faweaddons.bukkit.commands;

import de.philippulti.faweaddons.bukkit.FaweAddonPlugin;
import de.philippulti.faweaddons.bukkit.utils.HelpMessageBuilder;
import de.philippulti.faweaddons.bukkit.utils.HelpMessagePage;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpMessageCommand extends FaweAddonCommand {

  private final FaweAddonPlugin plugin;
  private final HelpMessageBuilder helpMessageBuilder;

  public HelpMessageCommand(final FaweAddonPlugin plugin, final HelpMessageBuilder helpMessageBuilder) {
    super(plugin, "", true);
    this.plugin = plugin;
    this.helpMessageBuilder = helpMessageBuilder;
  }

  public void onCommand(final CommandSender commandSender, final String label, final String[] args) {
    if (args.length == 1) {
      String key = args[0];
      Player player = (Player) commandSender;
      Audience.audience(player).sendMessage(helpMessageBuilder.buildMultiPageMessage(plugin.helpMessagePageList
          .stream()
          .filter(page -> page.getKey().equalsIgnoreCase(key))
          .findFirst()
          .get()));
    }
  }

}
