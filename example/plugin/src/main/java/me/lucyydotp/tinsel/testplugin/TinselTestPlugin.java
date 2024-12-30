package me.lucyydotp.tinsel.testplugin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.lucyydotp.tinsel.Tinsel;
import me.lucyydotp.tinsel.font.FontFamily;
import me.lucyydotp.tinsel.font.OffsetMap;
import me.lucyydotp.tinsel.layout.TextDrawContext;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

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

    private ActionBarTextHolder actionBarTextHolder;

    private final Map<String, Consumer<TextDrawContext>> layouts;

    public TinselTestPlugin() {
        final Consumer<TextDrawContext> horizontal = ctx -> {
            ctx.drawAligned(Component.text("Left"), 0f);
            ctx.drawAligned(Component.text("Centred"), 0.5f);
            ctx.drawAligned(Component.text("Right"), 1f);
        };

        final Consumer<TextDrawContext> lines = ctx -> {
            for (int i = 0; i < 12; i++) {
                ctx.moveCursor(0, -48 + (i * 8));
                ctx.draw(Component.text("Line " + (i + 1)));
            }
        };

        final Consumer<TextDrawContext> background = ctx -> {
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
        };

        final Consumer<TextDrawContext> dialogue = ctx -> {
            ctx.drawAligned(Component.text("\uE001").font(GLYPH_FONT), 0.5f);
            ctx.moveCursor(4, 0);
            ctx.draw(Component.text("NPC Name").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
            ctx.moveCursor(4, 12);
            ctx.draw(Component.text("According to all known laws of aviation,"));
            ctx.moveCursor(4, 20);
            ctx.draw(Component.text("there is no way a bee should be able to fly."));
            ctx.moveCursor(251, 32);
            ctx.draw(Component.text("Click").color(NamedTextColor.GRAY), 1);
        };

        final Consumer<TextDrawContext> victory = ctx -> {
            ctx.drawAligned(Component.text("\uE001").font(GLYPH_FONT), 0.5f);
            ctx.moveCursor(ctx.cursorX(), -8);
            ctx.drawAligned(Component.text("VICTORY!").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD), 0.5f);
            ctx.moveCursor(24, 8);
            ctx.draw(Component.text("Kills"));
            ctx.moveCursor(102, 8);
            ctx.draw(Component.text("15").decorate(TextDecoration.BOLD), 1);

            ctx.moveCursor(153, 8);
            ctx.draw(Component.text("XP"));
            ctx.moveCursor(228, 8);
            ctx.draw(Component.text("+99999").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD), 1);

            ctx.moveCursor(24, 20);
            ctx.draw(Component.text("Deaths"));
            ctx.moveCursor(102, 20);
            ctx.draw(Component.text("2").decorate(TextDecoration.BOLD), 1);

            ctx.moveCursor(153, 20);
            ctx.draw(Component.text("Coins"));
            ctx.moveCursor(228, 20);
            ctx.draw(Component.text("+500").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD), 1);

            ctx.moveCursor(ctx.cursorX(), 32);
            ctx.drawAligned(Component.text("Click to continue").color(NamedTextColor.GRAY), 0.5f);
        };

        this.layouts = Map.of(
                "horizontal", horizontal,
                "lines", lines,
                "background", background,
                "dialogue", dialogue,
                "victory", victory
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

                            actionBarTextHolder.setText(player, tinsel.draw(255, Style.style(ShadowColor.none()), layout));
                            return 0;
                        })
        ).build();
    }

    @Override
    public void onEnable() {
        actionBarTextHolder = new ActionBarTextHolder(this);
        getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                (event) -> event.registrar().register(node())
        );
        super.onEnable();
    }
}
