package com.ezzenix.smoothgui.lib.config;

import com.ezzenix.smoothgui.lib.EmId;
import com.ezzenix.smoothgui.lib.EmPort;
import com.ezzenix.smoothgui.lib.IconButton;
import com.ezzenix.smoothgui.lib.RightClickableButton;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

//? if >1.20.1 {
import net.minecraft.client.gui.components.SpriteIconButton;
//? } else {
/*import net.minecraft.client.gui.components.TextAndImageButton;
*///? }

public class ConfigScreen extends Screen {
	private final Screen parent;
	private ConfigListWidget list;

	private static final int ROW_WIDTH = 380;
	private static final int BUTTON_WIDTH = 120;

	public ConfigScreen(Screen parent) {
		super(Component.literal(BaseConfig.getTitle()));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();

		this.addRenderableWidget(Button.builder(Component.literal("Done"), (button) -> {
			BaseConfig.save();
			this.minecraft.gui.setScreen(this.parent);
		}).bounds(this.width / 2 - 100, this.height - 26, 200, 20).build());

		this.list = new ConfigListWidget(this.minecraft, this.width, this.height - 57, 24, 25);
		this.addWidget(this.list);

		this.updateList();
	}

	@Override
	public void tick() {
		super.tick();
		updateButtons();
	}

	public void changed() { }

	public void updateButtons() {
		if (this.list == null) return;
		for (ConfigListWidget.Entry entry : this.list.children()) {
			if (entry.buttons != null && entry.buttons.size() >= 2) {
				if (entry.buttons.get(1) instanceof Button button) {
					button.active = !Objects.equals(String.valueOf(entry.info.getValue()), String.valueOf(entry.info.defaultValue));
				}
			}
		}
	}

	public void updateList() {
		this.list.clear();
		int rightX = width/2+ROW_WIDTH/2;
		int buttonLeftX = rightX-BUTTON_WIDTH-25;
		for (EntryInfo info : BaseConfig.entries) {
			if (info.comment != null) {
				this.list.add(List.of(), info);
				continue;
			}

			var resetButton = EmPort.createIconButton(Component.literal("Reset"), b -> {
				info.setValue(info.defaultValue);
				updateList();
			}, EmId.forSprite("smoothgui", "reset"), 12, 20);
			resetButton.setX(rightX - 20);

			if (info.getType() == boolean.class) {
				/* boolean toggle */
				Button button = Button.builder(getBooleanButtonText(info), (b) -> {
					boolean currentValue = (boolean) info.getValue();
					info.setValue(!currentValue);
					b.setMessage(getBooleanButtonText(info));
				}).tooltip(info.getTooltip()).bounds(buttonLeftX, 0, BUTTON_WIDTH, 20).build();

				this.list.add(List.of(button, resetButton), info);
			} else if (info.getType() == int.class || info.getType() == float.class || info.getType() == double.class) {
				/* number slider */
				double normalized = (Double.parseDouble(info.getValue().toString())) / (info.entry.max() - info.entry.min());
				ConfigSliderWidget slider = new ConfigSliderWidget(buttonLeftX, 0, BUTTON_WIDTH, 20, normalized, info);
				slider.setTooltip(info.getTooltip());

				this.list.add(List.of(slider, resetButton), info);
			} else if (info.getType().isEnum()) {
				/* enum cycle */
				Button button = new RightClickableButton(buttonLeftX, 0, BUTTON_WIDTH, 20, Component.literal(info.getValue().toString()), (b, wasRightClick) -> {
					Object[] constants = info.getType().getEnumConstants();
					Enum<?> currentValue = (Enum<?>) info.getValue();

					int nextIndex = currentValue.ordinal() + (wasRightClick ? -1 : 1);
					if (nextIndex < 0) nextIndex = constants.length-1;
					if (nextIndex >= constants.length) nextIndex = 0;
					info.setValue(constants[nextIndex]);

					b.setMessage(Component.literal(info.getValue().toString()));
				});

				this.list.add(List.of(button, resetButton), info);
			}
		}
		this.updateButtons();
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		//? if <=1.20.1
		//super.renderBackground(graphics);

		super.extractRenderState(graphics, mouseX, mouseY, delta);
		this.list.extractRenderState(graphics, mouseX, mouseY, delta);

		graphics.centeredText(Minecraft.getInstance().font, this.title, this.width/2, 8, 0xffffffff);
	}

	private Component getBooleanButtonText(EntryInfo entry) {
		boolean val = (boolean) entry.getValue();
		return Component.literal(val ? "§aYes" : "§cNo");
	}

	@Override
	public void onClose() {
		BaseConfig.save();
		this.minecraft.gui.setScreen(this.parent);
	}

	private class ConfigListWidget extends ContainerObjectSelectionList<ConfigListWidget.Entry> {
		public ConfigListWidget(Minecraft mc, int width, int height, int y, int itemHeight) {
			//? if >=1.20.3 {
			super(mc, width, height, y, itemHeight);
			//? } else {
			/*super(mc, width, height, y, height+y, itemHeight);
			*///? }
		}

		public void add(List<AbstractWidget> buttons, EntryInfo info) {
			this.addEntry(new Entry(buttons, info));
		}

		public void clear() {
			this.clearEntries();
		}

		@Override
		public int /*? if >=1.21.4 {*/ scrollBarX() /*?} else {*/ /*getScrollbarPosition() *//*?}*/ {
			return ConfigScreen.this.width - /*? if >=26.1 {*/ this.scrollbarWidth() /*?} else {*/ /*6*//*?}*/ - 4;
		}

		@Override
		public int getRowWidth() {
			return ROW_WIDTH;
		}

		public class Entry extends ContainerObjectSelectionList.Entry<Entry> {
			public final List<AbstractWidget> buttons;
			private final EntryInfo info;
			public final Component title;
			private final boolean centered;

			private float currentOffsetX = 0f;

			public Entry(List<AbstractWidget> buttons, EntryInfo info) {
				this.buttons = buttons;
				this.info = info;
				this.title = Component.literal(info.getName());
				this.centered = info.comment != null && info.comment.centered();
			}

			@Override
			//? if >= 1.21.9 {
			public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float tickDelta) {
				int x = this.getX();
				int y = this.getY();
			//?} else {
			/*public void extractRenderState(GuiGraphicsExtractor graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			*///?}
				buttons.forEach(b -> {
					b.setY(y);
					b.extractRenderState(graphics, mouseX, mouseY, tickDelta);
				});

				boolean isHovered = this.isMouseOver(mouseX, mouseY) && info.entry != null;

				float targetOffsetX = isHovered ? 4f : 0f;
				float speed = 0.65f;
				float adaptiveSpeed = 1.0f - (float)Math.pow(1.0f - speed, tickDelta);
				this.currentOffsetX = Mth.lerp(adaptiveSpeed, this.currentOffsetX, targetOffsetX);

				int color = isHovered ? 0xff87ff95 : 0xffffffff;

				if (this.centered) {
					graphics.centeredText(Minecraft.getInstance().font, this.title, ConfigScreen.this.width/2, y+5, color);
				} else {
					graphics.text(Minecraft.getInstance().font, this.title, x + (int)currentOffsetX, y+5, color);
				}
			}

			@Override @NotNull
			public List<? extends GuiEventListener> children() {
				return Lists.newArrayList(buttons);
			}

			@Override @NotNull
			public List<? extends NarratableEntry> narratables() {
				return Lists.newArrayList(buttons);
			}
		}
	}
}
