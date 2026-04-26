package io.github.wen_wen520.magpie_bridge;

import java.nio.file.Path;

public class GeneralMessage {

	public final String app;
	public final String title;
	public final String body;
	public final Path icon;
	public final String audio;
	public final String duration;

	private GeneralMessage(Builder builder) {
		this.app = builder.app;
		this.title = builder.title;
		this.body = builder.body;
		this.icon = builder.icon;
		this.audio = builder.audio;
		this.duration = builder.duration;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String app = "Minecraft";
		private String title = "Magpie Bridge Mod";
		private String body = "Message";
		private Path icon = Main.DEFAULT_HEAD;
		private String audio = "sms";
		private String duration = "short";

		public Builder app(String app) {
			this.app = app;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder body(String body) {
			this.body = body;
			return this;
		}

		public Builder icon(Path icon) {
			this.icon = icon;
			return this;
		}

		public Builder audio(String audio) {
			this.audio = audio;
			return this;
		}

		public Builder duration(String duration) {
			this.duration = duration;
			return this;
		}

		public GeneralMessage build() {
			return new GeneralMessage(this);
		}
	}
}
