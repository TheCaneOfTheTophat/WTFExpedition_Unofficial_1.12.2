package wtf.client.renderers;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.client.models.entities.ModelBlockHead;
import wtf.entities.customentities.EntityBlockHead;

@SideOnly(Side.CLIENT)
public class RenderBlockHead extends RenderLivingBase<EntityBlockHead> {
	protected ResourceLocation TEXTURE;

	public RenderBlockHead(RenderManager renderManager) {
	     this(renderManager, false);
	}

	public RenderBlockHead(RenderManager renderManager, boolean useSmallArms) {
		super(renderManager, new ModelBlockHead(0.0F, useSmallArms), 0.5F);
		TEXTURE = new ResourceLocation("textures/entity/steve.png");

		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerArrow(this));
		this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
	}

	@Override
	public ModelPlayer getMainModel() {
	     return (ModelPlayer)super.getMainModel();
	}

	@Override
	public void doRender(EntityBlockHead entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBlockHead entity) {
		return TEXTURE;
	}

	@Override
	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	@Override
	protected void preRenderCallback(EntityBlockHead entitylivingbaseIn, float partialTickTime) {
		GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
	}

	@Override
	public void renderName(EntityBlockHead entity, double x, double y, double z) {}
}
