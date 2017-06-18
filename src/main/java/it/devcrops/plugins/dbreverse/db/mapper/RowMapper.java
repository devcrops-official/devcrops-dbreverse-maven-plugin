package it.devcrops.plugins.dbreverse.db.mapper;

import it.devcrops.plugins.dbreverse.model.OggettoDB;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by davide on 03/10/16.
 */
public interface RowMapper {
    /**
     * @param rs
     * @return OggettoDB
     * @throws SQLException
     */
    OggettoDB mapRow(ResultSet rs) throws SQLException;
}
