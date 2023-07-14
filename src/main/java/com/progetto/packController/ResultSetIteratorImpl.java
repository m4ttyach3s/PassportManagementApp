package com.progetto.packController;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetIteratorImpl implements ResultSetIterator {
    private ResultSet resultSet = null;

    public ResultSetIteratorImpl(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public boolean hasNext() throws SQLException {
        return resultSet.next();
    }

    public Object[] next() throws SQLException {
        // Extract the necessary data from the ResultSet
        int id = resultSet.getInt("ID");
        String cfCittadino = resultSet.getString("CFCittadino");
        String ora = resultSet.getString("ora");
        String serv = resultSet.getString("servizio");
        String data = resultSet.getString("giorno");
        String causa = resultSet.getString("causaRilascio");
        String csede = resultSet.getString("città");

        Object[] rowData = new Object[7];
        rowData[0] = id;
        rowData[1] = cfCittadino;
        rowData[2] = ora;
        rowData[3] = serv;
        rowData[4] = causa;
        rowData[5] = data;
        rowData[6] = csede;

        return rowData;
    }
}
