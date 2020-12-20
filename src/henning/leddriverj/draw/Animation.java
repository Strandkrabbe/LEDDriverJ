package henning.leddriverj.draw;

public interface Animation extends Drawable {
	
	// Not sure if this is a good way
	// May use state/time dependent animations instead
	
	/**
	 * Called when the animation should do a step.
	 */
	public void doAnimationStep();
	
	/**
	 * @return The step time/duration in ms.
	 */
	public long getStepTime();
	
}
