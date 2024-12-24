package me.lucyydotp.tinsel.layout;

import net.kyori.adventure.text.Component;

public interface TextDrawContext {
    int cursorX();
    int cursorY();

    int totalWidth();
    void moveCursor(int x, int y);

    void draw(Component component);
    void drawAligned(Component component, float alignment);
}
