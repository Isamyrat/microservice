package com.example.config;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final Gson gson;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(setUpStringConverter());
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<>());
        converters.add(setUpGsonConverter());
    }

    public GsonHttpMessageConverter setUpGsonConverter() {
        GsonHttpMessageConverter gsonConv = new GsonHttpMessageConverter();
        gsonConv.setGson(gson);
        gsonConv.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        return gsonConv;
    }

    private StringHttpMessageConverter setUpStringConverter() {
        StringHttpMessageConverter stringConv = new StringHttpMessageConverter();
        stringConv.setWriteAcceptCharset(false);
        stringConv.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.ALL));
        return stringConv;
    }
}
