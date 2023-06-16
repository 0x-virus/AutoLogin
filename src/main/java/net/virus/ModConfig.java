package net.virus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "autologin.json");
    private static ModConfig instance;
    private String text;
    private List<String> serverIPs;

    private ModConfig() {
        // Default values
        text = "Hello, world!";
        serverIPs = Arrays.asList("127.0.0.1", "192.168.0.1");
    }

    public static ModConfig getInstance() {
        if (instance == null) {
            instance = new ModConfig();
            instance.loadConfig();
        }
        return instance;
    }

    public String getText() {
        return text;
    }

    public List<String> getServerIPs() {
        return serverIPs;
    }

    public void saveConfig() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            writer.write(GSON.toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        if (!CONFIG_FILE.exists()) {
            // Создаем новый конфигурационный файл, если он не существует
            try {
                CONFIG_FILE.createNewFile();
                saveConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            ModConfig config = GSON.fromJson(reader, ModConfig.class);
            if (config != null) {
                text = config.text;
                serverIPs = config.serverIPs;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
