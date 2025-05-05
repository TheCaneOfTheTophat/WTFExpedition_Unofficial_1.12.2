package wtf.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.enums.Modifier;
import wtf.init.BlockSets;

public class ItemDebugModifier extends Item {

    Modifier modifier;

    public ItemDebugModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos);

        IBlockState transformed = BlockSets.getTransformedState(state, modifier);

        if (!worldIn.isRemote && transformed != null)
            if (worldIn.setBlockState(pos, transformed, 11)) {
                worldIn.playEvent(2001, pos, Block.getStateId(transformed));
                return EnumActionResult.SUCCESS;
            }

        return EnumActionResult.PASS;
    }

    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(ItemStack stack) {
        switch(modifier) {
            case FRACTURED:
                return "Fracture";
            case CRACKED:
                return "Crack";
            case LAVA_CRUST:
                return "Lava Crust";
            case MOSS:
                return "Moss";
            case WET:
                return "Wet";
            case LAVA_DRIPPING:
                return "Lava Drip";
            case SOUL:
                return "Soul";
            case BRICK:
            default:
                return "Brick";
        }
    }
}
