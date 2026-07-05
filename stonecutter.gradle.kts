plugins {
    id("dev.kikugie.stonecutter")
    id("gg.meza.stonecraft")
}

stonecutter active "26.2-fabric" /* [SC] DO NOT EDIT */

stonecutter parameters {
	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
	}

	replacements.string(current.parsed >= "26.1", "!unobfuscate") {
		replace("GuiGraphics", "GuiGraphicsExtractor")
		replace("net.minecraft.client.GuiMessage", "net.minecraft.client.multiplayer.chat.GuiMessage")
		replace("net.minecraft.client.GuiMessageTag", "net.minecraft.client.multiplayer.chat.GuiMessageTag")
		replace("graphics.drawString(", "graphics.text(")
		replace("graphics.drawCenteredString(", "graphics.centeredText(")
		replace("renderContent", "extractContent")
		replace("render(", "extractRenderState(")
		replace("render\"", "extractRenderState\"")
	}

	replacements.string(current.parsed >= "26.2") {
		replace("minecraft.setScreen(", "minecraft.gui.setScreen(")
	}
}
