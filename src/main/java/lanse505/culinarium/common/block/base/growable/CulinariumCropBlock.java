package lanse505.culinarium.common.block.base.growable;

import lanse505.culinarium.common.block.base.core.ICulinariumCoreBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CulinariumCropBlock extends CropBlock implements ICulinariumCoreBlock {

    protected final int maxAge;

    public CulinariumCropBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.maxAge = 7;
    }

    public CulinariumCropBlock(BlockBehaviour.Properties pProperties, int maxAge) {
        super(pProperties);
        this.maxAge = maxAge;
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }
}
