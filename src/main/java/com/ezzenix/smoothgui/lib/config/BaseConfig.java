package com.ezzenix.smoothgui.lib.config;

import com.ezzenix.smoothgui.lib.Platform;
import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseConfig {
	private static final Gson GSON = new GsonBuilder()
		.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.PRIVATE, Modifier.FINAL)
		.addSerializationExclusionStrategy(new ExclusionStrategy() {
			public boolean shouldSkipClass(Class<?> clazz) { return false; }
			public boolean shouldSkipField(FieldAttributes fieldAttributes) {
				return fieldAttributes.getAnnotation(Entry.class) == null;
			}
		})
		.registerTypeAdapterFactory(new EnumTypeAdapterFactory())
		.setPrettyPrinting().create();

	public static final List<EntryInfo> entries = new ArrayList<>();

	private static String modId;
	private static String title;
	private static File configFile;
	private static Class<? extends BaseConfig> configClass;

	public static void init(String id, Class<? extends BaseConfig> config) {
		modId = id;
		title = modId;
		configFile = new File(Platform.getConfigDirectory().toFile(), modId+".json");
		configClass = config;

		if (config.isAnnotationPresent(Config.class)) {
			if (!config.getAnnotation(Config.class).title().trim().isEmpty()) {
				title = config.getAnnotation(Config.class).title().trim();
			}
		}

		for (Field field : config.getFields()) {
			if (
				(field.isAnnotationPresent(Entry.class) || field.isAnnotationPresent(Comment.class))
				&& !field.isAnnotationPresent(Hidden.class)
			) {
				entries.add(new EntryInfo(field));
			}
		}

		load();
	}

	public static void load() {
		if (!configFile.exists()) {
			save();
			return;
		}

		try (FileReader reader = new FileReader(configFile)) {
			GSON.fromJson(reader, configClass);
		} catch (Exception e) {
			e.printStackTrace();

			File backup = new File(configFile.getPath() + ".bak");
			configFile.renameTo(backup);
			save();
		}
	}

	public static void save() {
		try (FileWriter writer = new FileWriter(configFile)) {
			Object dummyInstance = configClass.getDeclaredConstructor().newInstance();
			GSON.toJson(dummyInstance, writer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getModId() {
		return modId;
	}

	public static String getTitle() {
		return title;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Entry {
		String name() default "";
		String desc() default "";
		double min() default Double.MIN_NORMAL;
		double max() default Double.MAX_VALUE;
		int precision() default 100;
		String suffix() default "";
		boolean offText() default false;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Comment {
		String name() default "";
		boolean centered() default true;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Hidden {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Config {
		String title() default "";
	}

}
