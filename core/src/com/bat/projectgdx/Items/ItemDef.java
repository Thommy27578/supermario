package com.bat.projectgdx.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by MSC on 01.04.2016.
 */
public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
