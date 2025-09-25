package com.hakanvardar;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.hakanvardar.page.Page;

public class App {
    private static boolean running = true;
    private static Page currentPage = null;
    private static Connection conn = null;
    private static Screen screen = null;
    private static Page[] pages = null;

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:db/library.db");
            initDatabase();
        } catch (SQLException e) {
            System.out.println(e.toString());
            System.exit(1);
        }

        try {
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            Terminal terminal = factory.createTerminal();

            screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null);
            screen.startScreen();

            TextGraphics textGraphics = screen.newTextGraphics();
            pages = Page.pages(textGraphics);
            setCurrentPage(Page.MENU);

            while (running) {
                KeyStroke keyStroke = screen.pollInput();
                if (keyStroke != null) {
                    currentPage.onInput(keyStroke);
                }
                currentPage.onRender();

                screen.refresh();
                Thread.yield();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (screen != null) {
                try {
                    screen.close();
                } catch (IOException ignored) {
                }
            }

        }

    }

    private static void initDatabase() {
        try (var stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS books (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            writer TEXT NOT NULL,
                            isbn TEXT NOT NULL UNIQUE,
                            year INTEGER NOT NULL
                        );
                    """);
        } catch (SQLException e) {
            System.out.println("Database init error: " + e);
            System.exit(1);
        }
    }

    public static boolean addBook(String name, String writer, String isbn, int year) {
        String sql = "INSERT INTO books(name, writer, isbn, year) VALUES(?, ?, ?, ?)";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, writer);
            pstmt.setString(3, isbn);
            pstmt.setInt(4, year);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static Connection getConnection() {
        return conn;
    }

    public static void exit() {
        running = false;
    }

    public static void setCurrentPage(int pageID) {
        currentPage = pages[pageID];
    }
}