package chrislo27.kilojoule.core.lighting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import ionium.templates.Main;

/**
 * Converts ints back and forth between RGBLS78755 (RGB, lighting, sky-lighting).
 *
 */
public class LightUtils {

	public static final int MAX_R_VALUE = 127;
	public static final int MAX_G_VALUE = 255;
	public static final int MAX_B_VALUE = 127;
	public static final int MAX_LIGHT_VALUE = 31;
	public static final float INVERSE_LIGHT = 1f / MAX_LIGHT_VALUE;

	public static final LightingValue tmp = new LightingValue();
	public static final LightingValue tmp2 = new LightingValue();

	public static final int TOTAL_BLACK = rgblsToInt(0, 0, 0, 0, 0);
	public static final int CLEAR_WHITE = rgblsToInt(1, 1, 1, 0, 0);

	public static int rgblsToInt(float r, float g, float b, float lighting, float sky) {
		int result = (((int) (127 * r) & 127) << 25) | (((int) (255 * g) & 255) << 17)
				| (((int) (127 * b) & 127) << 10) | (((int) (31 * lighting) & 31) << 5)
				| (((int) (31 * sky) & 31));

		return result;
	}

	public static Color setColor(Color c, int rgbls, float alpha) {
		c.set(getR(rgbls), getG(rgbls), getB(rgbls), alpha);

		return c;
	}

	public static int modify(int rgbls, float rAmt, float gAmt, float bAmt, float lightAmt,
			float skyAmt) {
		return clamp(rgblsToInt(MathUtils.clamp(getR(rgbls) + rAmt, 0, 1),
				MathUtils.clamp(getG(rgbls) + gAmt, 0, 1),
				MathUtils.clamp(getB(rgbls) + bAmt, 0, 1),
				MathUtils.clamp(getLighting(rgbls) + lightAmt, 0, 1),
				MathUtils.clamp(getSky(rgbls) + skyAmt, 0, 1)));
	}

	public static int clamp(int rgbls) {
		return rgblsToInt(MathUtils.clamp(getR(rgbls), 0, 1), MathUtils.clamp(getG(rgbls), 0, 1),
				MathUtils.clamp(getB(rgbls), 0, 1), MathUtils.clamp(getLighting(rgbls), 0, 1),
				MathUtils.clamp(getSky(rgbls), 0, 1));
	}

	public static float getR(int rgbls) {
		return ((rgbls & (127 << 25)) >>> 25) / 127f;
	}

	public static float getG(int rgbls) {
		return ((rgbls & (255 << 17)) >>> 17) / 255f;
	}

	public static float getB(int rgbls) {
		return ((rgbls & (127 << 10)) >>> 10) / 127f;
	}

	public static float getLighting(int rgbls) {
		return ((rgbls & (31 << 5)) >>> 5) / 31f;
	}

	public static float getSky(int rgbls) {
		return (rgbls & 31) / 31f;
	}

	public static class LightingValue {

		public float r;
		public float g;
		public float b;
		public float lighting;
		public float sky;

		public LightingValue set(float r, float g, float b, float lighting, float sky) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.lighting = lighting;
			this.sky = sky;

			return this;
		}

		public LightingValue set(int rgbls) {
			set(LightUtils.getR(rgbls), LightUtils.getG(rgbls), LightUtils.getB(rgbls),
					LightUtils.getLighting(rgbls), LightUtils.getSky(rgbls));

			return this;
		}

		public int getRGBLS() {
			return LightUtils.rgblsToInt(r, g, b, lighting, sky);
		}

		@Override
		public String toString() {
			return "[r=" + (r + ", g=" + g + ", b=" + b + ", l=" + lighting + ", s=" + sky) + "]";
		}

	}

}
