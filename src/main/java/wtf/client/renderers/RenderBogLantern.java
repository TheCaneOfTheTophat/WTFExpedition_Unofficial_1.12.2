package wtf.client.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.client.models.entities.ModelInvisible;
import wtf.entities.customentities.EntityBogLantern;

@SideOnly(Side.CLIENT)
public class RenderBogLantern extends RenderLiving<EntityBogLantern> {

    public RenderBogLantern(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelInvisible(), 0.00F);
    }

    @Override
	protected ResourceLocation getEntityTexture(EntityBogLantern entity) {
        return null;
    }

    @Override
	protected void preRenderCallback(EntityBogLantern entitylivingbaseIn, float partialTickTime) {
        GlStateManager.scale(0.35F, 0.35F, 0.35F);
    }

    @Override
	protected void applyRotations(EntityBogLantern entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
        GlStateManager.translate(0.0F, MathHelper.cos(p_77043_2_ * 0.3F) * 0.1F, 0.0F);
        super.applyRotations(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
    }
    
    @Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
        if (this.renderManager.options != null)
            if (entityIn.canRenderOnFire() && (!(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).isSpectator()))
                this.renderEntityOnFire((EntityBogLantern) entityIn, x, y, z, partialTicks);
    }
    
    private void renderEntityOnFire(EntityBogLantern entity, double x, double y, double z, float partialTicks) {
        GlStateManager.disableLighting();
        TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        float f = entity.width * entity.getFlameSize();
        GlStateManager.scale(f, f, f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        float f1 = 0.5F;
        float f3 = entity.height / f;
        float f4 = (float)(entity.posY - entity.getEntityBoundingBox().minY);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, -0.3F + ((int)f3) * 0.02F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f5 = 0.0F;
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        float f6 = textureatlassprite.getMinU();
        float f7 = textureatlassprite.getMinV();
        float f8 = textureatlassprite.getMaxU();
        float f9 = textureatlassprite.getMaxV();

        float f10 = f8;
        f8 = f6;
        f6 = f10;

        vertexbuffer.pos(f1 - 0.0F, 0.0F - f4, f5).tex(f8, f9).endVertex();
        vertexbuffer.pos(-f1 - 0.0F, 0.0F - f4, f5).tex(f6, f9).endVertex();
        vertexbuffer.pos(-f1 - 0.0F, 1.4F - f4, f5).tex(f6, f7).endVertex();
        vertexbuffer.pos(f1 - 0.0F, 1.4F - f4, f5).tex(f8, f7).endVertex();

        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }
}