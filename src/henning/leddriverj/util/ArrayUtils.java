package henning.leddriverj.util;

public class ArrayUtils {
	
	// TODO!!! replace every miltidimensional array clone with the methods below
	// If you see one
	
	public static int[][][] copy3(int[][][] array)	{
		int[][][] n = new int[array.length][][];
		for (int Y = 0;Y < array.length;Y++)	{
			int[][] row = array[Y];
			int[][] nrow = new int[row.length][];
			for (int X = 0;X < row.length;X++)	{
				int[] color = row[X];
				int[] ncolor = new int[color.length];
				for (int C = 0;C < color.length;C++)	{
					ncolor[C] = color[C];
				}
				nrow[X] = ncolor;
			}
			n[Y] = nrow;
		}
		return n;
	}
	
	public static int[][] copy2(int[][] array)	{
		int[][] n = new int[array.length][];
		for (int Y = 0;Y < array.length;Y++)	{
			for (int X = 0;X < array[Y].length;X++)	{
				n[Y][X] = array[Y][X];
			}
		}
		return n;
	}
	
}
