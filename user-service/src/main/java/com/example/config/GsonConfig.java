package com.example.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;

@Configuration
public class GsonConfig {
    @Bean
    public Gson gson() {
        return new GsonBuilder()
            .registerTypeAdapterFactory(DateTypeAdapter.FACTORY)
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
    }

    private static final class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
            jsonWriter.value(localDate.toString());
        }

        @Override
        public LocalDate read(final JsonReader jsonReader) throws IOException {
            return LocalDate.parse(jsonReader.nextString());
        }
    }
}
