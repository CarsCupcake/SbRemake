package me.carscupcake.sbremake.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<T, K> {
    private T first;
    private K second;
}
