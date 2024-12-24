package me.lucyydotp.tinsel.testplugin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.lucyydotp.tinsel.Tinsel;
import me.lucyydotp.tinsel.layout.TextLayoutBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class TinselTestPlugin extends JavaPlugin {

    private final Tinsel tinsel = Tinsel.withTinselPack();

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

        this.layouts = Map.of(
                "one", one,
                "two", two,
                "three", three,
                "four", four
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
