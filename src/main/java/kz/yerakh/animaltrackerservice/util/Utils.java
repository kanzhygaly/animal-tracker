package kz.yerakh.animaltrackerservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static <T> boolean isDuplicate(final List<T> values) {
        return values.stream().distinct().count() != values.size();
    }
}
