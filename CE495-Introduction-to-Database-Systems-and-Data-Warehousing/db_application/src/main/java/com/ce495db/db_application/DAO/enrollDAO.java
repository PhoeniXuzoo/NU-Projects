package com.ce495db.db_application.DAO;

import com.ce495db.db_application.Domain.courseOffering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class enrollDAO {

    @Autowired
    JdbcTemplate template;

    public List<courseOffering> getCourseOfferingById(String username, int currentYear, String currentQuarter) {
        return template.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                String storedProc = "{call courseOffering(?, ?, ?)}";
                CallableStatement cs = connection.prepareCall(storedProc);
                cs.setInt(1, Integer.parseInt(username));
                cs.setInt(2, currentYear);
                cs.setString(3, currentQuarter);
                return cs;
            }
        }, new CallableStatementCallback<List<courseOffering>>() {
            @Override
            public List<courseOffering> doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                ResultSet rs = callableStatement.executeQuery();
                List<courseOffering> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new courseOffering(rs.getString("uoscode"),
                            rs.getString("uosname"),
                            rs.getString("semester"),
                            rs.getInt("year"),
                            (Integer) rs.getObject("enrollment"),
                            (Integer) rs.getObject("maxenrollment")));
                }

                return list;
            }
        });
    }

    @Transactional
    public List<String> enrollCourse(String username, String courseCode, int courseYear, String courseSemester, int currentYear, String currentSemester, String currrentDate) {

        /*wheather course has already been in transcript.*/
        Integer isEnroll = 0;
        isEnroll = template.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                String storedProc = "{call isEnroll(?, ?, ?, ?, ?)}";
                CallableStatement cs = connection.prepareCall(storedProc);
                cs.setString(1, username);
                cs.setString(2, courseCode);
                cs.setInt(3, currentYear);
                cs.setString(4, currentSemester);
                cs.registerOutParameter(5, Types.INTEGER);
                return cs;
            }
        }, new CallableStatementCallback<Integer>() {
            @Override
            public Integer doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                return (Integer) callableStatement.getObject(5);
            }
        });

        if (isEnroll > 0) {
            List<String> result = new ArrayList<>();
            result.add(new String("in"));
            return result;
        }

        /*prerequisite of course*/
        List<String> preReqList =  template.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                String storedProc = "{call prerequisiteNum(?, ?)}";
                CallableStatement cs = connection.prepareCall(storedProc);
                cs.setString(1, courseCode);
                cs.setString(2, currrentDate);
                return cs;
            }
        }, new CallableStatementCallback<List<String>>() {
            @Override
            public List<String> doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                ResultSet rs = callableStatement.executeQuery();
                List<String> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new String(rs.getString("prerequoscode")));
                }

                return list;
            }
        });


        if (preReqList == null) {                                                                                       //don't need prerequisite
            List<String> result = new ArrayList<>();
            String sql = "{call enroll(" + username + ",'" + courseCode + "','" + courseSemester + "'," + courseYear + ")}";
            template.execute(sql);
            result.add("suc");
            return result;
        }
        else {
            List<String> hasPrereqList = template.execute(new CallableStatementCreator() {                              //which are prereqs in transcript
                @Override
                public CallableStatement createCallableStatement(Connection connection) throws SQLException {
                    String storedProc = "{call enrollCourseNeedPre(?, ?, ?, ?, ?, ?)}";
                    CallableStatement cs = connection.prepareCall(storedProc);
                    cs.setString(1, username);
                    cs.setString(2, courseCode);
                    cs.setInt(3, courseYear);
                    cs.setString(4, courseSemester);
                    cs.setString(5, currrentDate);
                    cs.registerOutParameter(6, Types.INTEGER);
                    return cs;
                }
            }, new CallableStatementCallback<List<String>>() {
                @Override
                public List<String> doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                    ResultSet rs = callableStatement.executeQuery();
                    List<String> list = new ArrayList<>();
                    if (((Integer)callableStatement.getObject(6)) == 1) {                                 // not full
                        while (rs.next())
                            list.add(new String(rs.getString("prerequoscode")));
                        return list;
                    }
                    else {                                                                                              // full
                        list.add(new String("full"));
                        return list;
                    }
                }
            });

            if (hasPrereqList.contains("full")) {
                List<String> result = new ArrayList<>();
                result.add(new String("full"));
                return result;
            }

            int isEqual = 1;
            List<String> result = new ArrayList<>();
            for (String s1 : preReqList)
                if (!hasPrereqList.contains(s1)) {
                    isEqual = 0;
                    result.add(s1);
                }

            if (isEqual == 1) {
                String sql = "{call enroll(" + username + ",'" + courseCode + "','" + courseSemester + "'," + courseYear + ")}";
                template.execute(sql);
                result.add(new String("suc"));
                return result;
            }
            else {
                return result;
            }
        }
    }
}
