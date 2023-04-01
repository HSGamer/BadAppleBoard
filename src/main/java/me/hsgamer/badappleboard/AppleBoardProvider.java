package me.hsgamer.badappleboard;

import me.hsgamer.betterboard.lib.core.config.Config;
import me.hsgamer.betterboard.lib.core.variable.VariableManager;
import me.hsgamer.betterboard.provider.board.FastBoardProvider;
import me.hsgamer.betterboard.provider.board.internal.BoardFrame;
import me.hsgamer.hscore.bukkit.utils.ColorUtils;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AppleBoardProvider extends FastBoardProvider {
    private final List<Frame> frames = new ArrayList<>();
    private final Map<UUID, Integer> currentIndexMap = new ConcurrentHashMap<>();
    private String title = "&u&lBad Apple";

    public AppleBoardProvider(List<Frame> frames) {
        this.frames.addAll(frames);
    }

    @Override
    public Optional<BoardFrame> fetch(Player player) {
        if (this.frames.isEmpty()) {
            return Optional.empty();
        }
        int index = this.currentIndexMap.computeIfAbsent(player.getUniqueId(), uuid -> 0);
        this.currentIndexMap.put(player.getUniqueId(), (index + 1) % this.frames.size());
        return Optional.of(new BoardFrame(ColorUtils.colorize(VariableManager.setVariables(title, player.getUniqueId())), this.frames.get(index).getList()));
    }

    @Override
    public void clear() {
        super.clear();
        this.frames.clear();
    }

    @Override
    public void loadFromConfig(Config config) {
        super.loadFromConfig(config);
        this.title = config.getInstance("title", title, String.class);
    }
}
