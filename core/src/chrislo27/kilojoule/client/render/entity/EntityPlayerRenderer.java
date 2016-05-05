package chrislo27.kilojoule.client.render.entity;

import com.badlogic.gdx.graphics.g2d.Batch;

import chrislo27.kilojoule.core.entity.EntityPlayer;
import chrislo27.kilojoule.core.world.World;
import ionium.templates.Main;

public class EntityPlayerRenderer extends EntityRenderer<EntityPlayer> {

	public EntityPlayerRenderer(EntityPlayer entity) {
		super(entity);
	}

	@Override
	public void render(Batch batch, World world) {
		batch.setColor(1, 1, 1, 1);
		Main.fillRect(batch, lerpPosition.x, lerpPosition.y, 1, 1);
	}

}
