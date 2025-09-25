package com.hakanvardar.page;

import java.io.IOException;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.hakanvardar.App;

public class MenuPage extends Page {
    private static String[] choices = { "Add book", "Exit" };
    private static int currentChoice = 0;

    public MenuPage(TextGraphics tg) {
        super(tg);
    }

    public void onInput(KeyStroke keyStroke) throws IOException {
        switch (keyStroke.getKeyType()) {
            case ArrowUp:
                currentChoice = Math.clamp(currentChoice - 1, 0, choices.length - 1);
                break;
            case ArrowDown:
                currentChoice = Math.clamp(currentChoice + 1, 0, choices.length - 1);
                break;
            case Enter:
                switch (choices[currentChoice]) {
                    case "Add book":
                        clear();
                        App.setCurrentPage(Page.ADD_BOOK);
                        break;
                    case "Exit":
                        App.exit();
                        break;
                    default:
                        break;
                }

            default:
                break;

        }
    }

    public void onRender() throws IOException {
        int x = 0, y = 0;

        for (String string : choices) {
            if (y == currentChoice) {
                tg.setForegroundColor(TextColor.ANSI.GREEN);
            } else {
                tg.setForegroundColor(TextColor.ANSI.DEFAULT);
            }
            tg.putString(x, y++, string);
        }
    }

}
