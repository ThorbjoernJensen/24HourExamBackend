package facades;

import entities.Role;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import security.errorhandling.AuthenticationException;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    public static UserFacade getFacadeInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User createUser(User user) {
        User createdUser = user;
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Role userRole = new Role("user");
        user.addRole(userRole);
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        return createdUser;
    }

    public User getUserByUserName(String userName) {
        EntityManager em = emf.createEntityManager();
        User user;
        em.getTransaction().begin();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.userName = :userName", User.class)
                .setParameter("userName", userName);
        user = query.getSingleResult();
        em.getTransaction().commit();
        em.close();

        return user;

    }
}
