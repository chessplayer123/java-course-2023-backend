package edu.java.link;

import jakarta.annotation.Nullable;

public interface LinkInfoSupplier {
    String getLinkSummary();

    @Nullable
    String getDifference(LinkInfoSupplier supplier);
}
