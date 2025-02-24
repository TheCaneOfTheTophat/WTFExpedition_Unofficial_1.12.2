package wtf.client.renderers;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wtf.WTFExpedition;

@SideOnly(Side.CLIENT)
public class RenderGhost extends RenderBlockHead {

	public RenderGhost(RenderManager renderManager) {
		super(renderManager);
		TEXTURE = new ResourceLocation(WTFExpedition.modID, "textures/entity/transparent.png");
	}
}
