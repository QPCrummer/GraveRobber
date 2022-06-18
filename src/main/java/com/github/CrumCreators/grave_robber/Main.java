package com.github.CrumCreators.grave_robber;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;

public class Main implements ModInitializer {

    public static String cfgver;
    public static int time;
    public static int invtime;
    public static double height;
    public static boolean perms;

    public static Properties properties = new Properties();

    @Override
    public void onInitialize()  {
        var path = FabricLoader.getInstance().getConfigDir().resolve("grave-robber.properties");

        if (Files.notExists(path)) {
            try {
                mkfile();
                System.out.println("Creating GraveRobber config");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                loadcfg();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cfgver = properties.getProperty("config-version");
            if (!(Objects.equals(cfgver, "1.0"))) {
                try {
                    mkfile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Updating GraveRobber config");
            } else {
                parse();
            }
        }
    }

    public void mkfile() throws IOException {
        OutputStream output = new FileOutputStream(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("grave-robber.properties")));
        if (!properties.contains("config-version")) {properties.setProperty("config-version", "1.0");}
        if (!properties.contains("teleport-timer-seconds")) {properties.setProperty("teleport-timer-seconds", "5");}
        if (!properties.contains("teleport-height-above-grave")) {properties.setProperty("teleport-height-above-grave", "0.5");}
        if (!properties.contains("teleport-invulnerability-seconds")) {properties.setProperty("teleport-invulnerability-seconds", "2");}
        if (!properties.contains("allow-everyone-teleporting")) {properties.setProperty("allow-everyone-teleporting", "true");}
        properties.store(output, null);
        parse();
    }

    public void loadcfg() throws IOException {
        InputStream input = new FileInputStream(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("grave-robber.properties")));
        properties.load(input);
    }

    public void parse() {
        cfgver = properties.getProperty("config-version");
        time = Integer.parseInt(properties.getProperty("teleport-timer-seconds"));
        height = Double.parseDouble(properties.getProperty("teleport-height-above-grave"));
        invtime = Integer.parseInt(properties.getProperty("teleport-invulnerability-seconds"))*1000;
        perms = Boolean.parseBoolean(properties.getProperty("allow-everyone-teleporting"));

    }
}

