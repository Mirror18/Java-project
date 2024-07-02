package com.mirror.appcontext.service;


import com.mirror.appcontext.bean.User;
import com.zaxxer.hikari.HikariDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mirror
 */
public class UserService {
    private MailService mailService;

    private HikariDataSource dataSource;

    public void setMailService(MailService mailService) {

        this.mailService = mailService;
    }
    public void setDataSource(HikariDataSource dataSource){
        this.dataSource = dataSource;
    }
    private List<User> users = new ArrayList<>(List.of( // users:
            new User(1, "bob@example.com", "password", "Bob"), // bob
            new User(2, "alice@example.com", "password", "Alice"), // alice
            new User(3, "tom@example.com", "password", "Tom"))); // tom

    public User login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                mailService.sendLoginMail(user);
                return user;
            }
        }
        throw new RuntimeException("login failed.");
    }

//    public User login(String email, String password) throws SQLException {
//        try (Connection conn = dataSource.getConnection()){
//            PreparedStatement ps = conn.prepareStatement("select * from user");
//            ResultSet rs = ps.executeQuery();
//            while(rs.next()) {
//                Long dbId = rs.getLong("id");
//                String dbEmail = rs.getString("email");
//                String dbPassword = rs.getString("password");
//                String dbName = rs.getString("name");
//                if(dbEmail.equals(email) && dbPassword.equals(password)) {
//                    return new User(dbId, dbEmail, dbPassword, dbName);
//                }
//            }
//            throw new RuntimeException("login failed.");
//        }
//    }

    public User getUser(long id) {
        return this.users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow();
    }

    public User register(String email, String password, String name) {
        users.forEach((user) -> {
            if (user.getEmail().equalsIgnoreCase(email)) {
                throw new RuntimeException("email exist.");
            }
        });
        User user = new User(users.stream().mapToLong(User::getId).max().getAsLong() + 1, email, password, name);
        users.add(user);
        mailService.sendRegistrationMail(user);
        return user;
    }
}