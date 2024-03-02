package edu.java.link;

import edu.java.exceptions.DifferenceIsNotSupportedException;
import jakarta.annotation.Nullable;

public interface LinkInfoSupplier {
    String getLinkSummary();

    @Nullable
    String getDifference(LinkInfoSupplier supplier) throws DifferenceIsNotSupportedException;
}
