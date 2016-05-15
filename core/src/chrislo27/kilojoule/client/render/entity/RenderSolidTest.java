package chrislo27.kilojoule.client.render.entity;

import com.badlogic.gdx.graphics.g2d.Batch;

import chrislo27.kilojoule.client.Main;
import chrislo27.kilojoule.core.entity.EntitySolidTest;
import chrislo27.kilojoule.core.world.World;

public class RenderSolidTest extends EntityRenderer<EntitySolidTest> {

	public RenderSolidTest(EntitySolidTest entity) {
		super(entity);
	}

	@Override
	public void render(Batch batch, World world) {
		batch.setColor(Main.getRainbow(System.currentTimeMillis(), 2, 0.3f));
		Main.fillRect(batch, this.lerpPosition.x, this.lerpPosition.y,
				entity.physicsBody.bounds.width, entity.physicsBody.bounds.height);

		batch.setColor(Main.getRainbow(System.currentTimeMillis(), 0.5f, 0.5f));
		Main.drawRect(batch, this.lerpPosition.x, this.lerpPosition.y,
				entity.physicsBody.bounds.width, entity.physicsBody.bounds.height, 0.0625f);

		batch.setColor(1, 1, 1, 1);
	}

}
