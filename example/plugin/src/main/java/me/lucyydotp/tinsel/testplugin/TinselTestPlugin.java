package me.lucyydotp.tinsel.testplugin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.lucyydotp.tinsel.Tinsel;
import me.lucyydotp.tinsel.font.FontFamily;
import me.lucyydotp.tinsel.font.OffsetMap;
import me.lucyydotp.tinsel.layout.TextLayoutBuilder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class TinselTestPlugin extends JavaPlugin {

    private static final Key GLYPH_FONT = Key.key("tinsel.example:glyph");

    private final Tinsel tinsel = Tinsel.builder()
            .withTinselPack()
            .withFonts(
                    FontFamily.fromResourcePack(Path.of(System.getenv("TINSEL_PACK_PATH")))
                            .add(GLYPH_FONT, OffsetMap.offsets(GLYPH_FONT, 12, 24))
                            .build()
            ).build();

    private final Map<String, TextLayoutBuilder> layouts;

    public TinselTestPlugin() {
        final var one = new TextLayoutBuilder(tinsel);
        one.add((ctx) -> ctx.drawAligned(Component.text("Centred"), 0.5f));

        final var two = new TextLayoutBuilder(tinsel);
        two.add((ctx) -> ctx.drawAligned(Component.text("Left"), 0f));
        two.add((ctx) -> ctx.drawAligned(Component.text("Centred"), 0.5f));

        final var three = new TextLayoutBuilder(tinsel);
        three.add((ctx) -> ctx.drawAligned(Component.text("Left"), 0f));
        three.add((ctx) -> ctx.drawAligned(Component.text("Centred"), 0.5f));
        three.add((ctx) -> ctx.drawAligned(Component.text("Right"), 1));

        final var four = new TextLayoutBuilder(tinsel);
        four.add((ctx) -> ctx.drawAligned(Component.text("Left"), 0f));
        four.add((ctx) -> {
            ctx.moveCursor(ctx.cursorX(), 12);
            ctx.drawAligned(Component.text("Centred"), 0.5f);
            ctx.drawAligned(Component.text("Left 2"), 0f);
        });
        four.add((ctx) -> {
            ctx.moveCursor(ctx.cursorX(), 4);
            ctx.drawAligned(Component.text("Right"), 1);
        });


        final var five = new TextLayoutBuilder(tinsel);
        five.add(ctx -> {
            for (int i = 0; i < 12; i++) {
                ctx.moveCursor(0, -48 + (i * 8));
                ctx.draw(Component.text("Line " + (i + 1)));
            }
        });

        final var bg = new TextLayoutBuilder(tinsel, Style.style(ShadowColor.none()));
        bg.add(ctx -> {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    ctx.moveCursor(0, j * 12);
                    ctx.drawAligned(Component.text("\uE000").font(GLYPH_FONT), i / 2.0f);

                    final var text = Component.text("gaming").color(switch (Math.floorMod(i + j, 3)) {
                        case 0 -> NamedTextColor.RED;
                        case 1 -> NamedTextColor.GREEN;
                        default -> NamedTextColor.BLUE;
                    });

                    final var measured = tinsel.textWidthMeasurer().measure(text);
                    ctx.moveCursor(ctx.cursorX() - 24 - (measured / 2), ctx.cursorY());
                    ctx.drawWithWidth(text, measured);
                }
            }
        });

        this.layouts = Map.of(
                "one", one,
                "two", two,
                "three", three,
                "four", four,
                "five", five,
                "bg", bg
        );
    }

    private LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("tinsel").then(
                Commands.argument("layout", StringArgumentType.word())
                        .suggests((ctx, suggestions) -> {
                            for (final var layout : layouts.keySet()) {
                                suggestions.suggest(layout);
                            }
                            return suggestions.buildFuture();
                        })
                        .executes(ctx -> {
                            if (!(ctx.getSource().getSender() instanceof Player player)) {
                                return 0;
                            }

                            final var layout = layouts.get(ctx.getArgument("layout", String.class));
                            if (layout == null) return 0;

                            player.sendActionBar(layout.draw(181));
                            return 0;
                        })
        ).build();
    }

    @Override
    public void onEnable() {
        getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                (event) -> event.registrar().register(node())
        );
        super.onEnable();
    }
}
