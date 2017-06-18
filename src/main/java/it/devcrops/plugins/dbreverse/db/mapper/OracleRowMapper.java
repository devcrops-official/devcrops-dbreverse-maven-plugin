package it.devcrops.plugins.dbreverse.db.mapper;

import it.devcrops.plugins.dbreverse.model.OggettoDB;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by davide on 03/10/16.
 */
public class OracleRowMapper implements RowMapper {

    /**
     * @param rs
     * @return OggettoDb popolato con il contenuto del Result Set
     * @throws SQLException
     */
    @Override
    public OggettoDB mapRow(ResultSet rs) throws SQLException {
        OggettoDB oggettoDB = new OggettoDB();
        oggettoDB.setNome(rs.getString("object_name"));
        oggettoDB.setTipo(rs.getString("object_type"));
        oggettoDB.setDdl(rs.getString("object_ddl"));
        return oggettoDB;
    }

}
