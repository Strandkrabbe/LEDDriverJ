package henning.leddriverj.draw;

public interface Drawable {

	/**
	 * This method should draw the content of the Drawable to the passed area.<br>
	 * If the method returns false the object will <b>not</b> be drawn
	 * 
	 * @param a The {@link DrawingArea} to draw on
	 * @return If the {@link Drawable} should be drawn
	 */
	public boolean draw(DrawingArea a);

	/**
	 * @return The X position relative to the next container
	 */
	public int getX();

	/**
	 * @return The Y position relative to the next container
	 */
	public int getY();

	/**
	 * <i>Note: the {@link DrawingArea} passed to the draw method has the width
	 * returned here</i>
	 * 
	 * @return The width of the element to draw
	 */
	public int getWidth();

	/**
	 * <i>Note: the {@link DrawingArea} passed to the draw method has the height
	 * returned here</i>
	 * 
	 * @return The height of the element to draw
	 */
	public int getHeight();

	/**
	 * @return The {@link ColorMode} the upper container uses to draw
	 */
	public default ColorMode getColorMode() {
		return ColorMode.REPLACE;
	}

}