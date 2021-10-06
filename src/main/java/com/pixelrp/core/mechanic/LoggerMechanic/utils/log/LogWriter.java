package com.pixelrp.core.mechanic.LoggerMechanic.utils.log;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class LogWriter {

    private final CommandSource sender;
    private final File file;

    public LogWriter(CommandSource sender, File file) {
        this.sender = sender;
        this.file = file;
    }

    public void write() {
        try {
            FileWriter writer = new FileWriter(file);
            write0(writer);
            try {
                writer.close();
            } catch (IOException ignore) {
            }
            sender.sendMessage(Text.of(TextColors.GREEN, "Log file generated."));
        } catch (IOException e) {
            sender.sendMessage(Text.of(TextColors.RED, "Failed to save the log. Exception printed into the console."));
            e.printStackTrace();
        }
    }

    protected abstract void write0(final FileWriter writer) throws IOException;
}
