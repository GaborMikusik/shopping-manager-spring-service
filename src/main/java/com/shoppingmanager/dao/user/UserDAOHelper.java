package com.shoppingmanager.dao.user;

import com.shoppingmanager.dao.converter.InstantConverter;
import com.shoppingmanager.model.Role;
import com.shoppingmanager.model.RoleName;
import com.shoppingmanager.model.User;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.persistence.EntityNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class UserDAOHelper {
    public static User getUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setName(rs.getString("name"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCreatedAt(InstantConverter.convertToInstant(rs, "created_at"));
        user.setUpdatedAt(InstantConverter.convertToInstant(rs, "updated_at"));
        user.setRoles(getRoles(rs.getString("roles")));
        return user;
    }

    public static PreparedStatement getPreparedStatement(User user, Connection connection, String query) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
        ps.setString(1, user.getName());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getPassword());
        return ps;
    }

    private static Set<Role> getRoles(String roles) {
        Set<Role> rolesSet = new HashSet<>();
        if (roles != null) {
            String[] roleNames = roles.split(",");
            for (String roleName : roleNames) {
                Role role = new Role();
                role.setName(RoleName.valueOf(roleName.trim()));
                rolesSet.add(role);
            }
        }
        return rolesSet;
    }

    private static List<Long> getRoleIdForAdminRole(final JdbcTemplate jdbcTemplate, Set<Role> roles) {
        List<Long> roleIds = new LinkedList<>();
        String query = "SELECT id FROM roles WHERE name = ?";

        for (Role role : roles) {
            Long id = jdbcTemplate.queryForObject(query, Long.class, role.getName().name());
            roleIds.add(id);
        }

        return roleIds;
    }

    public static void insertRolesForUser(final JdbcTemplate jdbcTemplate, Set<Role> roles, Long generatedUserId) {
        List<Long> roleIds = getRoleIdForAdminRole(jdbcTemplate, roles);
        String roleInsertQuery = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(roleInsertQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, generatedUserId);
                ps.setLong(2, roleIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return roleIds.size();
            }
        });
    }

    public static void updateRolesForUser(final JdbcTemplate jdbcTemplate, User user) {
        String deleteUser = "DELETE FROM user_roles WHERE user_id=?";
        jdbcTemplate.update(deleteUser, user.getId());

        insertRolesForUser(jdbcTemplate, user.getRoles(), user.getId());
    }

    public static Long getGeneratedUserId(GeneratedKeyHolder generatedKeyHolder) {
        Number key = generatedKeyHolder.getKey();
        if (key == null)
            throw new EntityNotFoundException("User ID not generated");

        return key.longValue();
    }
}
