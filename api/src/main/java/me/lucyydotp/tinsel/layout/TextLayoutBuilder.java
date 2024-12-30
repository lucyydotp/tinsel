package me.lucyydotp.tinsel.layout;

import me.lucyydotp.tinsel.Tinsel;
import me.lucyydotp.tinsel.font.Font;
import me.lucyydotp.tinsel.font.FontFamily;
import me.lucyydotp.tinsel.font.FontSet;
import me.lucyydotp.tinsel.font.Spacing;
import me.lucyydotp.tinsel.measurement.TextWidthMeasurer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TextLayoutBuilder {
    private final List<TextDrawable> drawables = new ArrayList<>();
    private final TextWidthMeasurer textWidthMeasurer;
    private final FontSet fonts;
    private final Style baseStyle;

    public TextLayoutBuilder(Tinsel tinsel, Style baseStyle) {
        this.textWidthMeasurer = tinsel.textWidthMeasurer();
        this.fonts = tinsel.fonts();
        this.baseStyle = baseStyle;
    }

    public TextLayoutBuilder(Tinsel tinsel) {
        this(tinsel, Style.empty());
    }

    public void add(TextDrawable drawable) {
        drawables.add(drawable);
    }

    public Component draw(int width) {
        final var context = new Context(width);
        for (final var drawable: drawables) {
            drawable.draw(context);
        }
        return context.output();
    }

    private class Context implements TextDrawContext {
        private final int totalWidth;
        private final TextComponent.Builder content = Component.text().style(baseStyle);
        private int cursorX = 0;
        private int cursorY = 0;

        private Context(int totalWidth) {
            this.totalWidth = totalWidth;
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
                return fonts.defaultFont().offset(0);
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
                return input.font(findOffset(fonts.defaultFont(), cursorY).name());
            }

            var font = fonts.font(fontKey);
            if (font == null) {
                System.out.printf("Unknown font %s.", fontKey);
                font = fonts.defaultFont().offset(0);
            }

            final var newFont = findOffset(fonts.fontFamily(font.family()), cursorY + font.offset());
            // TODO(lucy): recurse
            return input.font(newFont.name());
        }

        @Override
        public void draw(Component component, float align) {
            final var width = textWidthMeasurer.measure(component);
            final var spacing = -Math.round(width * align);

            moveCursor(cursorX + spacing, cursorY);
            drawWithWidth(component, textWidthMeasurer.measure(component));
        }

        @Override
        public void drawWithWidth(Component component, int width) {
            content.append(adjustForY(component));
            cursorX += width;
        }

        @Override
        public void drawAligned(Component component, float alignment) {
            final var width = textWidthMeasurer.measure(component);
            final var targetX = Math.round((totalWidth - width) * alignment);
            if (targetX != cursorX) {
                content.append(Spacing.spacing(targetX - cursorX));
            }
            content.append(adjustForY(component));
            cursorX = targetX + width;
        }

        private Component output() {
            if (cursorX != totalWidth) {
                content.append(Spacing.spacing(totalWidth - cursorX));
            }
            return content.build();
        }
    }
}
