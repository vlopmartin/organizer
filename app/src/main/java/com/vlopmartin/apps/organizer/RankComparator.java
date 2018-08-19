package com.vlopmartin.apps.organizer;

import java.util.Comparator;

public abstract class RankComparator<T> implements Comparator<T> {
    protected abstract int getRank(T item);

    @Override
    public int compare(T o1, T o2) {
        int r1 = getRank(o1);
        int r2 = getRank(o2);
        if (r1 > r2) return 1;
        else if (r1 < r2) return -1;
        else return 0;
    }
}
