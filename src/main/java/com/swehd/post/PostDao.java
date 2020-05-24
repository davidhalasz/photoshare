package com.swehd.post;

import util.jpa.GenericJpaDao;

import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

public class PostDao extends GenericJpaDao<Post> {
    private static PostDao instance;

    private PostDao() {
        super(Post.class);
    }

    public static PostDao getInstance() {
        if (instance == null) {
            instance = new PostDao();
            instance.setEntityManager(Persistence.createEntityManagerFactory("post-mysql").createEntityManager());
        }
        return instance;
    }

    public List<Post> findAllPost() {
        return entityManager.createQuery("SELECT p FROM Post p", Post.class)
                .getResultList();
    }

    public Optional<Post> findPost(long pid) {
        try {
            return Optional.ofNullable(entityManager.createQuery("SELECT p FROM Post p WHERE p.pid = :pid", Post.class)
                    .setParameter("pid", pid)
                    .getSingleResult());
        } catch (NoResultException e) {
            return null;
        }
    }
}
