package me.hsgamer.badappleboard;

import me.hsgamer.betterboard.builder.BoardProviderBuilder;
import me.hsgamer.betterboard.lib.core.config.PathString;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class BadAppleBoard extends JavaPlugin {
    private final List<Frame> jpFrames = new ArrayList<>();
    private final List<Frame> enFrames = new ArrayList<>();

    @Override
    public void onEnable() {
        jpFrames.addAll(load(getResource("badapple_jp.txt")));
        enFrames.addAll(load(getResource("badapple_en.txt")));

        BoardProviderBuilder.INSTANCE.register(c -> {
            AppleBoardProvider provider;
            if (c.getInstance(new PathString("lang"), "en", String.class).equalsIgnoreCase("en")) {
                provider = new AppleBoardProvider(enFrames);
            } else {
                provider = new AppleBoardProvider(jpFrames);
            }
            provider.loadFromConfig(c);
            return provider;
        }, "badapple", "apple");
    }

    @Override
    public void onDisable() {
        jpFrames.forEach(Frame::clear);
        enFrames.forEach(Frame::clear);
        jpFrames.clear();
        enFrames.clear();
    }

    private List<Frame> load(InputStream inputStream) {
        List<Frame> list = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            int i = 0;
            String line = bufferedReader.readLine();
            Frame frame = null;
            while (line != null) {
                if (i == 0) {
                    frame = new Frame();
                }
                if (i != 12 && i != 11 && i != 10) {
                    frame.add(line);
                }
                if (i == 12) {
                    list.add(frame);
                }
                i = (i + 1) % 13;
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            list.forEach(Frame::clear);
            list.clear();
        }
        return list;
    }
}
