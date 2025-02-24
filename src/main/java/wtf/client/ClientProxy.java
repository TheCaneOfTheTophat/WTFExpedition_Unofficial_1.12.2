package wtf.client;


import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import wtf.client.models.ModelDecoration;
import wtf.client.models.ModelDenseOre;
import wtf.client.models.ModelSpeleothem;
import wtf.crafting.render.WCICTESR;
import wtf.crafting.render.WCICTileEntity;
import wtf.entities.customentities.EntityBlockHead;
import wtf.entities.customentities.EntityDerangedIronGolem;
import wtf.entities.customentities.EntityFireElemental;
import wtf.entities.customentities.EntityBogLantern;
import wtf.entities.customentities.EntityCursedArmor;
import wtf.client.renderers.RenderBlockHead;
import wtf.client.renderers.RenderDerangedIronGolem;
import wtf.client.renderers.RenderBogLantern;
import wtf.client.renderers.RenderGhost;
import wtf.CommonProxy;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInitialization() {
		ModelLoaderRegistry.registerLoader(new ModelDenseOre.Loader());
		ModelLoaderRegistry.registerLoader(new ModelSpeleothem.Loader());
		ModelLoaderRegistry.registerLoader(new ModelDecoration.Loader());

		MinecraftForge.EVENT_BUS.register(new OverlayStitchEventHandler());
	}

	@Override
	public void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityBlockHead.class, RenderBlockHead::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBogLantern.class, RenderBogLantern::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCursedArmor.class, RenderGhost::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDerangedIronGolem.class, RenderDerangedIronGolem::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFireElemental.class, RenderGhost::new);
	}

	@Override
	public void initWCICRender() {
		 ClientRegistry.bindTileEntitySpecialRenderer(WCICTileEntity.class, new WCICTESR());
	}
}
