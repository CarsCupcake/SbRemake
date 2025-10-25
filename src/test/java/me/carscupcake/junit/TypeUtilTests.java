package me.carscupcake.junit;

import me.carscupcake.sbremake.player.Essence;
import me.carscupcake.sbremake.util.CountMap;
import me.carscupcake.sbremake.util.TypeUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TypeUtilTests {
    private final HashMap<String, Integer> testA = new HashMap<>();
    private final Map<String, Integer> testB = new HashMap<>();
    @Test
    public void testHashMapInstance() throws Exception {
        var types = TypeUtil.getMapGenericTypes(getClass().getDeclaredField("testA").getGenericType());
        assert types.length == 2;
        assert types[0].equals(String.class);
        assert types[1].equals(Integer.class);
    }

    @Test
    public void testMapInstance() throws Exception {
        var types = TypeUtil.getMapGenericTypes(getClass().getDeclaredField("testB").getGenericType());
        assert types.length == 2;
        assert types[0].equals(String.class);
        assert types[1].equals(Integer.class);
    }

    private final CountMap<Essence> singleParameterTest = new CountMap<>();
    @Test
    public void testSingleParameter() throws Exception {
        var types = TypeUtil.getMapGenericTypes(getClass().getDeclaredField("singleParameterTest").getGenericType());
        assert types.length == 2;
        assert types[0].equals(Essence.class);
        assert types[1].equals(Integer.class);
    }

    @Test
    public void testCustomInstance() {
        var types = TypeUtil.getMapGenericTypes(StringIntTest.class);
        assert types.length == 2;
        assert types[0].equals(String.class);
        assert types[1].equals(Integer.class);
    }
    public static class StringIntTest extends HashMap<String, Integer> {}
}
