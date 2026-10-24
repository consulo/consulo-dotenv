/*
 * Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package consulo.dotenv.run;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reads the variables of a .env file: {@code KEY=value} lines with an optional {@code export} prefix, single or double quoted values
 * (double quoted ones may span several lines, kept with their line breaks) and {@code #} comments.
 */
public final class DotEnvFileParser {
    private DotEnvFileParser() {
    }

    public static Map<String, String> parse(String text) {
        Map<String, String> result = new LinkedHashMap<>();
        String currentKey = null;
        StringBuilder currentValue = null;
        for (String line : text.split("\\R", -1)) {
            if (currentKey == null) {
                int separator = line.indexOf('=');
                if (separator < 0 || line.trim().startsWith("#")) {
                    continue;
                }

                String key = removePrefix(line.substring(0, separator).trim(), "export ").trim();
                String rawValue = line.substring(separator + 1);
                if (rawValue.trim().startsWith("\"")) {
                    String value = stripComment(removePrefix(rawValue.trim(), "\""), '"');
                    if (value.endsWith("\"")) {
                        result.put(key, removeSuffix(value, "\""));
                    }
                    else {
                        currentKey = key;
                        currentValue = new StringBuilder(value);
                    }
                }
                else {
                    result.put(key, removeSurrounding(stripComment(rawValue, '\''), "'"));
                }
            }
            else {
                String value = stripComment(line, '"').trim();
                currentValue.append('\n').append(removeSuffix(value, "\""));
                if (value.endsWith("\"")) {
                    result.put(currentKey, currentValue.toString());
                    currentKey = null;
                    currentValue = null;
                }
            }
        }
        return result;
    }

    private static String stripComment(String line, char delimiter) {
        int position = line.lastIndexOf('#');
        if (position < 0 || position < line.lastIndexOf(delimiter)) {
            return line.trim();
        }
        return line.substring(0, position).trim();
    }

    private static String removePrefix(String text, String prefix) {
        return text.startsWith(prefix) ? text.substring(prefix.length()) : text;
    }

    private static String removeSuffix(String text, String suffix) {
        return text.endsWith(suffix) ? text.substring(0, text.length() - suffix.length()) : text;
    }

    private static String removeSurrounding(String text, String delimiter) {
        if (text.length() >= delimiter.length() * 2 && text.startsWith(delimiter) && text.endsWith(delimiter)) {
            return text.substring(delimiter.length(), text.length() - delimiter.length());
        }
        return text;
    }
}
