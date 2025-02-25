package eu.kratochvil.jidlobot.parser;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>
 * AlergensParser is a component designed to parse a mask value representing
 * allergens into a set of individual allergen indices. It processes a bitmask
 * (provided as a Long) and extracts the indices of the bits that are set to 1,
 * indicating the presence of corresponding allergens.
 * </p><p>
 * The result is a sorted set of integers where each integer represents the
 * index of an allergen associated with a "1" in the provided bitmask. Indices
 * are calculated starting from 1 (for the least significant bit).
 * </p><p>
 * Key functionalities:
 * - Parse a Long allergen mask into individual allergen indices.
 * - Handle null or zero mask values gracefully by returning an empty set.
 * </p>
 */
@Component
public class AlergensParser {

    /**
     * Parses an allergen bitmask into a set of allergen indices.
     * The method processes the given bitmask, where each bit represents
     * an allergen. Indices in the resulting set start from 1 and correspond
     * to the position of the bits set to 1 in the binary representation of the mask.
     *
     * @param alergenMask the bitmask representing allergens, where each bit indicates
     *                    the presence of an allergen. A value of null or 0 will result
     *                    in an empty set being returned.
     * @return a sorted set of integers representing the indices of allergens extracted
     *         from the bitmask, starting from 1 for the least significant bit.
     */
    public Set<Integer> parse(Long alergenMask) {
        Set<Integer> result = new TreeSet<>(Comparator.naturalOrder());
        if ((alergenMask == null) || (alergenMask == 0)) {
            return result;
        }

        int index = 1;
        while (alergenMask > 0) {
            if ((alergenMask & 1) == 1) {
                result.add(index);
            }
            alergenMask >>= 1;
            index++;
        }
        return result;
    }

}
