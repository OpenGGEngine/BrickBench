package com.opengg.loader;

import com.opengg.core.engine.Resource;
import com.opengg.loader.game.nu2.NU2MapData;
import com.opengg.loader.game.nu2.scene.SceneFileLoader;
import com.opengg.loader.loading.MapLoader;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public class Profiler {
    public static void main(String[] args) throws IOException {
        var fileName = Resource.getUserDataPath().resolve("THINGS_PC.GSC");
        String[] maps = new String[]{"C:\\Users\\warre\\Desktop\\TCSMODS\\ModdedClean\\LEVELS\\EPISODE_I\\PODSPRINT\\PODSPRINT_A\\PODSPRINT_A_PC.GSC","C:\\Users\\warre\\Desktop\\TCSMODS\\ModdedClean\\LEVELS\\EPISODE_IV\\MOSEISLEY\\MOSEISLEY_A\\MOSEISLEY_A_PC.GSC","C:\\Users\\warre\\Desktop\\TCSMODS\\ModdedClean\\LEVELS\\EPISODE_I\\PODSPRINT\\PODSPRINT_A\\PODSPRINT_A.TER"};
        long time = System.nanoTime();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < maps.length; j++) {
                fileName = Path.of(maps[j]);
                var mapData = new NU2MapData("THINGS_PC.GSC",
                        new MapXml("THINGS_PC.GSC", fileName, MapXml.MapType.SINGLE_FILE,
                                List.of(fileName), Map.of(), Map.of()),
                        new NU2MapData.SceneData(),
                        new NU2MapData.GizmoData(),
                        new NU2MapData.TerrainData(),
                        new NU2MapData.AIData(),
                        new NU2MapData.TxtData(),
                        new NU2MapData.GitData(),
                        new NU2MapData.RTLData());

                var file = FileChannel.open(fileName, StandardOpenOption.READ);

                mapData = mapData.loadFile(new MapLoader.MapFile(fileName, file));
            }
        }
        long finaltime = System.nanoTime();
        System.out.println((finaltime - time)/1e9f);
    }
}
