package kz.yerakh.animaltrackerservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static final String AND = " AND";

    public static <T> boolean isDuplicate(final List<T> values) {
        return values.stream().distinct().count() != values.size();
    }

    public static void appendAndIfNeeded(StringBuilder where) {
        if (where.length() > 6) {
            where.append(AND);
        }
    }
}
