package com.pixelrp.core.utils.database;

import com.pixelrp.core.PixelRP;

import java.io.*;
import java.nio.file.Files;

public class Database {

    private File dbFile = null;

    public Database(String name){
        dbFile = PixelRP.instance.getConfigDir().resolve(name + ".db").toFile();
        try {
            if (!dbFile.exists()) {
                Files.createFile(dbFile.toPath());
            }
        } catch (IOException ex) {
            PixelRP.instance.logger.error("Failed to load " + dbFile.getName() + "...", ex);
        }
    }

    public void saveDataBase(Object value) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dbFile));
            oos.writeObject(value);
            oos.flush();
            oos.close();
            PixelRP.instance.logger.info("Log database saved.");
        } catch (IOException ex) {
            PixelRP.instance.logger.error("Failed to save " + dbFile.getName() + "...", ex);
        }
    }

    public Object loadDataBase() throws IOException {
        Object temp = null;

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dbFile));
        try {
            temp = (Object) ois.readObject();
        } catch (ClassNotFoundException ex) {
            PixelRP.instance.logger.error("Failed to load " + dbFile.getName() + "..." + " Invalid database format.");
        }
        try {
            ois.close();
        } catch (Exception ignore) {
        }
        return temp;
    }

}
