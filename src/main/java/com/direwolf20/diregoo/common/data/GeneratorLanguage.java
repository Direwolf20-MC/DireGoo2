package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class GeneratorLanguage extends LanguageProvider {
    public GeneratorLanguage(DataGenerator gen) {
        super(gen, DireGoo.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("screen.diregoo.energy", "Energy: %s/%s FE");
        add("screen.diregoo.antigoobeacon.fuel_remaining", "Fuel Time Remaining: %d");
        add("screen.diregoo.turret.shots_remaining", "Boost Shots Remaining: %d");
        add("diregoo.tooltip.screen.xrange", "Range X");
        add("diregoo.tooltip.itemenergy", "Energy: %d/%d");
        addBlock(ModBlocks.ANTI_GOO_FIELD_GEN, "Antigoo Field Generator");
        addBlock(ModBlocks.ZAPPER_TURRET_BLOCK, "Zapper Turret");
        //addBlock(ModBlocks.CHARGING_STATION, "Charging Station");
        //add("itemGroup.charginggadgets", "Charging Gadgets");
        //add("screen.charginggadgets.energy", "Energy: %s/%s FE");
        //add("screen.charginggadgets.no_fuel", "Fuel source empty");
        //add("screen.charginggadgets.burn_time", "Burn time left: %ss");
    }
}
