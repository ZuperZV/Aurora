package net.zuperz.aurora.item.custom;

import net.minecraft.world.item.Item;

public class AuroraSkullItem extends Item {
    private int number;

    public AuroraSkullItem(int startValue, Item.Properties properties) {
        super(properties);
        this.number = startValue;
    }

    // Method to set the value for this instance
    public void setValue(int newValue) {
        this.number = newValue;
    }

    // Method to get the value for this instance
    public int getValue() {
        return this.number;
    }
}
