package com.hakanvardar.page;

import java.io.IOException;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.hakanvardar.App;

public class AddBookPage extends Page {
    private static String[] inputFields = { "Name: ", "Writer: ", "ISBN: ", "Year: " };
    private static String[] inputValues = { "", "", "", "" };
    private static int currentField = 0;
    private String errorMessage = "";
    private boolean bookAdded = false;

    public AddBookPage(TextGraphics tg) {
        super(tg);
    }

    public void onInput(KeyStroke keyStroke) throws IOException {
        switch (keyStroke.getKeyType()) {
            case ArrowUp:
                currentField = Math.clamp(currentField - 1, 0, inputFields.length - 1);
                break;
            case ArrowDown:
                currentField = Math.clamp(currentField + 1, 0, inputFields.length - 1);
                break;
            case Escape:
                clear();
                App.setCurrentPage(Page.MENU);
                break;
            case Backspace:
                if (!inputValues[currentField].isEmpty()) {
                    String value = inputValues[currentField];
                    inputValues[currentField] = value.substring(0, value.length() - 1);
                    clear();
                }
                break;
            case Enter:
                boolean emptyField = false;
                for (int i = 0; i < inputFields.length - 1; i++) {
                    if (inputValues[i].isEmpty()) {
                        emptyField = true;
                        break;
                    }
                }
                if (emptyField) {
                    errorMessage = "All fields must be filled!";
                } else {
                    int year = Integer.parseInt(inputValues[3]);
                    boolean success = App.addBook(inputValues[0], inputValues[1], inputValues[2], year);
                    if (success) {
                        bookAdded = true;
                    } else {
                        errorMessage = "This book already exists!";
                        bookAdded = false;
                    }

                }

                clear();
                break;
            default:
                bookAdded = false;
                var ch = keyStroke.getCharacter();
                if (Character.isLetterOrDigit(ch) || Character.isSpaceChar(ch)
                        || ch == "'".toCharArray()[0]) {
                    if (currentField == 3 && !Character.isDigit(ch)) {
                        break;
                    }
                    inputValues[currentField] += ch.toString();

                }
                break;
        }
    }

    public void onRender() throws IOException {
        int x = 0, y = 0;

        for (String string : inputFields) {
            if (y == currentField) {
                tg.setForegroundColor(TextColor.ANSI.GREEN);
            } else {
                tg.setForegroundColor(TextColor.ANSI.DEFAULT);
            }
            tg.putString(x, y, string + inputValues[y++]);
        }

        if (!errorMessage.isEmpty()) {
            tg.setForegroundColor(TextColor.ANSI.RED);
            tg.putString(0, y + 1, errorMessage);
        }
        if (bookAdded) {
            tg.setForegroundColor(TextColor.ANSI.GREEN);
            tg.putString(0, y + 2, String.format("%s succesfuly added to DB", inputValues[0]));
        }
    }
}
