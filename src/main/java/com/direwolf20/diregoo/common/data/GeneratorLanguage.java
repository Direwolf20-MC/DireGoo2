package com.direwolf20.diregoo.common.data;

import com.direwolf20.diregoo.DireGoo;
import com.direwolf20.diregoo.common.blocks.ModBlocks;
import com.direwolf20.diregoo.common.items.ModItems;
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
        add("screen.diregoo.antigoofieldgen.active", "Active %1$d");
        add("screen.diregoo.antigoofieldgen.renderarea", "Render %1$d");
        add("diregoo.tooltip.screen.xrange", "Range X");
        add("diregoo.tooltip.itemenergy", "Energy: %d/%d");
        add("block.diregoo.zapperitemscreen", "Goo Zapper");
        add("block.diregoo.turretscreen", "Goo Turret");
        add("block.diregoo.antigoobeaconscreen", "Antigoo Beacon");
        add("diregoo.message.toomuchgoo", "Too Much Goo to Render: ");
        add("diregoo.message.goocount", "Detected Goo Count: ");
        addBlock(ModBlocks.ANTI_GOO_BEACON, "Antigoo Beacon");
        addBlock(ModBlocks.ANTI_GOO_FIELD_GEN, "Antigoo Field Generator");
        addBlock(ModBlocks.GOO_BLOCK, "Goo");
        addBlock(ModBlocks.GOO_BLOCK_BURST, "Burst Goo");
        addBlock(ModBlocks.GOO_BLOCK_POISON, "Poisoned Goo");
        addBlock(ModBlocks.GOO_BLOCK_TERRAIN, "Terrain Goo");
        addBlock(ModBlocks.GOO_DETECTOR, "Goo Detector");
        addBlock(ModBlocks.TURRET_BLOCK, "Goo Turret");
        addBlock(ModBlocks.ZAPPER_TURRET_BLOCK, "Goo Zapper Turret");
        addItem(ModItems.FOCUS_T1, "Basic Focus Crystal");
        addItem(ModItems.FOCUS_T2, "Improved Focus Crystal");
        addItem(ModItems.FOCUS_T3, "Powerful Focus Crystal");
        addItem(ModItems.FOCUS_T4, "Extreme Focus Crystal");
        addItem(ModItems.POWERAMP_T1, "Basic Power Amplifier");
        addItem(ModItems.POWERAMP_T2, "Improved Power Amplifier");
        addItem(ModItems.POWERAMP_T3, "Powerful Power Amplifier");
        addItem(ModItems.POWERAMP_T4, "Extreme Power Amplifier");
        addItem(ModItems.ANTI_GOO_DUST, "AntiGoo Dust");
        addItem(ModItems.ANTI_GOO_PASTE, "AntiGoo Paste");
        addItem(ModItems.CORE_FREEZE, "Freezing Core");
        addItem(ModItems.CORE_MELT, "Melting Core");
        addItem(ModItems.GOONADE, "Goonade");
        addItem(ModItems.GOONADE_FREEZE, "Freezing Goonade");
        addItem(ModItems.GOO_REMOVER, "Goo Remover");
        addItem(ModItems.GOO_RESIDUE, "Goo Residue");
        addItem(ModItems.GOO_SCANNER, "Goo Scanner");
        addItem(ModItems.GOO_ZAPPER, "Goo Zapper");


        //addBlock(ModBlocks.CHARGING_STATION, "Charging Station");
        //add("itemGroup.charginggadgets", "Charging Gadgets");
        //add("screen.charginggadgets.energy", "Energy: %s/%s FE");
        //add("screen.charginggadgets.no_fuel", "Fuel source empty");
        //add("screen.charginggadgets.burn_time", "Burn time left: %ss");
    }
}
