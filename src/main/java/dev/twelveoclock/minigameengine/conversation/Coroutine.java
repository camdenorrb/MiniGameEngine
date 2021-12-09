package dev.twelveoclock.minigameengine.conversation;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

public final class Coroutine {

	private Scope scope;


	public void launch(final Consumer<Scope> scopeConsumer) {


	}


	interface Continuation {

		void pause();

		void resume();

	}

	public static class Scope {

		private Executor executor;


		public Scope(final Executor executor) {
			this.executor = executor;
		}


		public Executor getExecutor() {
			return executor;
		}

		public void setExecutor(final Executor executor) {
			this.executor = executor;
		}

	}


	/*
	public static class Context {

		private Executor executor;


		public void thing() {
			executor.execute();
		}

	}*/



}
