package com.fedpol1.enchantips.gui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;

public abstract class InfoDelineator implements Drawable, Element {

    protected static final int LINE_HEIGHT = 10;
    protected static final int INDENTATION = 16;

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean focused = false;
    protected InfoLineContainer parent;
    protected ScrollableInfoLineContainer nearestScrollableParent;

    public InfoDelineator() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.parent = null;
        this.nearestScrollableParent = null;
    }

    public int getHeight(int index) {
        return InfoDelineator.LINE_HEIGHT;
    }

    public int getHeight() {
        return this.getHeight(0);
    }

    public void setHeight() {
        this.height = InfoDelineator.LINE_HEIGHT;
    }

    @Override
    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    public abstract void refresh(int index);

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }
}
