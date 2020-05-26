package com.swehd.post;

import util.jpa.GenericJpaDao;

import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

/**
 * DAO class for the {@link Post} entity.
 */
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

    /**
     * Returns the list of all post from the database.
     * @return all post.
     */
    public List<Post> findAllPost() {
        return entityManager.createQuery("SELECT p FROM Post p", Post.class)
                .getResultList();
    }

    /**
     * Returns a post with the specified id from the database.
     * @param pid the post id
     * @return the post
     */
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
