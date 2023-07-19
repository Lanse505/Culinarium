package lanse505.culinarium.common.block.base.growable;

import lanse505.culinarium.common.block.base.core.ICulinariumCoreBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraftforge.common.IPlantable;

public class CulinariumBushBlock extends BushBlock implements IPlantable, ICulinariumCoreBlock {

    public CulinariumBushBlock(Properties pProperties) {
        super(pProperties);
    }

}
