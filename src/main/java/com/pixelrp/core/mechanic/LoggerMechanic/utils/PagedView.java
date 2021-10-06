package com.pixelrp.core.mechanic.LoggerMechanic.utils;

import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PagedView {

    private static final String HEADING = "========== %s (%d/%d) ==========";

    private final String name;
    private final int perPage;
    private List<Text> contents = new ArrayList<>();

    public PagedView(String name) {
        this(name, 9);
    }

    public PagedView(String name, int perPage) {
        this.name = name;
        this.perPage = perPage;
    }

    public void addLine(Text line) {
        contents.add(line);
    }

    public Optional<Text> getPage(int page) {
        int maxPages = Math.max(1, (contents.size() + perPage - 1) / perPage);

        if (page < 1 || page > maxPages) {
            return Optional.empty();
        }

        Text.Builder builder = Text.builder();
        builder.append(Text.of(String.format(HEADING, name, page, maxPages))).append(Text.NEW_LINE);

        for (int i = (page - 1) * perPage; i < Math.min(page * perPage, contents.size()); i++) {
            builder.append(contents.get(i));
            if (i < Math.min(page * perPage, contents.size()) - 1) {
                builder.append(Text.NEW_LINE);
            }
        }

        return Optional.of(builder.build());
    }
}
