package edu.java.scrapper;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import static org.assertj.core.api.Assertions.assertThat;

public class MigrationTest extends IntegrationEnvironment {
    @Test
    @SneakyThrows
    public void chatTableWasSuccessfullyCreated() {
        Connection con = POSTGRES.createConnection("");
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM chat;");
        ResultSetMetaData metadata = stmt
            .executeQuery()
            .getMetaData();

        assertThat(metadata.getColumnCount()).isEqualTo(2);
        assertThat(metadata.getColumnName(1)).isEqualTo("id");
        assertThat(metadata.getColumnName(2)).isEqualTo("registered_at");
    }

    @Test
    @SneakyThrows
    public void subscriptionTableWasSuccessfullyCreated() {
        Connection con = POSTGRES.createConnection("");
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM subscription;");
        ResultSetMetaData metadata = stmt
            .executeQuery()
            .getMetaData();

        assertThat(metadata.getColumnCount()).isEqualTo(2);
        assertThat(metadata.getColumnName(1)).isEqualTo("chat_id");
        assertThat(metadata.getColumnName(2)).isEqualTo("link_id");
    }

    @Test
    @SneakyThrows
    public void linkTableWasSuccessfullyCreated() {
        Connection con = POSTGRES.createConnection("");
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM link;");
        ResultSetMetaData metadata = stmt
            .executeQuery()
            .getMetaData();

        assertThat(metadata.getColumnCount()).isEqualTo(5);
        assertThat(metadata.getColumnName(1)).isEqualTo("id");
        assertThat(metadata.getColumnName(2)).isEqualTo("url");
        assertThat(metadata.getColumnName(3)).isEqualTo("data");
        assertThat(metadata.getColumnName(4)).isEqualTo("created_at");
        assertThat(metadata.getColumnName(5)).isEqualTo("last_check_time");
    }
}
