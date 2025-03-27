package me.carscupcake.sbremake.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Pair<T, K> {
    private T first;
    private K second;
}
