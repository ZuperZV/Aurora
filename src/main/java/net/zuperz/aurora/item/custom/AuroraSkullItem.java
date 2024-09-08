package net.zuperz.aurora.item.custom;

import net.minecraft.world.item.Item;

public class AuroraSkullItem extends Item {
    private int Number;

    public AuroraSkullItem(int startValue, Item.Properties properties) {
        super(properties);
        this.Number = startValue;
    }

    public void setValue(int newValue) {
        this.Number = newValue;
    }

    public int getValue() {
        return this.Number;
    }
}
