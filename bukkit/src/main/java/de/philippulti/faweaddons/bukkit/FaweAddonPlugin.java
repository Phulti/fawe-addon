package de.philippulti.faweaddons.bukkit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import de.philippulti.faweaddons.bukkit.commands.FaweAddonTestCommand;
import de.philippulti.faweaddons.bukkit.commands.HelpMessageCommand;
import de.philippulti.faweaddons.bukkit.commands.MultiClipboardCommand;
import de.philippulti.faweaddons.bukkit.utils.HelpMessageBuilder;
import de.philippulti.faweaddons.bukkit.utils.HelpMessagePage;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FaweAddonPlugin extends JavaPlugin {

  public List<HelpMessagePage> helpMessagePageList;
  @Getter
  private Component prefix =
      Component.text().append(Component.text("[").color(NamedTextColor.GRAY))
          .append(Component.text("Fawe-Addons").color(NamedTextColor.GOLD))
          .append(Component.text("] ").color(NamedTextColor.GRAY))
          .asComponent();
  private WorldEditPlugin worldEditPlugin;
  private HelpMessageBuilder helpMessageBuilder;

  public void onEnable() {
    this.worldEditPlugin = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
    helpMessagePageList = new ArrayList<>();
    helpMessageBuilder = new HelpMessageBuilder(this);
    registerCommands();
  }

  private void registerCommands() {
    FaweAddonTestCommand faweAddonTestCommand = new FaweAddonTestCommand(this, helpMessageBuilder);
    HelpMessageCommand helpMessageCommand = new HelpMessageCommand(this, helpMessageBuilder);
    MultiClipboardCommand multiClipboardCommand =
        new MultiClipboardCommand(this, "", true, worldEditPlugin.getWorldEdit());
    Objects.requireNonNull(getCommand("faweaddontest")).setExecutor(faweAddonTestCommand);
    Objects.requireNonNull(getCommand("faweaddontest")).setTabCompleter(faweAddonTestCommand);
    Objects.requireNonNull(getCommand("helpmessage")).setExecutor(helpMessageCommand);
    Objects.requireNonNull(getCommand("multiclipboard")).setExecutor(multiClipboardCommand);
    Objects.requireNonNull(getCommand("multiclipboard")).setTabCompleter(multiClipboardCommand);
  }

}
