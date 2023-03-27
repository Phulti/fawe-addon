package de.philippulti.faweaddons.bukkit.commands;

import de.philippulti.faweaddons.bukkit.FaweAddonPlugin;
import de.philippulti.faweaddons.bukkit.utils.HelpMessageBuilder;
import de.philippulti.faweaddons.bukkit.utils.HelpMessagePage;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FaweAddonTestCommand extends FaweAddonCommand implements TabCompleter {

  private final FaweAddonPlugin plugin;
  private final HelpMessageBuilder helpMessageBuilder;

  public FaweAddonTestCommand(final FaweAddonPlugin plugin, final HelpMessageBuilder helpMessageBuilder) {
    super(plugin, "", true);
    this.plugin = plugin;
    this.helpMessageBuilder = helpMessageBuilder;
  }

  public void onCommand(final CommandSender commandSender, final String label,
                        final String[] args) {
    Player player = (Player) commandSender;
    if (args[0].equalsIgnoreCase("multiPageMessages")) {
      testMultiPageMessages(player, args);
    } else {
      player.sendMessage(ChatColor.RED + "Unknown Testcase!");
    }
    return;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command,
                                              @NotNull final String alias, final @NotNull String[] args) {
    List<String> tabComplete = new ArrayList<>();
    if (args.length == 1) {
      tabComplete.add("multiPageMessages");
      tabComplete.removeIf(s -> !s.toLowerCase(Locale.ROOT).contains(args[0].toLowerCase(Locale.ROOT)));
    }

    if (args.length == 2 && args[0].equalsIgnoreCase("multiPageMessages")) {
      tabComplete.clear();
      tabComplete.add("singlePage");
      tabComplete.add("withPreviousPage");
      tabComplete.add("withNextPage");
      tabComplete.add("bothPages");
      tabComplete.removeIf(s -> !s.toLowerCase(Locale.ROOT).contains(args[1].toLowerCase(Locale.ROOT)));
    }
    return tabComplete;
  }

  private void testMultiPageMessages(final Player player, final String args[]) {
    if (args[1].equalsIgnoreCase("singlePage")) {
      HelpMessagePage helpMessagePage =
          new HelpMessagePage(plugin, "HELPMESSAGE_TEST_SINGLEPAGE_MAIN", "Dies ist ein Single-Page Test!",
              new String[] {"ABC", "DEF", "GHI"}, null, null);
      Audience.audience(player).sendMessage(helpMessageBuilder.buildMultiPageMessage(helpMessagePage));
      return;
    }

    if (args[1].equalsIgnoreCase("withPreviousPage")) {
      HelpMessagePage previousHelpMessagePage =
          new HelpMessagePage(plugin, "HELPMESSAGE_TEST_WITHPREVIOUS_PREVIOUS",
              "Dies ist ein Previous-Page Test! (Previous)\n",
              new String[] {"ABC ", "DEF ", "GHI "}, null, null);
      HelpMessagePage helpMessagePage =
          new HelpMessagePage(plugin, "HELPMESSAGE1_TEST_WITHPREVIOUS_MAIN",
              "Dies ist ein Previous-Page Test! (Main)\n",
              new String[] {"JKL ", "MNO ", "PQR "}, previousHelpMessagePage, null);
      previousHelpMessagePage.setNextPage(helpMessagePage);
      helpMessagePage.setPreviousPage(previousHelpMessagePage);
      Audience.audience(player).sendMessage(helpMessageBuilder.buildMultiPageMessage(helpMessagePage));
      return;
    }

    if (args[1].equalsIgnoreCase("withNextPage")) {
      HelpMessagePage helpMessagePage =
          new HelpMessagePage(plugin, "HELPMESSAGE_TEST_WITHNEXT_MAIN", "Dies ist ein Next-Page Test! (Main)\n",
              new String[] {"ABC", "DEF", "GHI"}, null, null);
      HelpMessagePage nextHelpMessagePage =
          new HelpMessagePage(plugin, "HELPMESSAGE_TEST_WITHNEXT_NEXT", "Dies ist ein Next-Page Test! (Next)\n",
              new String[] {"JKL", "MNO", "PQR"}, helpMessagePage, null);
      helpMessagePage.setNextPage(nextHelpMessagePage);
      nextHelpMessagePage.setPreviousPage(helpMessagePage);
      Audience.audience(player).sendMessage(helpMessageBuilder.buildMultiPageMessage(helpMessagePage));
      return;
    }

    if (args[1].equalsIgnoreCase("bothPages")) {
      HelpMessagePage previousHelpMessagePage =
          new HelpMessagePage(plugin, "HELPMESSAGE_TEST_WITHBOTH_PREVIOUS",
              "Dies ist ein Both-Pages Test! (Previous)\n",
              new String[] {"ABC", "DEF", "GHI"}, null, null);
      HelpMessagePage helpMessagePage =
          new HelpMessagePage(plugin, "HELPMESSAGE_TEST_WITHBOTH_MAIN", "Dies ist ein Both-Pages Test! (Main)\n",
              new String[] {"JKL", "MNO", "PQR"}, null, null);
      HelpMessagePage nextHelpMessagePage =
          new HelpMessagePage(plugin, "HELPMESSAGE_TEST_WITHBOTH_NEXT", "Dies ist ein Both-Pages Test! (Next)\n",
              new String[] {"STU", "VWX", "YZA"}, null, null);
      previousHelpMessagePage.setNextPage(helpMessagePage);
      helpMessagePage.setPreviousPage(previousHelpMessagePage);
      helpMessagePage.setNextPage(nextHelpMessagePage);
      nextHelpMessagePage.setPreviousPage(helpMessagePage);
      Audience.audience(player).sendMessage(helpMessageBuilder.buildMultiPageMessage(helpMessagePage));
      return;
    }

    Audience.audience(player)
        .sendMessage(Component.text().append(Component.text("There are no other multi-page tests!").color(
            NamedTextColor.RED)));
    return;
  }

}
