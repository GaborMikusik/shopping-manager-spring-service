package com.shoppingmanager.dao.user;

import com.shoppingmanager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.persistence.EntityNotFoundException;

public class UserDAOImpl implements UserDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SELECT_USER_BY_ID_QUERY = "SELECT u.id AS user_id, u.name, u.username, u.email, u.password, u.created_at, u.updated_at," +
            "GROUP_CONCAT(r.name) AS roles " +
            "FROM users u " +
            "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
            "LEFT JOIN roles r ON ur.role_id = r.id " +
            "WHERE u.id = ? " +
            "GROUP BY u.id";

    @Override
    public User save(User user) {
        final String query = "INSERT INTO users (name, username, email, password, created_at, updated_at) VALUES (?,?,?,?,NOW(),NOW())";
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        int update = jdbcTemplate.update(connection -> UserDAOHelper.getPreparedStatement(user, connection, query), generatedKeyHolder);

        if (update != 1)
            throw new EntityNotFoundException("User not saved");

        Number key = generatedKeyHolder.getKey();
        if (key == null)
            throw new EntityNotFoundException("User ID not generated");

        Long userId = UserDAOHelper.getGeneratedUserId(generatedKeyHolder);

        UserDAOHelper.insertRolesForUser(this.jdbcTemplate, user.getRoles(), userId);

        return jdbcTemplate.queryForObject(SELECT_USER_BY_ID_QUERY, (rs, rowNum) -> UserDAOHelper.getUser(rs), userId);
    }

    @Override
    public User getById(Long userId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_USER_BY_ID_QUERY, (rs, rowNum) -> UserDAOHelper.getUser(rs), userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User update(User user) {
        final String updateQuery = "UPDATE users SET name = ?, username = ?, email = ?, password = ?, updated_at = NOW() WHERE id = ?";
        int update = jdbcTemplate.update(updateQuery, user.getName(), user.getUsername(), user.getEmail(), user.getPassword(), user.getId());

        if (update == 1) {
            UserDAOHelper.updateRolesForUser(this.jdbcTemplate, user);
            return this.getById(user.getId());
        } else {
            throw new EntityNotFoundException("User with ID " + user.getId() + " not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        String deleteRoles = "DELETE FROM user_roles WHERE user_id = ?";
        jdbcTemplate.update(deleteRoles, id);
        String deleteUser = "DELETE FROM users WHERE id=?";
        jdbcTemplate.update(deleteUser, id);
    }
}
