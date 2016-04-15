package chrislo27.kilojoule.core.dimension;

import com.badlogic.gdx.math.Rectangle;

import chrislo27.kilojoule.core.chunk.Chunk;
import chrislo27.kilojoule.core.entity.Entity;
import ionium.util.quadtree.QuadTree;

public class Dimension {

	public final int dimWidth, dimHeight;
	public QuadTree<Entity> quadTree;

	public Dimension(int sizex, int sizey) {
		if (sizex % Chunk.SIZE != 0 || sizey % Chunk.SIZE != 0) throw new IllegalArgumentException(
				"Size parameters must evenly divide into chunk boundaries (got " + sizex + "x"
						+ sizey + ")");

		dimWidth = sizex;
		dimHeight = sizey;

		quadTree = new QuadTree<>(new Rectangle(0, 0, sizex, sizey), 0, 8, 8);
	}

}
