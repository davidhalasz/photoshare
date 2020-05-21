//package com.swehd.user;
//
//import com.swehd.post.model.Post;
//import com.swehd.user.User;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import java.util.List;
//import java.util.Optional;
//
//public class UserService {
//
//    private static Logger logger = LogManager.getLogger();
//
//    private EntityManager em;
//
//    private UserService(EntityManager em) {
//        this.em = em;
//    }
//
//
//    public User create(String name, String email, String password) {
//        return null;
//    }
//
//    public Optional<Post> find(long id) {
//        // TODO
//        return null;
//    }
//
//    public List<User> findAll() {
//        // TODO
//        return null;
//    }
//
//
//    public void delete(long id) {
//        // TODO
//    }
//
//    public void deleteAll() {
//        // TODO
//    }
//
//    public static void main(String[] args) {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("post-mysql");
//        EntityManager em = emf.createEntityManager();
//        UserService service = new UserService(em);
//
//        em.getTransaction().begin();
//        service.create( getName, "david@email.com", "pass1");
//        em.getTransaction().commit();
//
//        logger.info("post 1: {}", service.find(1));
//        logger.info("post3: {}", service.find(3));
//
//        logger.info("All post sets:");
//        service.findAll().forEach(logger::info);
//
//        em.getTransaction().begin();
//        service.delete(1);
//        em.getTransaction().commit();
//
//        logger.info("All post sets:");
//        service.findAll().forEach(logger::info);
//
//        em.getTransaction().begin();
//        service.deleteAll();
//        em.getTransaction().commit();
//
//        em.close();
//        emf.close();
//    }
//
//}
