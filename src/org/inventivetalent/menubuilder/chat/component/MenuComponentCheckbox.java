package org.inventivetalent.menubuilder.chat.component;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.inventivetalent.menubuilder.ValueListener;
import org.inventivetalent.menubuilder.chat.ChatListener;
import org.inventivetalent.menubuilder.chat.LineBuilder;

/**
 * Checkbox MenuComponent
 * States are either <code>true</code> or <code>false</code> (checked/unchecked) returned by {@link #isChecked()}
 * By default is rendered either as <i>[✔]</i> or <i>[✖]</i>
 */
public class MenuComponentCheckbox extends MenuComponent {

	public static final String DEFAULT_FORMAT = " [%s] ";
	public static final String YES            = "✔";
	public static final String NO             = "✖";
	public static final String EMPTY          = " ";

	private boolean checked;
	private String format          = DEFAULT_FORMAT;
	private String stringChecked   = YES;
	private String stringUnchecked = NO;

	private ValueListener<Boolean> valueListener;

	/**
	 * Construct a new Checkbox
	 */
	public MenuComponentCheckbox() {
		this(false);
	}

	/**
	 * Construct a new Checkbox
	 *
	 * @param checked if <code>true</code> the checkbox will be checked
	 */
	public MenuComponentCheckbox(boolean checked) {
		this.checked = checked;
		updateText();
	}

	/**
	 * Changes the default format (<i> [%s] </i>)
	 *
	 * @param format the new format, must contain a variable (e.g. <i>%s</i>
	 * @return the Checkbox
	 */
	public MenuComponentCheckbox withFormat(String format) {
		this.format = format;
		updateText();
		return this;
	}

	/**
	 * Changes the default <b>checked</b> string (<i>✔</i>)
	 *
	 * @param stringChecked the new string
	 * @return the Checkbox
	 */
	public MenuComponentCheckbox withCheckedString( String stringChecked) {
		this.stringChecked = stringChecked;
		updateText();
		return this;
	}

	/**
	 * Changes the default <b>unchecked</b> string (<i>✖</i>)
	 *
	 * @param stringUnchecked the new string
	 * @return the Checkbox
	 */
	public MenuComponentCheckbox withUncheckedString( String stringUnchecked) {
		this.stringUnchecked = stringUnchecked;
		updateText();
		return this;
	}

	/**
	 * @return <code>true</code> if the checkbox is checked, <code>false</code> otherwise
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * Change the <i>checked</i>-state of the checkbox
	 *
	 * @param checked <code>true</code> if the checkbox should be set checked, <code>false</code> otherwise
	 * @return the Checkbox
	 */
	public MenuComponentCheckbox setChecked(boolean checked) {
		this.checked = checked;
		updateText();
		return this;
	}

	/**
	 * Add a {@link ValueListener} to be called when the value updates
	 *
	 * @param listener {@link ValueListener} to add
	 * @return the Checkbox
	 */
	public MenuComponentCheckbox onChange(final ValueListener<Boolean> listener) {
		this.valueListener = listener;
		return this;
	}

	@Override
	public String render() {
		String formatted = this.format;

		formatted = String.format(formatted, isChecked() ? stringChecked : stringUnchecked);

		return formatted;
	}

	public TextComponent build() {
		return this.component;
	}

	@Override
	public MenuComponent appendTo(final LineBuilder builder) {
		builder.append(new ChatListener() {
			@Override
			public void onClick(Player player) {
				boolean wasChecked = isChecked();
				setChecked(!wasChecked);
				if (valueListener != null) { valueListener.onChange(player, wasChecked, isChecked()); }
				if (builder.getContainer() != null) { builder.getContainer().refreshContent(); }
			}
		}, build());
		return this;
	}
}
