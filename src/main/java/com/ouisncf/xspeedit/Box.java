package com.ouisncf.xspeedit;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to describe a box
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Box {

    /** List of articles */
    private List<Integer> items = new ArrayList<>();

    /**
     * Get the remaining free space in the box
     * @return the remaining free space in the box
     */
    public int getRemainingCapacity(){
        return Constants.BOX_CAPACITY - this.items.stream().mapToInt(i -> i).sum();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getItems().stream()
                .map(Object::toString)
                .collect(Collectors.joining(StringUtils.EMPTY));
    }
}
