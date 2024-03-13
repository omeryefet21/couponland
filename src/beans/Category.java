package beans;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Category {

    FOOD, ELECTRICITY, RESTAURANT, VACATION, ACTIVITY, FURNITURE;


    /**
     * Returns a set containing category names, to be added as categories during scheme creation.
     *
     * @return a set containing category names.
     */
    public static Set<String> getCategoryNames() {
        return Arrays.stream(values()).sequential().map(Category::name).reduce(new HashSet<>(), (set, name) -> {
                    set.add(name.charAt(0) + name.toLowerCase().substring(1));
                    return set;
                },
                (a, b) -> b);
    }
}
