package chrislo27.kilojoule.core.tileentity;

import chrislo27.kilojoule.core.world.World;

public interface ITileEntityProvider {

	public TileEntity createNewTileEntity(World world, int x, int y);

}
