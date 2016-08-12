package city.smash.union.utils;

import java.awt.Color;

public class ColorUtils {
	public static Color getColorLerped(float perc, float startColor, float endColor, float stauration, float brightness, boolean inverse) {
		
		float num = 0;
		
		if (inverse) {
			// <-----
			
			float amt = 0;
			if (startColor>endColor) {
				amt = startColor-endColor;
			} else {
				amt = startColor + (360-endColor);
			}
			num = ( startColor-(perc*amt))/360;
		} else {
			// ----->

			float amt = 0;
			if (endColor>startColor) {
				amt = endColor-startColor;
			} else {
				amt = endColor + (360-startColor);
			}
			num = ( startColor+(perc*amt))/360;
		}
		
		num=num%1;
		if (num < 0) {
			num++;
		}
		
		return Color.getHSBColor(num, stauration, brightness);
	}
	
	public static float getPercentageLerped(float perc, float start, float end, boolean inverse) {
		float num = 0;
		if (inverse) {
			num = ((perc)*(start-end))+end;
		} else {
			num = ((1-perc)*(start-end))+end;
		}
		return num;
	}
}