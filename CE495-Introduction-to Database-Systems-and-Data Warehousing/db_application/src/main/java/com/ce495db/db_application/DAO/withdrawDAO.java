package com.ce495db.db_application.DAO;

import com.ce495db.db_application.Domain.courseGrade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.relational.core.sql.SQL;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class withdrawDAO {

    @Autowired
    JdbcTemplate template;

    public List<courseGrade> currentEnroll(String username, int currentYear, String currentQuarter) {
        return template.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                String storedProc = "{call currentEnroll(?, ?, ?)}";
                CallableStatement cs = connection.prepareCall(storedProc);
                cs.setString(1, username);
                cs.setInt(2, currentYear);
                cs.setString(3, currentQuarter);
                return cs;
            }
        }, new CallableStatementCallback<List<courseGrade>>() {
            @Override
            public List<courseGrade> doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                ResultSet rs = callableStatement.executeQuery();
                List<courseGrade> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new courseGrade(
                            rs.getInt("studid"),
                            rs.getString("uoscode"),
                            rs.getString("uosname"),
                            rs.getString("semester"),
                            rs.getInt("year"),
                            rs.getString("grade")
                    ));
                }

                return list;
            }
        });
    }

    @Transactional
    public List<String> withdrawCourse(String username, String courseCode, String courseYear, String courseSemester) {
        return template.execute(new CallableStatementCreator() {
                @Override
                public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                    String storedProc = "{call withdrawByUpdate(?, ?, ?, ?, ?)}";
                    CallableStatement cs = connection.prepareCall(storedProc);
                    cs.setString(1, username);
                    cs.setString(2, courseCode);
                    cs.setString(3, courseYear);
                    cs.setString(4, courseSemester);
                    cs.registerOutParameter(5, Types.INTEGER);
                    return cs;
                }
            }, new CallableStatementCallback<List<String>>() {
                @Override
                public List<String> doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                    callableStatement.execute();
                    List<String> list = new ArrayList<>();
                    if ((Integer) callableStatement.getObject(5) == 1) {
                        list.add(new String("suc"));
                        list.add(new String("Enrollment goes below half of max enrollment."));
                    }
                    else list.add("suc");

                    return list;
                }
            });
    }
}
