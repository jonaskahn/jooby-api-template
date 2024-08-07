package io.github.jonaskahn.middlewares.context;

public class LanguageContextHolder {
    private static final ThreadLocal<String> language = new ThreadLocal<>();

    public static String currentLanguage() {
        return language.get();
    }

    public static void set(String value) {
        language.set(value);
    }
}
