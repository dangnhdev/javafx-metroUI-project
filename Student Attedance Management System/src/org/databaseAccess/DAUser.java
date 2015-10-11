/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.databaseAccess;

import org.model.User;

import java.util.List;

/**
 * @author Admin
 */
public class DAUser {
    public List<User> getAll() {
        List<User> us;
        String sql = "SELECT * FROM user";
        us = DatabaseConnect.connectWithQuerryUser(sql);
        return us;
    }

    public void add(User user) {
        String sql = "INSERT INTO user(name,password,email,level) " +
                "VALUES('" + user.getUsername() + "','" + user.getPassword() + "','" + user.getEmail() + "'," + user.getLevel() + ")";
        DatabaseConnect.connect(sql);
    }

    public void edit(User user) {
        String sql = "UPDATE user SET password= '" + user.getPassword()
                + "',email= '" + user.getEmail() + "',level= " + user.getLevel() + " WHERE name= '" + user.getUsername() + "'";
        DatabaseConnect.connect(sql);
    }

    public void delete(String username) {
        String sql = "DELETE FROM user WHERE name= '" + username + "'";
        DatabaseConnect.connect(sql);
    }

}
