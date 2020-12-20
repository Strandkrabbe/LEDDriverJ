package henning.leddriverj.util;

public class ColorUtils {
	
	public static int[] copy(int[] color)	{
		return color.clone();
	}
	public static int[] invert(int[] color)	{
		int[] newcolor = new int[3];
		for (int C = 0;C < 3;C++)	{
			newcolor[C] = 255 - color[C];
		}
		return newcolor;
	}
	public static int[] multiply(int[] color,float factor)	{
		color[0] = (int) (color[0] * factor);
		color[1] = (int) (color[1] * factor);
		color[2] = (int) (color[2] * factor);
		for (int C = 0;C < 3;C++)	{
			if (color[C] > 255)
				color[C] = 255;
			if (color[C] < 0)	{
				color[C] = 0;
			}
		}
		return color;
	}
	public static int[] add(int[] color,int[] add)	{
		color[0] = (int) (color[0] + add[0]);
		color[1] = (int) (color[1] + add[1]);
		color[2] = (int) (color[2] + add[2]);
		for (int C = 0;C < 3;C++)	{
			if (color[C] > 255)
				color[C] = 255;
			if (color[C] < 0)	{
				color[C] = 0;
			}
		}
		return color;
	}
	
}
