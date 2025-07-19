package me.carscupcake.sbremake.player.accessories;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.util.Lazy;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public enum AccessoryBagPowers implements AccessoryBagPower {
    Fortuitous,
    Pretty,
    Protected,
    Simple,
    Warrior,
    Commando,
    Disciplined,
    Inspired,
    Ominous,
    Prepared,
    Silky,
    Sweet,
    Adept,
    Bloody,
    Forceful,
    Itchy,
    Mythical,
    Shaded,
    Sighted,
    Bizarre,
    Demonic,
    Hurtful,
    Pleasant,
    Sanguisuge,
    Frozen,
    Healthy,
    Slender,
    Strong,
    Bubba,
    Crumbly,
    Scorching;
    private final Map<Stat, Number> statMultipliers = new HashMap<>();
    private static Lazy<List<Map<String, String>>> fileInfo = null;

    AccessoryBagPowers() {
        for (var info : getFileInfo()) {
            if (info.get("Power").equals(name())) {
                for  (var entry : info.entrySet()) {
                    if (entry.getKey().equals("Power")) continue;
                    statMultipliers.put(Stat.valueOf(entry.getKey()), Double.parseDouble(entry.getValue()));
                }
                break;
            }
        }
    }
    public static List<Map<String, String>> getFileInfo() {
        if (fileInfo == null) {
            fileInfo = new Lazy<>(() -> {
                List<Map<String, String>> response = new LinkedList<>();
                CsvMapper mapper = new CsvMapper();
                CsvSchema schema = CsvSchema.emptySchema().withHeader();
                MappingIterator<Map<String, String>> iterator;
                try {
                    iterator = mapper.readerFor(Map.class)
                            .with(schema)
                            .readValues(Main.class.getClassLoader().getResourceAsStream("assets/powers.csv"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                while (iterator.hasNext()) {
                    response.add(iterator.next());
                }
                return response;
            });
        }
        return fileInfo.get();
    }

    public double getStat(Stat stat, int magicPower) {
        var mult = statMultipliers.getOrDefault(stat, 0);
        if (mult == null) return 0;
        return Math.round((mult.doubleValue() / 100d) * getMultiplier(stat) * 719.28 * Math.pow(Math.log(1 + 0.0019 * magicPower), 1.2));
    }

    private double getMultiplier(Stat stat) {
        return switch (stat) {
            case Intelligence -> 1.5;
            case Health -> 1.4;
            case Strength, Defense, CritDamage -> 1;
            case Speed -> 0.5;
            case CritChance -> 0.4;
            case Vitality -> 0.333;
            case AttackSpeed -> 0.3;
            case TrueDefense -> 0.27;
            case Mending -> 0.25;
            default -> 0;
        };
    }
}
