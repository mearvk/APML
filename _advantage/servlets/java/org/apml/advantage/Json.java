package org.apml.advantage;

import java.util.List;
import java.util.Map;

/** Minimal JSON writer: strings, numbers, booleans, null, List and Map. Avoids any third-party dependency. */
public final class Json {
    private Json() {}

    public static String write(Object v) {
        StringBuilder sb = new StringBuilder();
        append(sb, v);
        return sb.toString();
    }

    private static void append(StringBuilder sb, Object v) {
        if (v == null) {
            sb.append("null");
        } else if (v instanceof String s) {
            string(sb, s);
        } else if (v instanceof Number || v instanceof Boolean) {
            sb.append(v);
        } else if (v instanceof Map<?, ?> m) {
            sb.append('{');
            boolean first = true;
            for (Map.Entry<?, ?> e : m.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                string(sb, String.valueOf(e.getKey()));
                sb.append(':');
                append(sb, e.getValue());
            }
            sb.append('}');
        } else if (v instanceof List<?> l) {
            sb.append('[');
            for (int i = 0; i < l.size(); i++) {
                if (i > 0) sb.append(',');
                append(sb, l.get(i));
            }
            sb.append(']');
        } else {
            throw new IllegalArgumentException("Unsupported JSON type: " + v.getClass());
        }
    }

    private static void string(StringBuilder sb, String s) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20 || c == '<' || c == '>' || c == '&') sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
                }
            }
        }
        sb.append('"');
    }
}
