package com.shoppingmanager.dao.user;

import com.shoppingmanager.model.User;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(User user) {
        String query = "insert into Users (name, username, email, password, created_at, updated_at) values (?,?,?,?,NOW(),NOW())";
        try(Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public User getById(Long id) {
        String query = "select * from Users where id = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        //using RowMapper anonymous class, we can create a separate RowMapper for reuse

        return jdbcTemplate.queryForObject(query, new Object[]{id}, (rs, rowNum) -> {
            User _user = new User();
            _user.setId(rs.getLong("id"));
            _user.setName(rs.getString("name"));
            _user.setUsername(rs.getString("username"));
            _user.setEmail(rs.getString("email"));
            _user.setPassword(rs.getString("password"));
            return _user;
        });
    }

    @Override
    public void update(User user) {
//        String query = "update Employee set name=?, role=? where id=?";
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        Object[] args = new Object[] {employee.getName(), employee.getRole(), employee.getId()};
//
//        int out = jdbcTemplate.update(query, args);
//        if(out !=0){
//            System.out.println("Employee updated with id="+employee.getId());
//        }else System.out.println("No Employee found with id="+employee.getId());
    }

    @Override
    public void deleteById(Long id) {
        String query = "delete from Users where id=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM Users";
        try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setName(rs.getString("name"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
}
