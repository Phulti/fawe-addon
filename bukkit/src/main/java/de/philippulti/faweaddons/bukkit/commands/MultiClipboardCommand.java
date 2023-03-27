package de.philippulti.faweaddons.bukkit.commands;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.SessionManager;
import de.philippulti.faweaddons.bukkit.FaweAddonPlugin;
import de.philippulti.faweaddons.bukkit.utils.Converter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class MultiClipboardCommand extends FaweAddonCommand implements TabCompleter {

  private final FaweAddonPlugin plugin;
  private final WorldEdit worldEdit;

  public MultiClipboardCommand(final FaweAddonPlugin plugin, final String permission, final boolean playerExecutable,
                               final WorldEdit worldEdit) {
    super(plugin, permission, playerExecutable);
    this.plugin = plugin;
    this.worldEdit = worldEdit;
  }

  @Override
  public void onCommand(final CommandSender commandSender, final String label, final String[] args)
      throws WorldEditException {
    org.bukkit.entity.Player player = (org.bukkit.entity.Player) commandSender;
    Player fawePlayer = BukkitAdapter.adapt(player);
    SessionManager sessionManager = WorldEdit.getInstance().getSessionManager();
    LocalSession localSession = sessionManager.get(fawePlayer);
    if (args.length == 0) {

    }

    if (args.length == 1) {
      if (args[0].equalsIgnoreCase("list")) {
        this.handleList(player, fawePlayer);
      } else {
        Audience.audience(player).sendMessage(Component.text()
            .append(Component.text("The subcommand ").color(NamedTextColor.RED))
            .append(Component.text(args[0]).color(NamedTextColor.GOLD))
            .append(Component.text(" does not exist!").color(NamedTextColor.RED)));
      }
    }

    if (args.length == 2) {
      if (args[0].equalsIgnoreCase("save")) {
        handleSave(player, fawePlayer, localSession, args[1]);
      } else {
        Audience.audience(player).sendMessage(Component.text()
            .append(Component.text("The subcommand ").color(NamedTextColor.RED))
            .append(Component.text(args[0]).color(NamedTextColor.GOLD))
            .append(Component.text(" does not exist!").color(NamedTextColor.RED)));
      }
    }
    return;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command,
                                              @NotNull final String alias, final @NotNull String[] args) {
    org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
    if (args.length == 1) {
      List<String> tabComplete = new ArrayList<>();
      tabComplete.add("save");
      tabComplete.add("load");
      tabComplete.add("paste");
      tabComplete.add("delete");
      tabComplete.add("list");
      tabComplete.removeIf(s -> !s.toLowerCase().contains(args[0].toLowerCase()));
      return tabComplete;
    }

    if (args.length == 2 &&
        (args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("paste") || args[0].equalsIgnoreCase("delete"))) {
      List<String> tabComplete = new ArrayList<>();
      Path path = Paths.get(worldEdit.getConfiguration().getWorkingDirectoryPath().toUri());
      File playerClipboard = new File("" + path + "/multiclipboards/" + player.getUniqueId());
      if (playerClipboard.exists()) {
        Arrays.stream(playerClipboard.listFiles())
            .forEach(f -> tabComplete.add(f.getName().substring(0, f.getName().length() - 3)));
        return tabComplete;
      }
    }

    return null;
  }

  private void handleSave(final org.bukkit.entity.Player player, final Player fawePlayer,
                          final LocalSession localSession, final String clipboardName)
      throws WorldEditException {
    if (localSession.getSelection(localSession.getSelectionWorld()) != null &&
        localSession.getSelection(localSession.getSelectionWorld()) instanceof CuboidRegion) {
      CuboidRegion cuboidRegion = (CuboidRegion) localSession.getSelection(localSession.getSelectionWorld());
      BlockArrayClipboard blockArrayClipboard = Converter.convertRegionToBlockArrayClipboard(fawePlayer, cuboidRegion);
      this.saveClipboardToFile(player, fawePlayer, clipboardName.toUpperCase(), blockArrayClipboard);
    } else {
      Audience.audience(player).sendMessage(Component.text()
          .append(Component.text("This command only works with ").color(NamedTextColor.RED))
          .append(Component.text(" Cuboid Selections").color(NamedTextColor.GOLD))
          .append(Component.text("!").color(NamedTextColor.RED)));
    }
  }

  private void handleLoad() {

  }

  private void handlePaste() {

  }

  private void handleDelete() {

  }

  private void handleList(final org.bukkit.entity.Player player, final Player fawePlayer) {
    Path path = Paths.get(worldEdit.getConfiguration().getWorkingDirectoryPath().toUri());
    Logger.getLogger("FaweAddons").info("FilePath: " + path);
    File clipboardFile = new File("" + path + "/multiclipboards/" + fawePlayer.getUniqueId());
    if (clipboardFile.exists()) {
      if (clipboardFile.isDirectory()) {
        List<Component> components = new ArrayList<>();
        components.add(plugin.getPrefix());
        components.add(Component.text()
            .append(Component.text("The following MultiClipboards are available: \n").color(NamedTextColor.GRAY))
            .asComponent());
        File[] files = clipboardFile.listFiles();
        Arrays.stream(files).forEach(f -> components
            .add(Component.text()
                .append(Component.text(f.getName().substring(0, f.getName().length() - 3)).color(NamedTextColor.GOLD))
                .append(Component.text(", ").color(NamedTextColor.GRAY))
                .asComponent()));
        components.stream().forEach(c -> Audience.audience(player).sendMessage(c));
      }
    }
  }

  private void saveClipboardToFile(final org.bukkit.entity.Player player, final Player fawePlayer, final String name,
                                   final Clipboard clipboard) {
    try {
      Path path = Paths.get(worldEdit.getConfiguration().getWorkingDirectoryPath().toUri());
      Logger.getLogger("FaweAddons").info("FilePath: " + path);
      File clipboardFile =
          new File("" + path + "/multiclipboards/" + fawePlayer.getUniqueId(), name + ".bd");
      if (clipboardFile.exists()) {
        clipboardFile.delete();
      } else {
        clipboardFile =
            this.createFile(path + "/multiclipboards/" + fawePlayer.getUniqueId(), name + ".bd");
      }

      Logger.getLogger("FaweAddons").info("ClipboardFilepath:" + clipboardFile.getAbsolutePath());
      try (ClipboardWriter clipboardWriter = BuiltInClipboardFormat.SPONGE_SCHEMATIC
          .getWriter(new BufferedOutputStream(new FileOutputStream(clipboardFile)))) {
        clipboardWriter.write(clipboard);

        Audience.audience(player).sendMessage(Component.text()
            .append(plugin.getPrefix())
            .append(Component.text("You saved your current selection into the MultiClipboard named: ")
                .color(NamedTextColor.GRAY))
            .append(Component.text(name).color(NamedTextColor.GOLD))
            .append(Component.text("!").color(NamedTextColor.GRAY)));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public File createFile(final String dir, final String name) throws IOException {
    File file = new File(String.valueOf(dir), name);
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }
    if (!file.exists()) {
      file.createNewFile();
      return file;
    } else {
      return null;
    }
  }

}
