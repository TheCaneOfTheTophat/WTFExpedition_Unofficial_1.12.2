package wtf.client;


import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import wtf.client.models.ModelDecoration;
import wtf.client.models.ModelDenseOre;
import wtf.client.models.ModelSpeleothem;
import wtf.crafting.render.WCICTESR;
import wtf.crafting.render.WCICTileEntity;
import wtf.entities.customentities.EntityBlockHead;
import wtf.entities.customentities.EntityDerangedGolem;
import wtf.entities.customentities.EntityFireElemental;
import wtf.entities.customentities.EntityFlyingFlame;
import wtf.entities.customentities.EntityZombieGhost;
import wtf.entities.customentities.renderers.RenderBlockHead;
import wtf.entities.customentities.renderers.RenderDerangedGolem;
import wtf.entities.customentities.renderers.RenderFlyingFlame;
import wtf.entities.customentities.renderers.RenderZombieGhost;
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
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieGhost.class, RenderZombieGhost::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlyingFlame.class, RenderFlyingFlame::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDerangedGolem.class, RenderDerangedGolem::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBlockHead.class, RenderBlockHead::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFireElemental.class, RenderZombieGhost::new);
	}

	@Override
	public void initWCICRender() {
		 ClientRegistry.bindTileEntitySpecialRenderer(WCICTileEntity.class, new WCICTESR());
	}
}
