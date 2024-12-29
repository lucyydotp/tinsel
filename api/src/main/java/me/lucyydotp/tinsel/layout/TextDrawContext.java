package me.lucyydotp.tinsel.layout;

import net.kyori.adventure.text.Component;

/**
 * A context for drawing text.
 */
public interface TextDrawContext {
    /**
     * The cursor's current X position.
     */
    int cursorX();

    /**
     * The cursor's current Y position.
     */
    int cursorY();

    /**
     * The total width of the context's output text.
     */
    int totalWidth();

    /**
     * Moves the cursor to a spot by adding spacing.
     */
    void moveCursor(int x, int y);

    /**
     * Draws a component, measuring it in the process.
     * @param component the component to draw
     */
    void draw(Component component);

    /**
     * Draws a component, using a provided width measurement instead of measuring.
     * @param component the component to draw
     * @param width the width of the component
     */
    void drawWithWidth(Component component, int width);

    /**
     * Draws a component aligned to the edges of the total width.
     * @param component the component to draw
     * @param alignment the alignment value, between 0.0 and 1.0. 0.0 draws at the left edge, 1.0 draws at the right
     */
    void drawAligned(Component component, float alignment);
}
