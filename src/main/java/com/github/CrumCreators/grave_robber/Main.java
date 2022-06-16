package com.github.CrumCreators.grave_robber;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

public class Main implements ModInitializer {

    public static int time;

    public Main() {
    }

    Properties properties = new Properties();

    @Override
    public void onInitialize() {

        var path = FabricLoader.getInstance().getConfigDir().resolve("grave-robber.properties");

        if (Files.notExists(path)) {
            try {
                mkfile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            time = Integer.parseInt(properties.getProperty("teleport-timer-seconds"));
        }

        else {
            try {
                InputStream input = new FileInputStream(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("grave-robber.properties")));
                properties.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void mkfile() throws IOException {
        OutputStream output = new FileOutputStream(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("grave-robber.properties")));
        properties.setProperty("teleport-timer-seconds", "5");
        properties.store(output, null);
        time = Integer.parseInt(properties.getProperty("teleport-timer-seconds"));
    }
}

