package carscupcake;

import com.google.gson.GsonBuilder;

import javax.swing.plaf.PanelUI;
import java.io.*;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public class Main {
    public static void main(String[] args) {
        var dir = new File("./sbremake-data/src/main/resources/assets/schematics/dungeon/rooms/1x1");
        trace(dir);
    }

    public static void trace(File f) {
        if (f.isDirectory()) {
            if (f.getName().equals("puzzle")) return;
            if (f.getName().equals("L-shape")) return;
            for (File c : Objects.requireNonNull(f.listFiles())) {
                trace(c);
            }
            return;
        }
        try (var input = new FileInputStream(f); var stream = new GZIPInputStream(input); var isr = new InputStreamReader(stream); var reader = new BufferedReader(isr)) {
            var json  = new GsonBuilder().create();
            var obj = json.fromJson(reader, SchematicsModel.class);
            System.out.println(f.getName());
            var y = findShapeY(0, 0, obj);
            if (y <= 0) return;
            System.out.println(y);
            var doorModel = new DoorwaysModel(isDoor(4, y, 0, obj),
                    isDoor(8, y, 4, obj),
                    isDoor(4, y, 8, obj),
                    isDoor(0, y, 4, obj));
            System.out.println(doorModel);
            var file = new File(f.getParentFile(), f.getName() + ".json");
            try(var out = new FileOutputStream(file); var writer = new  OutputStreamWriter(out)) {
                var str = json.toJson(doorModel);
                writer.write(str);
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isDoor(int x, int y, int z, SchematicsModel obj) {
        var id = obj.blocks()[x][y][z];
        var block = obj.pallete()[id];
        if (block.id().endsWith("red_terracotta") || block.id().endsWith("blue_terracotta")) return false;
        if (block.id().endsWith("lime_terracotta")) return true;
        throw new RuntimeException("Invalid block id " + block.id());
    }

    public static int findShapeY(int x, int z, SchematicsModel obj) {
        var airId = -1;
        var y = obj.blocks()[0].length;
        while (y-- != 0) {
            var index = obj.blocks()[x][y][z];
            if (airId == -1) {
                airId = index;
                continue;
            }
            if (index == airId) continue;
            var other = obj.pallete()[index];
            if (!other.id().equals("minecraft:blue_terracotta")) {
                System.out.println("Unexpected Block: " + other.id());
                return -1;
            }
            break;
        }
        return y;
    }
}
