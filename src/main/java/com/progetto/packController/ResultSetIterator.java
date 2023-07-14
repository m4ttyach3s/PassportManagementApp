package com.progetto.packController;

import java.sql.SQLException;

public interface ResultSetIterator {
    boolean hasNext() throws SQLException;
    Object[] next() throws SQLException;
}

