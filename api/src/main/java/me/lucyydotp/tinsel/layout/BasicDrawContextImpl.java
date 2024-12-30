package me.lucyydotp.tinsel.layout;

import me.lucyydotp.tinsel.Tinsel;
import me.lucyydotp.tinsel.font.Font;
import me.lucyydotp.tinsel.font.FontFamily;
import me.lucyydotp.tinsel.font.Spacing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A basic implementation of {@link TextDrawContext}.
 */
@ApiStatus.Internal
@NullMarked
public class BasicDrawContextImpl implements TextDrawContext {
    private final int totalWidth;
    private final TextComponent.Builder content;
    private final Tinsel tinsel;

    private int cursorX = 0;
    private int cursorY = 0;

    public BasicDrawContextImpl(Tinsel tinsel, int totalWidth, Style baseStyle) {
        this.tinsel = tinsel;
        this.totalWidth = totalWidth;
        this.content = Component.text().style(baseStyle);
    }

    @Override
    public int cursorX() {
        return cursorX;
    }

    @Override
    public int cursorY() {
        return cursorY;
    }

    @Override
    public int totalWidth() {
        return totalWidth;
    }

    @Override
    public void moveCursor(int x, int y) {
        if (x != cursorX) {
            content.append(Spacing.spacing(x - cursorX));
            cursorX = x;
        }
        cursorY = y;
    }

    private Font findOffset(@Nullable FontFamily family, int offset) {
        if (family == null) {
            System.out.println("Couldn't find font!");
            return tinsel.fonts().defaultFont().offset(0);
        }
        final var font = family.offset(offset);
        if (font == null) {
            System.out.printf("Couldn't find font %s with offset %s.", family.key(), offset);
            return family.offset(0);
        }

        return font;
    }

    private Component adjustForY(Component input) {
        final var fontKey = input.font();
        if (fontKey == null) {
            return input.font(findOffset(tinsel.fonts().defaultFont(), cursorY).name());
        }

        var font = tinsel.fonts().font(fontKey);
        if (font == null) {
            System.out.printf("Unknown font %s.", fontKey);
            font = tinsel.fonts().defaultFont().offset(0);
        }

        final var newFont = findOffset(tinsel.fonts().fontFamily(font.family()), cursorY + font.offset());
        // TODO(lucy): recurse
        return input.font(newFont.name());
    }

    @Override
    public void draw(Component component, float align) {
        final var width = tinsel.textWidthMeasurer().measure(component);
        final var spacing = -Math.round(width * align);

        moveCursor(cursorX + spacing, cursorY);
        drawWithWidth(component, width);
    }

    @Override
    public void drawWithWidth(Component component, int width) {
        content.append(adjustForY(component));
        cursorX += width;
    }

    @Override
    public void drawAligned(Component component, float alignment) {
        final var width = tinsel.textWidthMeasurer().measure(component);
        final var targetX = Math.round((totalWidth - width) * alignment);
        if (targetX != cursorX) {
            content.append(Spacing.spacing(targetX - cursorX));
        }
        content.append(adjustForY(component));
        cursorX = targetX + width;
    }

    public Component output() {
        if (cursorX != totalWidth) {
            content.append(Spacing.spacing(totalWidth - cursorX));
        }
        return content.build();
    }
}
