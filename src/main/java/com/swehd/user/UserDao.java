package com.swehd.user;

import util.jpa.GenericJpaDao;

import javax.persistence.Persistence;

public class UserDao extends GenericJpaDao<User> {

    private static UserDao instance;

    private UserDao() {
        super(User.class);
    }

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
            instance.setEntityManager(Persistence.createEntityManagerFactory("post-mysql").createEntityManager());
        }
        return instance;
    }
}
