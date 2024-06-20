package me.hsgamer.badappleboard;

import me.hsgamer.betterboard.lib.core.bukkit.utils.ColorUtils;
import me.hsgamer.betterboard.lib.core.common.Validate;
import me.hsgamer.betterboard.lib.core.config.Config;
import me.hsgamer.betterboard.lib.core.config.PathString;
import me.hsgamer.betterboard.lib.core.variable.VariableManager;
import me.hsgamer.betterboard.provider.board.FastBoardProvider;
import me.hsgamer.betterboard.provider.board.internal.BoardFrame;
import me.hsgamer.hscore.animate.Animation;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AppleBoardProvider extends FastBoardProvider {
    private final List<Frame> frames = new ArrayList<>();
    private final Map<UUID, Animation<Frame>> animationMap = new ConcurrentHashMap<>();
    private String title = "&u&lBad Apple";
    private int fps = 35;

    public AppleBoardProvider(List<Frame> frames) {
        this.frames.addAll(frames);
    }

    private Animation<Frame> getAnimation(UUID uuid) {
        return this.animationMap.computeIfAbsent(uuid, uuid1 -> new Animation<>(frames, 1000L / this.fps));
    }

    @Override
    public Optional<BoardFrame> fetch(Player player) {
        if (this.frames.isEmpty()) {
            return Optional.empty();
        }
        Animation<Frame> animation = getAnimation(player.getUniqueId());
        return Optional.of(new BoardFrame(ColorUtils.colorize(VariableManager.GLOBAL.setVariables(title, player.getUniqueId())), animation.getCurrentFrame().getList()));
    }

    @Override
    public void clear() {
        super.clear();
        this.frames.clear();
    }

    @Override
    public void loadFromConfig(Config config) {
        super.loadFromConfig(config);
        this.title = config.getInstance(FastBoardProvider.TITLE_PATH, title, String.class);
        this.fps = Optional.ofNullable(config.getNormalized(new PathString("fps")))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(integer -> integer > 0)
                .orElse(fps);
    }
}
