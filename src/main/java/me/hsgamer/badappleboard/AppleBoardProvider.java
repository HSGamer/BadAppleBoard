package me.hsgamer.badappleboard;

import me.hsgamer.betterboard.api.BoardFrame;
import me.hsgamer.betterboard.api.provider.ConfigurableBoardProvider;
import me.hsgamer.betterboard.lib.core.bukkit.utils.MessageUtils;
import me.hsgamer.betterboard.lib.core.config.Config;
import me.hsgamer.betterboard.provider.ConditionProvider;
import me.hsgamer.hscore.variable.VariableManager;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AppleBoardProvider implements ConfigurableBoardProvider {
    private final List<Frame> frames = new ArrayList<>();
    private final Map<UUID, Integer> currentIndexMap = new ConcurrentHashMap<>();
    private final ConditionProvider conditionProvider = new ConditionProvider();
    private String title = "&u&lBad Apple";

    public AppleBoardProvider(List<Frame> frames) {
        this.frames.addAll(frames);
    }

    @Override
    public boolean canFetch(Player player) {
        return this.conditionProvider.check(player);
    }

    @Override
    public Optional<BoardFrame> fetch(Player player) {
        if (this.frames.isEmpty()) {
            return Optional.empty();
        }
        int index = this.currentIndexMap.computeIfAbsent(player.getUniqueId(), uuid -> 0);
        this.currentIndexMap.put(player.getUniqueId(), (index + 1) % this.frames.size());
        return Optional.of(new BoardFrame(MessageUtils.colorize(VariableManager.setVariables(title, player.getUniqueId())), this.frames.get(index).getList()));
    }

    @Override
    public void clear() {
        this.frames.clear();
    }

    @Override
    public void loadFromConfig(Config config) {
        this.conditionProvider.loadFromMap(config.getNormalizedValues("condition", false));
        this.title = config.getInstance("title", title, String.class);
    }
}
