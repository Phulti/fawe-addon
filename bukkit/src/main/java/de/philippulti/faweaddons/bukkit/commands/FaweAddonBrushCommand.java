package de.philippulti.faweaddons.bukkit.commands;

import de.philippulti.faweaddons.bukkit.FaweAddonPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FaweAddonBrushCommand extends FaweAddonCommand implements TabCompleter {

  public FaweAddonBrushCommand(final FaweAddonPlugin plugin,
                               final String permission, final boolean playerExecutable) {
    super(plugin, permission, playerExecutable);
  }

  @Override
  public void onCommand(final CommandSender commandSender, final String label, final String[] args) {

  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command,
                                              @NotNull final String alias, final @NotNull String[] args) {
    return null;
  }

  private void handleFullcopyBrush() {

  }

  private void handleRotateBrush() {

  }

  private void handleStackerBrush() {

  }

}
