package images;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.Function;

public class Histograms
{
	public static int[] equalizeHistogram(int[] histogram)
	{
		int[] equalized = new int[256];
		int sum = 0;
		for (int i = 0; i < histogram.length; i++)
		{
			sum += histogram[i];
			equalized[i] = sum;
		}
		int cdfMin = equalized[0];
		for (int i = 0; i < equalized.length; i++)
		{
			equalized[i] = 255 * (equalized[i] - cdfMin) / (sum - cdfMin);
		}
		return equalized;
	}

	public static double getHistogramSimilarity(int[] histogram1, int[] histogram2)
	{
		int sumOverlayed = 0;
		int total = 0;
		int[] h1 = histogram1;
		int[] h2 = histogram2;
		if (histogram1.length > histogram2.length)
		{
			h1 = histogram2;
			h2 = histogram1;
		}
		for (int i = 0; i < h1.length; i++)
		{
			int value = Math.min(h1[i], h2[i]);
			sumOverlayed += value;
			total += Math.max(h1[i], h2[i]);
		}
		for (int i = h1.length; i < h2.length; i++)
		{
			total += h2[i];
		}
		return (double)sumOverlayed / (double)total;
	}

	public static int[] getHistogram(BufferedImage image, int x, int y, int width, int height, Function<Integer, Integer> function)
	{
		int[] histogram = new int[256];
		for (int j = 0; j < height; j++)
		{
			for (int i = 0; i < width; i++)
			{
				int rgb = image.getRGB(x + i, y + j);
				int histogramY = function.apply(rgb);
				histogram[histogramY]++;
			}
		}
		return histogram;
	}

	public static int[] getHistogram(BufferedImage image, int x, int y, Function<Integer, Integer> function)
	{
		return getHistogram(image, x, y, image.getWidth(), image.getHeight(), function);
	}

	public static int[] getHistogram(BufferedImage image, Function<Integer, Integer> function)
	{
		return getHistogram(image, 0, 0, function);
	}

	public static int[] getMostFrequentColorsByLuminance(BufferedImage image, int x, int y, int width, int height, int count)
	{
		int[] luminanceHistogram = getHistogram(image, x, y, width, height, value -> Colors.getLuminance(value));
		int[] temp = new int[luminanceHistogram.length];
		System.arraycopy(luminanceHistogram, 0, temp, 0, luminanceHistogram.length);
		Arrays.sort(temp);
		int[] mostFrequentCount = new int[Math.min(count, temp.length)];
		for (int i = 0; i < mostFrequentCount.length; i++)
		{
			mostFrequentCount[i] = temp[temp.length - i - 1];
		}
		int[] result = new int[mostFrequentCount.length];
		for (int i = 0; i < mostFrequentCount.length; i++)
		{
			for (int j = 0; j < luminanceHistogram.length; j++)
			{
				if (mostFrequentCount[i] == luminanceHistogram[j])
				{
					result[i] = j;
				}
			}
		}
		return result;
	}
}
