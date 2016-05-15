package chrislo27.kilojoule.client;

import java.util.Arrays;

import aurelienribon.tweenengine.TweenAccessor;
import ionium.stage.Actor;

public class ActorAccessor<T extends Actor> implements TweenAccessor<T> {

	public static final int SCREEN_X = 0;
	public static final int SCREEN_Y = 1;
	public static final int SCREEN_W = 2;
	public static final int SCREEN_H = 3;
	public static final int PIXEL_X = 4;
	public static final int PIXEL_Y = 5;
	public static final int PIXEL_W = 6;
	public static final int PIXEL_H = 7;

	@Override
	public int getValues(T target, int type, float[] values) {
		switch (type) {

		case (SCREEN_X):
			values[0] = target.getScreenOffsetX();
			break;

		case (SCREEN_Y):
			values[0] = target.getScreenOffsetY();
			break;

		case (SCREEN_W):
			values[0] = target.getScreenOffsetWidth();
			break;

		case (SCREEN_H):
			values[0] = target.getScreenOffsetHeight();
			break;

		case (PIXEL_X):
			values[0] = target.getPixelOffsetX();
			break;

		case (PIXEL_Y):
			values[0] = target.getPixelOffsetY();
			break;

		case (PIXEL_W):
			values[0] = target.getPixelOffsetWidth();
			break;

		case (PIXEL_H):
			values[0] = target.getPixelOffsetHeight();
			break;

		default:
			return -1;

		}

		return 1;
	}

	@Override
	public void setValues(T target, int type, float[] values) {
		switch (type) {

		case (SCREEN_X):
			target.setScreenOffset(values[0], target.getScreenOffsetY());
			break;

		case (SCREEN_Y):
			target.setScreenOffset(target.getScreenOffsetX(), values[0]);
			break;

		case (SCREEN_W):
			target.setScreenOffsetSize(values[0], target.getScreenOffsetHeight());
			break;

		case (SCREEN_H):
			target.setScreenOffsetSize(target.getScreenOffsetWidth(), values[0]);
			break;

		case (PIXEL_X):
			target.setPixelOffset(values[0], target.getPixelOffsetY());
			break;

		case (PIXEL_Y):
			target.setPixelOffset(target.getPixelOffsetX(), values[0]);
			break;

		case (PIXEL_W):
			target.setPixelOffsetSize(values[0], target.getPixelOffsetHeight());
			break;

		case (PIXEL_H):
			target.setPixelOffsetSize(target.getPixelOffsetWidth(), values[0]);
			break;

		}
	}

}
