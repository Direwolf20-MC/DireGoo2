package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.DireGoo;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class GeneratorLanguage extends LanguageProvider {
    public GeneratorLanguage(DataGenerator gen) {
        super(gen, DireGoo.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("screen.diregoo.energy", "Energy: %s/%s FE");
        //addBlock(ModBlocks.CHARGING_STATION, "Charging Station");
        //add("itemGroup.charginggadgets", "Charging Gadgets");
        //add("screen.charginggadgets.energy", "Energy: %s/%s FE");
        //add("screen.charginggadgets.no_fuel", "Fuel source empty");
        //add("screen.charginggadgets.burn_time", "Burn time left: %ss");
    }
}
