package chrislo27.kilojoule.core.lighting;

import ionium.templates.Main;

/**
 * Converts ints back and forth between RGBLS78755 (RGB, lighting, sky-lighting).
 *
 */
public class IntLightUtils {

	public static final LightingValue tmp = new LightingValue();
	public static final LightingValue tmp2 = new LightingValue();

	public static int rgblsToInt(float r, float g, float b, float lighting, float sky) {
		int result = (((int) (127 * r) & 127) << 25) | (((int) (255 * g) & 255) << 17)
				| (((int) (127 * b) & 127) << 10) | (((int) (31 * lighting) & 31) << 5)
				| (((int) (31 * sky) & 31));

		return result;
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
