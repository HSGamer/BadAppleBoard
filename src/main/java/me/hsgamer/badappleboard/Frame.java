package me.hsgamer.badappleboard;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private final List<String> list = new ArrayList<>();

    public List<String> getList() {
        return list;
    }

    public void add(String line) {
        list.add(line);
    }

    public void clear() {
        list.clear();
    }
}
