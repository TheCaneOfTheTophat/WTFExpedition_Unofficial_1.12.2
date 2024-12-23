package wtf.items;

import net.minecraft.item.Item;
import wtf.Core;

public class SimpleItem extends Item{

	public SimpleItem(String name){
		this.setCreativeTab(Core.wtfTab);
		this.setRegistryName(Core.coreID, name);
		this.setUnlocalizedName(getRegistryName().toString());
	}
}
