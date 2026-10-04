package com.vildoria.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class NotesConfigScreen extends Screen {
	private final Screen parent;
	private int panelLeft;
	private int panelTop;
	private int panelWidth;
	private int panelHeight;
	private int scrollOffset;

	public NotesConfigScreen(Screen parent) {
		super(Component.translatable("config.vildoria.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		panelWidth = Math.max(1, Math.min(680, width - 28));
		panelHeight = Math.max(1, Math.min(500, height - 28));
		panelLeft = (width - panelWidth) / 2;
		panelTop = (height - panelHeight) / 2;
		scrollOffset = Math.min(scrollOffset, maxScrollOffset());

		addSizeControls(contentY(panelTop + 108), true);
		addSizeControls(contentY(panelTop + 144), false);
		addOptionButton(contentY(panelTop + 214), Component.translatable("config.vildoria.theme_label"),
				Component.translatable(NotesSettings.themeKey()), NotesSettings::cycleTheme);
		addOptionButton(contentY(panelTop + 250), Component.translatable("config.vildoria.font_label"),
				Component.translatable(NotesSettings.fontKey()), NotesSettings::cycleFont);
		addOptionButton(contentY(panelTop + 286), Component.translatable("config.vildoria.density_label"),
				Component.translatable(NotesSettings.densityKey()), NotesSettings::cycleDensity);
		addOptionButton(contentY(panelTop + 322), Component.translatable("config.vildoria.text_label"),
				Component.translatable(NotesSettings.textColorKey()), NotesSettings::cycleTextColor);
		addOptionButton(contentY(panelTop + 358), Component.translatable("config.vildoria.grid_label"),
				gridLabel(), NotesSettings::toggleGridLines);

		int footerButtonWidth = Math.max(1, Math.min(132, (panelWidth - 52) / 2));
		addRenderableWidget(Button.builder(NotesFont.apply(Component.translatable("config.vildoria.reset")),
				button -> {
					NotesSettings.reset();
					rebuildWidgets();
				}).bounds(panelLeft + 22, panelTop + panelHeight - 38, footerButtonWidth, 22).build());
		addRenderableWidget(Button.builder(NotesFont.apply(Component.translatable("config.vildoria.done")),
				button -> minecraft.setScreenAndShow(parent))
				.bounds(panelLeft + panelWidth - 22 - footerButtonWidth, panelTop + panelHeight - 38,
						footerButtonWidth, 22).build());
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		NotesSettings.Palette palette = NotesSettings.palette();
		graphics.fill(0, 0, width, height, palette.background());
		graphics.fill(panelLeft - 6, panelTop - 6, panelLeft + panelWidth + 6, panelTop + panelHeight + 6, palette.panel());
		graphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelTop + panelHeight, palette.panel());
		graphics.outline(panelLeft, panelTop, panelWidth, panelHeight, palette.border());
		graphics.fill(panelLeft, panelTop, panelLeft + panelWidth, panelTop + 62, palette.header());
		graphics.fill(panelLeft, panelTop + 58, panelLeft + panelWidth, panelTop + 61, palette.accent());
		graphics.text(font, NotesFont.apply(title), panelLeft + 22, panelTop + 15, 0xffffffff);
		graphics.text(font, NotesFont.apply(Component.translatable("config.vildoria.subtitle")),
				panelLeft + 22, panelTop + 34, 0xffd4e5dc);

		drawSection(graphics, "config.vildoria.layout", contentY(panelTop + 84), palette);
		drawSettingLabel(graphics, "config.vildoria.width_label", contentY(panelTop + 113), palette);
		drawSettingLabel(graphics, "config.vildoria.height_label", contentY(panelTop + 148), palette);
		drawSection(graphics, "config.vildoria.appearance", contentY(panelTop + 196), palette);
		drawSettingLabel(graphics, "config.vildoria.theme_label", contentY(panelTop + 214), palette);
		drawSettingLabel(graphics, "config.vildoria.font_label", contentY(panelTop + 250), palette);
		drawSettingLabel(graphics, "config.vildoria.density_label", contentY(panelTop + 286), palette);
		drawSettingLabel(graphics, "config.vildoria.text_label", contentY(panelTop + 322), palette);
		drawSettingLabel(graphics, "config.vildoria.grid_label", contentY(panelTop + 358), palette);

		graphics.text(font, NotesFont.apply(Component.translatable("config.vildoria.key",
				VildoriaClient.getOpenNotesKey().getTranslatedKeyMessage())),
				panelLeft + 22, panelTop + panelHeight - 61, palette.muted());
		super.extractRenderState(graphics, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		int contentTop = panelTop + 68;
		int contentBottom = panelTop + panelHeight - 68;
		if (scrollY != 0 && mouseX >= panelLeft && mouseX < panelLeft + panelWidth
				&& mouseY >= contentTop && mouseY < contentBottom && maxScrollOffset() > 0) {
			scrollOffset = Math.max(0, Math.min(maxScrollOffset(),
					scrollOffset - (int) Math.signum(scrollY) * 36));
			rebuildWidgets();
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}

	private void addSizeControls(int y, boolean horizontal) {
		int availableWidth = Math.max(1, panelWidth - 44);
		int controlWidth = Math.max(1, Math.min(30, (availableWidth - 4) / 4));
		int gap = 2;
		int valueWidth = Math.max(1, Math.min(92, availableWidth - controlWidth * 2 - gap * 2));
		int x = panelLeft + panelWidth - 22 - (controlWidth * 2 + valueWidth + gap * 2);
		Button[] buttons = new Button[2];
		Button valueButton = Button.builder(NotesFont.apply(sizeLabel(horizontal)), button -> {
			if (horizontal) {
				NotesSettings.increaseWidth();
			} else {
				NotesSettings.increaseHeight();
			}
			updateSizeMessage(horizontal, buttons[0], buttons[1]);
		}).bounds(x + controlWidth + gap, y, valueWidth, 22).build();
		Button minusButton = Button.builder(NotesFont.literal("-"), button -> {
			if (horizontal) {
				NotesSettings.decreaseWidth();
			} else {
				NotesSettings.decreaseHeight();
			}
			updateSizeMessage(horizontal, buttons[0], buttons[1]);
		}).bounds(x, y, controlWidth, 22).build();
		Button plusButton = Button.builder(NotesFont.literal("+"), button -> {
			if (horizontal) {
				NotesSettings.increaseWidth();
			} else {
				NotesSettings.increaseHeight();
			}
			updateSizeMessage(horizontal, buttons[0], buttons[1]);
		}).bounds(x + controlWidth + gap + valueWidth + gap, y, controlWidth, 22).build();
		buttons[0] = minusButton;
		buttons[1] = valueButton;
		minusButton.visible = isContentVisible(y, 22);
		valueButton.visible = isContentVisible(y, 22);
		plusButton.visible = isContentVisible(y, 22);
		addRenderableWidget(minusButton);
		addRenderableWidget(valueButton);
		addRenderableWidget(plusButton);
	}

	private void updateSizeMessage(boolean horizontal, Button minusButton, Button valueButton) {
		if (minusButton != null) {
			minusButton.setMessage(NotesFont.literal("-"));
		}
		if (valueButton != null) {
			valueButton.setMessage(NotesFont.apply(sizeLabel(horizontal)));
		}
	}

	private Component sizeLabel(boolean horizontal) {
		int size = horizontal ? NotesSettings.width() : NotesSettings.height();
		return Component.translatable("config.vildoria.value_px", size);
	}

	private void addOptionButton(int y, Component label, Component value, Runnable action) {
		int buttonWidth = Math.max(1, Math.min(220, panelWidth - 210));
		int x = panelLeft + Math.max(0, panelWidth - buttonWidth - 22);
		if (label != null) {
			// kept for readability; the button itself is the interactive value selector
		}
		addRenderableWidget(Button.builder(NotesFont.apply(value), button -> {
			action.run();
			button.setMessage(NotesFont.apply(currentValue(y + scrollOffset)));
		}).bounds(x, y, buttonWidth, 22).build()).visible = isContentVisible(y, 22);
	}

	private Component currentValue(int y) {
		if (y == panelTop + 214) {
			return Component.translatable(NotesSettings.themeKey());
		}
		if (y == panelTop + 250) {
			return Component.translatable(NotesSettings.fontKey());
		}
		if (y == panelTop + 286) {
			return Component.translatable(NotesSettings.densityKey());
		}
		if (y == panelTop + 322) {
			return Component.translatable(NotesSettings.textColorKey());
		}
		return gridLabel();
	}

	private Component gridLabel() {
		return Component.translatable(NotesSettings.gridLines() ? "config.vildoria.grid.on" : "config.vildoria.grid.off");
	}

	private void drawSection(GuiGraphicsExtractor graphics, String key, int y, NotesSettings.Palette palette) {
		if (!isContentVisible(y, 14)) {
			return;
		}
		graphics.text(font, NotesFont.apply(Component.translatable(key)), panelLeft + 22, y, palette.accent());
		graphics.fill(panelLeft + 22, y + 13, panelLeft + panelWidth - 22, y + 14, palette.border());
	}

	private void drawSettingLabel(GuiGraphicsExtractor graphics, String key, int y,
				      NotesSettings.Palette palette) {
		if (isContentVisible(y, 8)) {
			graphics.text(font, NotesFont.apply(Component.translatable(key)), panelLeft + 22, y, palette.text());
		}
	}

	private int contentY(int y) {
		return y - scrollOffset;
	}

	private int maxScrollOffset() {
		return Math.max(0, 448 - panelHeight);
	}

	private boolean isContentVisible(int y, int elementHeight) {
		return y >= panelTop + 68 && y + elementHeight <= panelTop + panelHeight - 68;
	}
}