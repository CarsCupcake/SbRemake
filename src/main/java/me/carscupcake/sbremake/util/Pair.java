package me.carscupcake.sbremake.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.carscupcake.sbremake.config.ConfigField;
import me.carscupcake.sbremake.config.DefaultConfigItem;

@Data
@AllArgsConstructor
public class Pair<T, K> implements DefaultConfigItem {
    @ConfigField
    private T first;
    @ConfigField
    private K second;

    public Pair() {}
}
