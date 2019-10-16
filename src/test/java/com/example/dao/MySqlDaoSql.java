package com.example.dao;

import com.example.model.Book;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MySqlDaoSql {
    JdbcTemplate jdbcTemplate;

    public MySqlDaoSql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Inserts list of books into book table in database as batch
     * @param books
     * @return
     */
    public int[] batchInsert(List<Book> books) {
        final List<Book> booksToInsert = books;

        return this.jdbcTemplate.batchUpdate(
            "insert into book (ID, AUTHOR, TITLE) values(?,?,?)",
            new BatchPreparedStatementSetter() {

                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setLong(1, booksToInsert.get(i).getId());
                    ps.setString(2, booksToInsert.get(i).getAuthor());
                    ps.setString(3, booksToInsert.get(i).getTitle());
                }

                public int getBatchSize() {
                    return booksToInsert.size();
                }

            });

    }

    /**
     * Deletes all the rows in the book table
     * @return
     */
    public int truncateTable() {
        final String sql = "DELETE from book";
        int count = jdbcTemplate.update(sql);
        return count;
    }

}
