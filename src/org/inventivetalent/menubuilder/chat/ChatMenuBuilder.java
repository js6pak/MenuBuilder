package org.inventivetalent.menubuilder.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.inventivetalent.menubuilder.MenuBuilder;
import org.inventivetalent.menubuilder.MenuBuilderPlugin;

import java.util.ArrayList;
import java.util.List;

public class ChatMenuBuilder extends MenuBuilder<BaseComponent[]> {

	private List<BaseComponent> lines        = new ArrayList<>();
	private List<String>        listenerKeys = new ArrayList<>();

	private List<LineCallback> callbackLines = new ArrayList<>();

	private List<HumanEntity> viewers = new ArrayList<>();

	/**
	 * Construct a new ChatMenuBuilder
	 */
	public ChatMenuBuilder() {
		super();
	}

	/**
	 * Add lines
	 *
	 * @param builders Array of {@link LineBuilder}
	 * @return the ChatMenuBuilder
	 */
	public ChatMenuBuilder withLine(LineBuilder... builders) {
		for (LineBuilder builder : builders) {
			listenerKeys.addAll(builder.listenerKeys);
			builder.withContainer(this);
			this.lines.add(builder.build());
		}
		return this;
	}

	/**
	 * Add a line at the specified index
	 *
	 * @param index   Position of the line
	 * @param builder {@link LineBuilder} to add
	 * @return the ChatMenuBuilder
	 */
	public ChatMenuBuilder withLine(int index, LineBuilder builder) {
		listenerKeys.addAll(builder.listenerKeys);
		builder.withContainer(this);
		if (lines.size() <= index) { this.lines.add(index, builder.build()); } else { this.lines.set(index, builder.build()); }
		return this;
	}

	/**
	 * Add lines
	 *
	 * @param lines Array of {@link String} lines to add
	 * @return the ChatMenuBuilder
	 */
	public ChatMenuBuilder withLine(String... lines) {
		for (String line : lines) {
			this.lines.add(new TextComponent(line));
		}
		return this;
	}

	/**
	 * Add a line using a {@link LineCallback}
	 * The callback will be called when {@link #refreshContent()} is called
	 *
	 * @param callback {@link LineCallback}
	 * @return the ChatMenuBuilder
	 */
	public ChatMenuBuilder withLine(LineCallback callback) {
		callbackLines.add(callback);
		return this;
	}

	/**
	 * Shows the menu to the viewers
	 *
	 * @param viewers Array of {@link HumanEntity}
	 * @return the ChatMenuBuilder
	 */
	@Override
	public ChatMenuBuilder show(HumanEntity... viewers) {
		BaseComponent[] components = build();
		for (HumanEntity viewer : viewers) {
			if (viewer instanceof Player) {
				for (BaseComponent component : components) {
					((Player) viewer).spigot().sendMessage(component);
				}
				if (!this.viewers.contains(viewer)) {
					this.viewers.add(viewer);
				}
			}
		}
		return this;
	}

	/**
	 * Refresh the menu
	 * Will call all {@link LineCallback}s registered with {@link #withLine(LineCallback)}
	 *
	 * @return the ChatMenuBuilder
	 */
	@Override
	public ChatMenuBuilder refreshContent() {
		for (LineCallback callback : callbackLines) {
			int index = callback.getIndex();
			LineBuilder builder = callback.getLine();

			withLine(index, builder);
		}

		for (HumanEntity viewer : this.viewers) {
			show(viewer);
		}
		return this;
	}

	/**
	 * Builds the menu
	 *
	 * @return Array of {@link BaseComponent}
	 */
	@Override
	public BaseComponent[] build() {
		return this.lines.toArray(new BaseComponent[this.lines.size()]);
	}

	/**
	 * Removes all registered listener for this builder
	 */
	@Override
	public void dispose() {
		for (String key : this.listenerKeys) {
			MenuBuilderPlugin.instance.chatCommandListener.unregisterListener(key);
		}
	}
}
