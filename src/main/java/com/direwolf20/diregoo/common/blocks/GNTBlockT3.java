package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.Config;

public class GNTBlockT3 extends GNTBlockT1 {

    public GNTBlockT3() {
        super();
    }

    public int getRadius() {
        return Config.GNT_TIER3_RADIUS.get();
    }

}
