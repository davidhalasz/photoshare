package com.swehd.user;

import util.jpa.GenericJpaDao;

import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.Optional;

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

    /**
     * Returns a User with the specified name and password from the database.
     * @param name the name of the user.
     * @param password password of the user.
     * @return the searched user.
     */
    public Optional<User> findUser(String name, String password) {
        try {
            return Optional.ofNullable(entityManager.createQuery("SELECT u FROM User u WHERE u.name = :name AND u.password = :password", User.class)
                    .setParameter("name", name)
                    .setParameter("password", password)
                    .getSingleResult());
        } catch (NoResultException e) {
            return null;
        }
    }

}
