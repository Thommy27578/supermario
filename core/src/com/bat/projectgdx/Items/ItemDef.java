package com.bat.projectgdx.Items;

import com.badlogic.gdx.math.Vector2;



//Item Definition - Beinhaltet nur die Position und den Untertypen (Um welche Art Item es sich handelt)

public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
