package net.iceviper.flyphants.sanitypvp.inventories;

import net.iceviper.flyphants.sanitypvp.player.AbstractConfig;

public class ShopConfig extends AbstractConfig {

	public ShopConfig(String shopId) {
		super("/shops/" + shopId + ".yml");
	}
	

}
