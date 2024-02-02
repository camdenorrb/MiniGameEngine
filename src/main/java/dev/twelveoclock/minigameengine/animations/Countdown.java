package dev.twelveoclock.minigameengine.animations;

public final class Countdown {

	private int seconds;


	public Countdown(final int seconds) {
		this.seconds = seconds;
	}


	public void start() {

	}

	public void stop() {

	}


	public int getRemainingSeconds() {
		return seconds;
	}

	public void setRemainingSeconds(final int seconds) {
		this.seconds = seconds;
	}


	public interface Presenter {
		void showRemainingTime(final Countdown countdown);

	}

}
