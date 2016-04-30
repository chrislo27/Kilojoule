package chrislo27.kilojoule.core.lighting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import chrislo27.kilojoule.client.Main;
import chrislo27.kilojoule.client.render.WorldRenderer;
import chrislo27.kilojoule.core.world.World;

public class LightingRenderer {

	private final Color avgTmp = new Color();
	private final Color tmp1 = new Color();
	private final Color tmp2 = new Color();
	private final Color tmp3 = new Color();
	private final Color tmp4 = new Color();

	public final WorldRenderer renderer;

	public LightingRenderer(WorldRenderer r) {
		renderer = r;
	}

	public void render(Batch batch, World world, int minX, int minY, int maxX, int maxY) {
		int light;
		final boolean smoothLighting = true;
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				light = world.getLighting(x, y);

				if (smoothLighting) {
					Main.drawGradient((SpriteBatch) batch, x, y, 1, 1,
							tmp1.set(setAveragedColor(world, x, y, x - 1, y, x, y - 1, x - 1,
									y - 1)),
							tmp2.set(setAveragedColor(world, x, y, x + 1, y, x, y - 1, x + 1,
									y - 1)),
							tmp3.set(setAveragedColor(world, x, y, x + 1, y, x, y + 1, x + 1,
									y + 1)),
							tmp4.set(setAveragedColor(world, x, y, x - 1, y, x, y + 1, x - 1,
									y + 1)));
				} else {
					batch.setColor(LightUtils.getR(light), LightUtils.getG(light),
							LightUtils.getB(light),
							1f - Math.max(LightUtils.getLighting(light), LightUtils.getSky(light)));
					Main.fillRect(batch, x, y, 1, 1);
					batch.setColor(1, 1, 1, 1);
				}
			}
		}
	}

	private Color setAveragedColor(World world, int x, int y, int x2, int y2, int x3, int y3,
			int x4, int y4) {

		int total = world.getLighting(x, y);
		int total2 = world.getLighting(x2, y2);
		int total3 = world.getLighting(x3, y3);
		int total4 = world.getLighting(x4, y4);

		float r, g, b, a, l, s;
		float r2, g2, b2, a2, l2, s2;
		float r3, g3, b3, a3, l3, s3;
		float r4, g4, b4, a4, l4, s4;
		float avgL;
		float avgS;

		r = LightUtils.getR(total);
		g = LightUtils.getG(total);
		b = LightUtils.getB(total);
		a = 1f - Math.max(LightUtils.getLighting(total), LightUtils.getSky(total));
		l = LightUtils.getLighting(total);
		s = LightUtils.getSky(total);

		r2 = LightUtils.getR(total2);
		g2 = LightUtils.getG(total2);
		b2 = LightUtils.getB(total2);
		a2 = 1f - Math.max(LightUtils.getLighting(total2), LightUtils.getSky(total2));
		l2 = LightUtils.getLighting(total2);
		s2 = LightUtils.getSky(total2);

		r3 = LightUtils.getR(total3);
		g3 = LightUtils.getG(total3);
		b3 = LightUtils.getB(total3);
		a3 = 1f - Math.max(LightUtils.getLighting(total3), LightUtils.getSky(total3));
		l3 = LightUtils.getLighting(total3);
		s3 = LightUtils.getSky(total3);

		r4 = LightUtils.getR(total4);
		g4 = LightUtils.getG(total4);
		b4 = LightUtils.getB(total4);
		a4 = 1f - Math.max(LightUtils.getLighting(total4), LightUtils.getSky(total4));
		l4 = LightUtils.getLighting(total4);
		s4 = LightUtils.getSky(total4);

		avgL = (l + l2 + l3 + l4) * 0.25f;
		avgS = (s + s2 + s3 + s4) * 0.25f;

		// set to light colour
		avgTmp.set((r + r2 + r3 + r4) * 0.25f, (g + g2 + g3 + g4) * 0.25f,
				(b + b2 + b3 + b4) * 0.25f, (a + a2 + a3 + a4) * 0.25f);

		// multiply by all light tint
		avgTmp.mul(world.generatorSettings.allLightTint);

		// multiply by sky light tint
		avgTmp.set(avgTmp.r + (avgS * world.generatorSettings.skyLightTint.r),
				avgTmp.g + (avgS * world.generatorSettings.skyLightTint.g),
				avgTmp.b + (avgS * world.generatorSettings.skyLightTint.b), avgTmp.a);

		// multiply by artificial light tint
		avgTmp.set(avgTmp.r + (avgL * world.generatorSettings.artificialLightTint.r),
				avgTmp.g + (avgL * world.generatorSettings.artificialLightTint.g),
				avgTmp.b + (avgL * world.generatorSettings.artificialLightTint.b), avgTmp.a);

		// lerp with shadow colour based on opacity
		avgTmp.lerp(world.generatorSettings.shadowColor, (a + a2 + a3 + a4) * 0.25f);

		return avgTmp;
	}

}
