package com.fedpol1.enchantips.gui.widgets;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CollapsibleInfoLine extends InfoDelineator implements Drawable, Element {

    protected static final int BUTTON_INDENTATION = 11;
    protected static final Identifier COLLAPSE_TEXTURE = Identifier.of(EnchantipsClient.MODID, "enchantment_info/collapse");
    protected static final Identifier COLLAPSE_HOVER_TEXTURE = Identifier.of(EnchantipsClient.MODID, "enchantment_info/collapse_hover");
    protected static final Identifier EXPAND_TEXTURE = Identifier.of(EnchantipsClient.MODID, "enchantment_info/expand");
    protected static final Identifier EXPAND_HOVER_TEXTURE = Identifier.of(EnchantipsClient.MODID, "enchantment_info/expand_hover");
    protected static final int BUTTON_WIDTH = 9;
    protected static final int BUTTON_HEIGHT = 9;

    protected final Text text;
    protected boolean collapsed;
    protected final InfoLineContainer lines;

    public CollapsibleInfoLine(Text text) {
        super();
        this.height = 0;
        this.text = text;
        this.collapsed = true;
        this.lines = new InfoLineContainer();
    }

    public void addLine(Text line) {
        this.addLine(new InfoLine(line));
    }

    public void addLine(InfoDelineator line) {
        line.parent = this.lines;
        line.nearestScrollableParent = this.nearestScrollableParent;
        this.lines.addLine(line);
    }

    public int getHeight(int index) {
        return InfoDelineator.LINE_HEIGHT + (this.collapsed ? 0 : this.lines.getHeight(index));
    }

    @Override
    public void refresh(int index) {
        this.x = this.parent.x;
        this.y = this.parent.y + this.parent.getHeight(index);
        if(this.parent == this.nearestScrollableParent) { this.y += this.nearestScrollableParent.scrollHeight; }
        this.width = this.parent.width;
        this.height = this.getHeight(index) + (this.collapsed ? 0 : this.lines.getHeight());
        if(!this.collapsed) {
            this.lines.refresh(index);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawHorizontalLine(this.x, this.x + width, this.y, 0xffff0000);
        context.drawHorizontalLine(this.x, this.x + width, this.y + LINE_HEIGHT, 0xffff0000);
        context.drawVerticalLine(this.x, this.y, this.y + LINE_HEIGHT, 0xffff0000);
        context.drawVerticalLine(this.x + this.width, this.y, this.y + LINE_HEIGHT, 0xffff0000);

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        this.drawButton(context, mouseX, mouseY, delta);
        context.drawTextWrapped(renderer, renderer.trimToWidth(this.text, this.width), this.x + BUTTON_INDENTATION, this.y + 1, this.width - BUTTON_INDENTATION, 0x404040);
        if(!this.collapsed) {
            this.lines.render(context, mouseX, mouseY, delta);
        }
    }

    private boolean isWithinButton(double mouseX, double mouseY) {
        int x = this.x + 1;
        int y = this.y + 1;
        return mouseX >= x && mouseX < x + BUTTON_WIDTH && mouseY >= y && mouseY < y + BUTTON_HEIGHT;
    }

    private void drawButton(DrawContext context, int mouseX, int mouseY, float delta) {
        Identifier texture;
        if(this.isWithinButton(mouseX, mouseY)) {
            texture = this.collapsed ? EXPAND_HOVER_TEXTURE : COLLAPSE_HOVER_TEXTURE;
        } else {
            texture = this.collapsed ? EXPAND_TEXTURE : COLLAPSE_TEXTURE;
        }
        context.drawGuiTexture(RenderLayer::getGuiTextured, texture, this.x + 1, this.y + 1, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.isWithinButton(mouseX, mouseY) && button == 0) {
            this.collapsed = !this.collapsed;
            boolean found = false;
            for (int i = 0; i < this.parent.lines.size(); i++) {
                if (this.parent.lines.get(i) == this || found) {
                    found = true;
                    this.parent.lines.get(i).refresh(i);
                }
            }
            return true;
        }
        return false;
    }
}
