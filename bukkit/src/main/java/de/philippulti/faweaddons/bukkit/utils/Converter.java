package de.philippulti.faweaddons.bukkit.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;

public class Converter {

  public static BlockArrayClipboard convertRegionToBlockArrayClipboard(final Player fawePlayer, final CuboidRegion cuboidRegion)
      throws WorldEditException {
    BlockArrayClipboard blockArrayClipboard = new BlockArrayClipboard(cuboidRegion);
    blockArrayClipboard.setOrigin(fawePlayer.getLocation().toVector().toBlockPoint());
    try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory()
        .getEditSession(fawePlayer.getWorld(), -1)) {
      ForwardExtentCopy forwardExtentCopy =
          new ForwardExtentCopy(editSession, cuboidRegion, blockArrayClipboard.getOrigin(),
              blockArrayClipboard, blockArrayClipboard.getOrigin());
      Operations.complete(forwardExtentCopy);
    }
    return blockArrayClipboard;
  }

}
