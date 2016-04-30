package chrislo27.kilojoule.core.lighting;

import com.badlogic.gdx.math.MathUtils;

import ionium.templates.Main;

/**
 * Converts ints back and forth between RGBLS78755 (RGB, lighting, sky-lighting).
 *
 */
public class IntLightUtils {

	public static final int MAX_R_VALUE = 127;
	public static final int MAX_G_VALUE = 255;
	public static final int MAX_B_VALUE = 127;
	public static final int MAX_LIGHT_VALUE = 31;

	public static final LightingValue tmp = new LightingValue();
	public static final LightingValue tmp2 = new LightingValue();

	public static int rgblsToInt(float r, float g, float b, float lighting, float sky) {
		int result = (((int) (127 * r) & 127) << 25) | (((int) (255 * g) & 255) << 17)
				| (((int) (127 * b) & 127) << 10) | (((int) (31 * lighting) & 31) << 5)
				| (((int) (31 * sky) & 31));

		return result;
	}

	public static float modify(int rgbls, float rAmt, float gAmt, float bAmt, float lightAmt,
			float skyAmt) {
		return IntLightUtils.rgblsToInt(IntLightUtils.getR(rgbls) + rAmt,
				IntLightUtils.getG(rgbls) + gAmt, IntLightUtils.getB(rgbls) + bAmt,
				IntLightUtils.getLighting(rgbls) + lightAmt, IntLightUtils.getSky(rgbls) + skyAmt);
	}

	public static int clamp(int rgbls) {
		return IntLightUtils.rgblsToInt(MathUtils.clamp(IntLightUtils.getR(rgbls), 0, 1),
				MathUtils.clamp(IntLightUtils.getG(rgbls), 0, 1),
				MathUtils.clamp(IntLightUtils.getB(rgbls), 0, 1),
				MathUtils.clamp(IntLightUtils.getLighting(rgbls), 0, 1),
				MathUtils.clamp(IntLightUtils.getSky(rgbls), 0, 1));
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
			set(IntLightUtils.getR(rgbls), IntLightUtils.getG(rgbls), IntLightUtils.getB(rgbls),
					IntLightUtils.getLighting(rgbls), IntLightUtils.getSky(rgbls));

			return this;
		}

		public int getRGBLS() {
			return IntLightUtils.rgblsToInt(r, g, b, lighting, sky);
		}

		@Override
		public String toString() {
			return "[r=" + (r + ", g=" + g + ", b=" + b + ", l=" + lighting + ", s=" + sky) + "]";
		}

	}

}
