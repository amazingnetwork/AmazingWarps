package br.com.amazingnetwork.managers;
import br.com.amazingnetwork.AmazingWarps;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlFileManager {

    AmazingWarps main = AmazingWarps.getInstance();

    boolean isNewFile;
    File directory;
    File file;
    FileConfiguration fileConfiguration;

    public YamlFileManager(String directory, String fileName, boolean isNewFile) {
        this.isNewFile = isNewFile;

        String fileNameExt = fileName + ".yml";

        createDirectory(directory);
        createFile(directory, fileNameExt);

        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void createDirectory(String directoryName) {
        directory = main.getDataFolder();

        if(directoryName != null) {
            directory = new File(main.getDataFolder(), directoryName.replace("/", File.separator));
            directory.mkdirs();
        }
    }

    public void createFile(String directory, String fileName) {
        file = new File(this.directory, fileName);

        if(!file.exists()) {
            if(this.isNewFile) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                main.saveResource(directory != null ? directory + File.separator + fileName : fileName, false);
            }
        }
    }

    public FileConfiguration getFile() {
        return fileConfiguration;
    }

    public void saveFile() {
        try {
            fileConfiguration.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reloadFile() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void setPropertyFile(String property, Object value) {
        fileConfiguration.set(property, value);
    }
}
