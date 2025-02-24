package wtf.client.renderers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.client.models.entities.ModelDerangedIronGolem;
import wtf.entities.customentities.EntityDerangedIronGolem;

@SideOnly(Side.CLIENT)
public class RenderDerangedIronGolem extends RenderLiving<EntityDerangedIronGolem> {

    private static final ResourceLocation IRON_GOLEM_TEXTURES = new ResourceLocation("textures/entity/iron_golem.png");

    public RenderDerangedIronGolem(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelDerangedIronGolem(), 0.5F);
    }

    @Override
	protected ResourceLocation getEntityTexture(EntityDerangedIronGolem entity) {
        return IRON_GOLEM_TEXTURES;
    }
    
    @Override
    protected void preRenderCallback(EntityDerangedIronGolem entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(0.7F, 0.7F, 0.7F);
    }

    @Override
    protected void applyRotations(EntityDerangedIronGolem entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
        super.applyRotations(entityLiving, p_77043_2_, p_77043_3_, partialTicks);

        if (entityLiving.limbSwingAmount >= 0.01D) {
            float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            GlStateManager.rotate(6.5F * f2, 0.0F, 0.0F, 1.0F);
        }
    }
}
