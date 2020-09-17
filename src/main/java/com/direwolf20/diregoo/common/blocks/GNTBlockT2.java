package com.direwolf20.diregoo.common.blocks;

import com.direwolf20.diregoo.Config;

public class GNTBlockT2 extends GNTBlockT1 {

    public GNTBlockT2() {
        super();
    }

    public int getRadius() {
        return Config.GNT_TIER2_RADIUS.get();
    }

}
