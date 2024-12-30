package io.github.buzzxu.spuddy.dal;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;

import java.sql.SQLException;

/**
 * @author xux
 * @date 2024年12月29日 17:22:07
 */
public class SpuddyPlugin implements JdbiPlugin {


    @Override
    public void customizeJdbi(Jdbi jdbi) throws SQLException {
        JdbiPlugin.super.customizeJdbi(jdbi);
    }
}
