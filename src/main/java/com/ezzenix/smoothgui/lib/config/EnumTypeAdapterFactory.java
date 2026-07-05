package com.ezzenix.smoothgui.lib.config;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class EnumTypeAdapterFactory implements TypeAdapterFactory {
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		Class<? super T> rawType = type.getRawType();

		if (!rawType.isEnum()) {
			return null;
		}

		return new TypeAdapter<T>() {
			@Override
			public void write(JsonWriter out, T value) throws IOException {
				out.value(value.toString());
			}

			@Override
			@SuppressWarnings("unchecked")
			public T read(JsonReader in) throws IOException {
				String name = in.nextString();
				T[] constants = (T[]) rawType.getEnumConstants();

				for (T constant : constants) {
					if (((Enum<?>) constant).name().equalsIgnoreCase(name)) {
						return constant;
					}
				}

				return constants[0];
			}
		};
	}
}
