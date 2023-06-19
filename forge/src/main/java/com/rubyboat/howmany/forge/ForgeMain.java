package com.rubyboat.howmany.forge;

import com.rubyboat.howmany.CommonMain;
import net.minecraftforge.fml.common.Mod;

@Mod(CommonMain.MOD_ID)
public class ForgeMain {


    public ForgeMain() {
        CommonMain.init();
    }
}