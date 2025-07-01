package me.carscupcake.sbremake.item.modifiers.attributes;

import lombok.Getter;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.SbItemStack;
import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AbstractAttribute {
    private final String id;
    private final String name;
    private final Lore lore;

    public static Map<String, AbstractAttribute> attributes = new HashMap<>();

    public AbstractAttribute(String name, Lore lore) {
        this.id = name.toLowerCase().replace(" ", "_");
        this.lore = lore;
        this.name = name;
    }

    protected ItemType getSingleType() {
        return null;
    }
    boolean isApplicable(SbItemStack stack) {
        if (getSingleType() == null) throw new NotImplementedException("getSingleType() not implemented. Eather implement isApplicable or getSingleType");
        return stack.sbItem().getType() == getSingleType();
    }
}
