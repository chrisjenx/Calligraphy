package com.intorias.calligraphy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.TypedValue;

public class WidgetHelper {

	private static final int FONT_SIZE_SP = 18;
	private static final int TEXT_COLOR = Color.BLACK;
	private static final boolean IS_RTL = false;
	private static final boolean DEBUG_MODE = false;

	public static Bitmap drawText(Context context, String text) {
		return drawText(context, text, FONT_SIZE_SP);
	}

	public static Bitmap drawText(Context context, String text, int fontSizeSP) {
		return drawText(context, text, fontSizeSP, TEXT_COLOR);
	}

	public static Bitmap drawText(Context context, String text, int fontSizeSP,
			int color) {
		return drawText(context, text, fontSizeSP, color, false);
	}

	public static Bitmap drawText(Context context, String text, int fontSizeSP,
			int color, boolean bold) {
		return drawText(context, text, fontSizeSP, color, bold, IS_RTL);
	}

	public static Bitmap drawText(Context context, String text, int fontSizeSP,
			int color, boolean bold, boolean isRTL) {
		
		String fontName = CalligraphyConfig.get().getFontPath();
		return drawText(context, text, fontSizeSP, color, bold, isRTL, fontName);
	}

	public static Bitmap drawText(Context context, String text, int fontSizeSP,
			int color, boolean bold, boolean isRTL, String fontName) {
		
		if (TextUtils.isEmpty(fontName)) {
			return drawText(context, text, fontSizeSP, color, bold, isRTL);
		}
		
		Typeface typeface = Typeface.createFromAsset(context.getAssets(),
				fontName);
		return drawText(context, text, fontSizeSP, color, bold, isRTL, typeface);
	}

	public static Bitmap drawText(Context context, String text, int fontSizeSP,
			int color, boolean bold, boolean isRTL, Typeface typeface) {

		int fontSizePX = convertDiptoPix(context, fontSizeSP);

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
		paint.setStyle(Style.FILL);
		paint.setTypeface(typeface);
		paint.setTextSize(fontSizePX);
		paint.setColor(color);
		paint.setFakeBoldText(bold);

		int padding = (fontSizePX / 10);
		int width = (int) (paint.measureText(text) + padding * 2);
		int height = (int) (fontSizePX + padding * 2);

		Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_4444);
		Canvas canvas = new Canvas(result);

		int theY = fontSizePX;

		if (isRTL) {
			paint.setTextAlign(Align.RIGHT);
			padding = width - padding;

			theY = (int) (theY / 1.3);
		}

		if (DEBUG_MODE) {
			canvas.drawColor(color ^ 0x00FFFFFF);
		}

		canvas.drawText(text, padding, theY, paint);

		return result;
	}

	private static int convertDiptoPix(Context context, float dip) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, context.getResources().getDisplayMetrics());
	}

}
