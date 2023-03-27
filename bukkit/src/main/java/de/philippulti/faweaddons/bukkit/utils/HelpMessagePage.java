package de.philippulti.faweaddons.bukkit.utils;

import de.philippulti.faweaddons.bukkit.FaweAddonPlugin;
import lombok.Data;

@Data
public class HelpMessagePage {

  private final FaweAddonPlugin plugin;
  private final String key;
  private final String title;
  private final String[] content;
  private HelpMessagePage previousPage;
  private HelpMessagePage nextPage;

  public HelpMessagePage(final FaweAddonPlugin plugin, final String key, final String title, final String[] content,
                            final HelpMessagePage previousPage, final HelpMessagePage nextPage) {
    this.plugin = plugin;
    this.key = key;
    this.title = title;
    this.content = content;
    this.previousPage = previousPage;
    this.nextPage = nextPage;
    getPlugin().helpMessagePageList.add(this);

  }

  public int calcAllPages() {
    HelpMessagePage tempPrevious = this;
    while (tempPrevious.getPreviousPage() != null) {
      tempPrevious = tempPrevious.getPreviousPage();
    }
    int amount = 1;
    while (tempPrevious.getNextPage() != null) {
      tempPrevious = tempPrevious.getNextPage();
      amount++;
    }
    return amount;
  }

  public int calcPageNumber() {
    int amount = 0;
    HelpMessagePage tempPreviousPage = previousPage;
    while (tempPreviousPage != null) {
      tempPreviousPage = previousPage.getPreviousPage();
      amount++;
    }
    return Math.abs(amount - calcAllPages());
  }

  public int calcNextPages() {
    int amount = 1;
    HelpMessagePage tempNextPage = nextPage;
    while (tempNextPage != null) {
      tempNextPage = nextPage.getNextPage();
      amount++;
    }
    return amount;
  }

}
