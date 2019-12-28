package com.ce495db.db_application.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ce495db.db_application.Domain.student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class studentDAO {

    @Autowired
    JdbcTemplate template;


    private final static Logger logger = LoggerFactory.getLogger(studentDAO.class);

    public List<student> getAllStudents () {
        //RowMapper
        return template.query("select * from student", (rs, rowNum) -> new student(
                rs.getString("Address"),
                rs.getInt("Id"),
                rs.getString("Name"),
                rs.getString("Password")
        ));
    }

    @Transactional
    public Boolean updateAddresses(String username, String newAddress1, String newAddress2) {
        int i;

        i = template.update("update student set Address = ? where Id = ?", newAddress1, username);
        if (i != 1) return Boolean.FALSE;
        else {
            logger.info(newAddress1 + " has been inserted.");
            logger.info(newAddress2 + " starts to instert.");
                int j = template.update("update student set Address = ? where Id = ?", newAddress2, username);
                i += j;
                logger.info(newAddress2 + " fail to intsert.");
                logger.info("Rollback");
        }
        if (i != 2) return Boolean.FALSE;
        else return Boolean.TRUE;
    }

    public Boolean login(String username, String password) {
        Integer count = template.queryForObject("select count(*) from student where id = " + username + " and binary password = '" + password + "'", Integer.class);
        return (count == null || count == 0)? Boolean.FALSE : Boolean.TRUE;
    }

    public student findById(int id) {
        return template.queryForObject("select * from student where Id = ?", new Object[]{id}, new RowMapper<student>() {
            @Override
            public student mapRow(ResultSet resultSet, int i) throws SQLException {
                return new student(resultSet.getString("Address"),
                        resultSet.getInt("Id"),
                        resultSet.getString("Name"),
                        resultSet.getString("Password"));
            }
        });
    }

    @Transactional
    public Boolean updateAddress(String username, String newAddress) {
        int i;
        i = template.update("update student set Address = ? where Id = ?", newAddress, username);
        if (i == 1) return Boolean.TRUE;
        else return Boolean.FALSE;
    }

    @Transactional
    public Boolean updatePassword(String username, String newPassword) {
        int i;
        i = template.update("update student set Password = ? where Id = ?", newPassword, username);
        if (i == 1) return Boolean.TRUE;
        else return Boolean.FALSE;
    }

    /*public List<student> getAllStudents() {
        return template.query("select * from student", new RowMapper<student>() {
            @Override
            public student mapRow(ResultSet resultSet, int i) throws SQLException {
                while(resultSet.next()) {

                }
                return null;
            }
        });

    }*/
}
