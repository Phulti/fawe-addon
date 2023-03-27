package de.philippulti.faweaddons.bukkit.utils;

import de.philippulti.faweaddons.bukkit.FaweAddonPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;

public class HelpMessageBuilder {

  private final FaweAddonPlugin plugin;

  public HelpMessageBuilder(final FaweAddonPlugin plugin) {
    this.plugin = plugin;
  }

  public Component buildMultiPageMessage(final HelpMessagePage page) {
    if (page.getPreviousPage() == null && page.getNextPage() == null) {
      return handleSinglePage(page);
    }

    if (page.getPreviousPage() != null && page.getNextPage() == null) {
      return this.handlePreviousPage(page);
    }

    if (page.getPreviousPage() == null && page.getNextPage() != null) {
      return this.handleNextPage(page);
    }

    if (page.getPreviousPage() != null && page.getNextPage() != null) {
      return this.handleBothPages(page);
    }
    return Component.text()
        .append(Component.text("Unknown Page Type! (Error: buildMultiPageMessage(36))").color(NamedTextColor.RED))
        .asComponent();
  }

  private Component handleSinglePage(final HelpMessagePage page) {
    TextComponent component = Component.text().append(plugin.getPrefix())
        .append(Component.text(" " + page.getTitle()).color(NamedTextColor.GRAY))
        .build();

    StringBuilder builder = new StringBuilder();
    Arrays.stream(page.getContent()).forEach(s -> builder.append(System.lineSeparator() + "- " + s));

    return Component.text().append(component.asComponent())
        .append(Component.text().append(Component.text(builder.toString()).color(NamedTextColor.GRAY))).asComponent();
  }

  private Component handlePreviousPage(final HelpMessagePage page) {
    TextComponent component = Component.text().append(plugin.getPrefix())
        .append(Component.text(" " + page.getTitle()).color(NamedTextColor.GRAY))
        .build();

    StringBuilder builder = new StringBuilder();
    Arrays.stream(page.getContent()).forEach(s -> builder.append(System.lineSeparator()).append("- ").append(s));

    return Component.text().append(component.asComponent())
        .append(createPreviousPageButton(page).asComponent())
        .append(Component.text().append(Component.text(builder.toString()).color(NamedTextColor.GRAY))).asComponent();
  }

  private Component handleNextPage(final HelpMessagePage page) {
    TextComponent component = Component.text().append(plugin.getPrefix())
        .append(Component.text(" " + page.getTitle()).color(NamedTextColor.GRAY))
        .build();

    StringBuilder builder = new StringBuilder();
    Arrays.stream(page.getContent()).forEach(s -> builder.append(System.lineSeparator()).append("- ").append(s));

    return Component.text().append(component.asComponent())
        .append(createNextPageButton(page).asComponent())
        .append(Component.text().append(Component.text(builder.toString()).color(NamedTextColor.GRAY))).asComponent();
  }

  private Component handleBothPages(final HelpMessagePage page) {
    TextComponent component = Component.text().append(plugin.getPrefix())
        .append(Component.text(" " + page.getTitle()).color(NamedTextColor.GRAY))
        .build();

    StringBuilder builder = new StringBuilder();
    Arrays.stream(page.getContent()).forEach(s -> builder.append(System.lineSeparator()).append("- ").append(s));

    return Component.text().append(component.asComponent())
        .append(createPreviousPageButton(page).asComponent())
        .append(createNextPageButton(page).asComponent())
        .append(Component.text().append(Component.text(builder.toString()).color(NamedTextColor.GRAY))).asComponent();
  }

  private TextComponent createNextPageButton(final HelpMessagePage page) {
    return Component.text().append(Component.text(" >> To the next page").color(NamedTextColor.RED)
        .clickEvent(ClickEvent.runCommand("/helpmessage " + page.getNextPage().getKey()))).hoverEvent(HoverEvent
        .showText(Component.text().append(Component.text("Go to the next page: ").color(NamedTextColor.GRAY)
            .append(Component.text(page.getNextPage().getKey()).color(NamedTextColor.GRAY))))).build();
  }

  private TextComponent createPreviousPageButton(final HelpMessagePage page) {
    return Component.text().append(Component.text(" << To the previous page").color(NamedTextColor.RED)
        .clickEvent(ClickEvent.runCommand("/helpmessage " + page.getPreviousPage().getKey()))).hoverEvent(HoverEvent
        .showText(Component.text().append(Component.text("Go to the previous page: ").color(NamedTextColor.GRAY)
            .append(Component.text(page.getPreviousPage().getKey()).color(NamedTextColor.GRAY))))).build();
  }

}
