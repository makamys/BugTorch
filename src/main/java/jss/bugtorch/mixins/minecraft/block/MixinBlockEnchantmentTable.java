package jss.bugtorch.mixins.minecraft.block;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;


@Mixin(value = BlockEnchantmentTable.class)
public abstract class MixinBlockEnchantmentTable extends BlockContainer {
   
	protected MixinBlockEnchantmentTable(Material material) {
		super(material);
	}

	/**
	 * @author jss2a98aj
	 * @reason Makes anything with enchantment power cause enchantment particles.
	 */
	@Overwrite()
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		//Unneeded in vanilla as the super call is empty.
		//super.randomDisplayTick(world, x, y, z, rand);
		for (int xPos = x - 2; xPos <= x + 2; ++xPos) {
			for (int zPos = z - 2; zPos <= z + 2; ++zPos) {
				if (xPos > x - 2 && xPos < x + 2 && zPos == z - 1) {
					zPos = z + 2;
				}
				if (rand.nextInt(16) == 0) {
					for (int yPos = y; yPos <= y + 1; ++yPos) {
						if (world.getBlock(xPos, yPos, zPos).getEnchantPowerBonus(world, xPos, yPos, zPos) > 0f) {
							if (!world.isAirBlock((xPos - x) / 2 + x, yPos, (zPos - z) / 2 + z)) {
								break;
							}
							world.spawnParticle("enchantmenttable", (double)x + 0.5D, (double)y + 2.0D, (double)z + 0.5D, (double)((float)(xPos - x) + rand.nextFloat()) - 0.5D, (double)((float)(yPos - y) - rand.nextFloat() - 1.0F), (double)((float)(zPos - z) + rand.nextFloat()) - 0.5D);
						}
					}
				}
			}
		}
	}

}
