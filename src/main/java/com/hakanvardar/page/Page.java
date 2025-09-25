package com.hakanvardar.page;

import java.io.IOException;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;

public abstract class Page {
    protected final TextGraphics tg;

    public Page(TextGraphics tg) {
        this.tg = tg;
    }

    public abstract void onInput(KeyStroke keyStroke) throws IOException;

    public abstract void onRender() throws IOException;

    public void clear() throws IOException {
        TerminalSize size = tg.getSize();
        tg.fillRectangle(new TerminalPosition(0, 0), size, ' ');
    }

    public static Page[] pages(TextGraphics tg) {
        return new Page[] {
                new MenuPage(tg),
                new AddBookPage(tg)
        };
    }

    public static final int MENU = 0;
    public static final int ADD_BOOK = 1;
}
