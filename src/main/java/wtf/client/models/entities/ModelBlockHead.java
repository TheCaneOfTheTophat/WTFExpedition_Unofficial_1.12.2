package wtf.client.models.entities;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.entities.customentities.EntityBlockHead;

@SideOnly(Side.CLIENT)
public class ModelBlockHead extends ModelPlayer {

    public ModelBlockHead(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.rightArmPose = ModelBiped.ArmPose.EMPTY;
        this.leftArmPose = ModelBiped.ArmPose.EMPTY;
        ItemStack itemstack = entitylivingbaseIn.getHeldItem(EnumHand.MAIN_HAND);

        if (itemstack.getItem() instanceof net.minecraft.item.ItemBow && ((EntityBlockHead)entitylivingbaseIn).isSwingingArms())
            if (entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT)
                this.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
            else
                this.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;

        super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
    }
}
