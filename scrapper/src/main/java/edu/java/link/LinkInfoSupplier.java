package edu.java.link;

import jakarta.annotation.Nullable;

public interface LinkInfoSupplier<T> {
    String getLinkSummary();

    @Nullable
    String getDifference(T supplier);
}
