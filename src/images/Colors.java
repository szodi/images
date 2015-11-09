package images;

public class Colors
{
	public static int rgbToInt(int red, int green, int blue)
	{
		return ((red & 0xff) << 16) + ((green & 0xff) << 8) + (blue & 0xff);
	}

	public static int getLuminance(int rgb)
	{
		int red = (rgb & 0x00ff0000) >> 16;
		int green = (rgb & 0x0000ff00) >> 8;
		int blue = (rgb & 0x000000ff);
		return (red + green + blue) / 3;
	}

	public static int morph(int value1, int value2, double progress)
	{
		return (int)(value1 + (value2 - value1) * progress);
	}

	public static int getRed(int rgb)
	{
		return (rgb & 0x00ff0000) >> 16;
	}

	public static int getGreen(int rgb)
	{
		return (rgb & 0x0000ff00) >> 8;
	}

	public static int getBlue(int rgb)
	{
		return rgb & 0x000000ff;
	}

	public static int getHue(int rgb)
	{
		float hue;
		int red = getRed(rgb);
		int green = getGreen(rgb);
		int blue = getBlue(rgb);
		int cMin = getMinComponent(rgb);
		int cMax = getMaxComponent(rgb);
		if (cMax != 0)
		{
			if (red == cMax)
			{
				hue = ((float)(green - blue)) / ((float)(cMax - cMin));
			}
			else if (green == cMax)
			{
				hue = 2.0f + ((float)(blue - red)) / ((float)(cMax - cMin));
			}
			else
			{
				hue = 4.0f + ((float)(red - green)) / ((float)(cMax - cMin));
			}
			hue = hue / 6.0f;
			if (hue < 0)
			{
				hue += 1.0f;
			}
			return (int)(255 * hue);
		}
		return 0;
	}

	public static int getSaturation(int rgb)
	{
		int cMin = getMinComponent(rgb);
		int cMax = getMaxComponent(rgb);
		if (cMax == 0)
		{
			return 0;
		}
		return 255 * (cMax - cMin) / cMax;
	}

	private static int getMinComponent(int rgb)
	{
		return Math.min(Math.min(getRed(rgb), getGreen(rgb)), getBlue(rgb));
	}

	private static int getMaxComponent(int rgb)
	{
		return Math.max(Math.max(getRed(rgb), getGreen(rgb)), getBlue(rgb));
	}

	public static int rgbToLuminance(int rgb, int targetLuminance)
	{
		int red = getRed(rgb);
		int green = getGreen(rgb);
		int blue = getBlue(rgb);
		int originalLuminance = Colors.getLuminance(rgb);
		int rgbMax = getMaxComponent(rgb);
		int rgbMin = getMinComponent(rgb);
		int originalDistanceMin = originalLuminance - rgbMin;
		int originalDistanceMax = rgbMax - originalLuminance;
		if (originalDistanceMin > 0)
		{
			int newDistanceMin = targetLuminance - Math.max(rgbMin + targetLuminance - originalLuminance, 0);
			int newDistanceMax = Math.min(rgbMax + targetLuminance - originalLuminance, 255) - targetLuminance;
			double rateMin = (double)newDistanceMin / (double)originalDistanceMin;
			double rateMax = (double)newDistanceMax / (double)originalDistanceMax;
			double rate = Math.min(rateMin, rateMax);
			red = (int)((red - originalLuminance) * rate + targetLuminance);
			green = (int)((green - originalLuminance) * rate + targetLuminance);
			blue = (int)((blue - originalLuminance) * rate + targetLuminance);
			return Colors.rgbToInt(red, green, blue);
		}
		return Colors.rgbToInt(targetLuminance, targetLuminance, targetLuminance);
	}
}
