package com.energizedwork.miniprofiler;

import net.sf.log4jdbc.ConnectionSpy;
import org.springframework.jdbc.datasource.DelegatingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ProfilingDataSource extends DelegatingDataSource {

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = super.getConnection();
        return new ConnectionSpy(conn);
    }
}
