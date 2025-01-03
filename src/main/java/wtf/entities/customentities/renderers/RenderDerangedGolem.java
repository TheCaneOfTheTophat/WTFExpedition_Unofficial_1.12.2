package wtf.entities.customentities.renderers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

public class RenderDerangedGolem extends  RenderLiving<EntityIronGolem>{

    public RenderDerangedGolem(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelIronGolem(), 0.5F);

    }
    

    private static final ResourceLocation IRON_GOLEM_TEXTURES = new ResourceLocation("textures/entity/iron_golem.png");

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(EntityIronGolem entity)
    {
        return IRON_GOLEM_TEXTURES;
    }
    
    @Override
    protected void preRenderCallback(EntityIronGolem entitylivingbaseIn, float partialTickTime)
    {
        GlStateManager.scale(0.7F, 0.7F, 0.7F);
    }

    
    @Override
	protected void applyRotations(EntityIronGolem entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
    {
        super.applyRotations(entityLiving, p_77043_2_, p_77043_3_, partialTicks);

        if (entityLiving.limbSwingAmount >= 0.01D)
        {
            float f = 13.0F;
            float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            GlStateManager.rotate(6.5F * f2, 0.0F, 0.0F, 1.0F);
        }
    }
	
}
