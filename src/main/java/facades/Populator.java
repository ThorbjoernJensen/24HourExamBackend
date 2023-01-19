/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author tha
 */
public class Populator {
    private static APIFacade instance;
    private static EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();

    private static final APIFacade FACADE = APIFacade.getInstance(emf);

    public static void populate() {

        EntityManager em = emf.createEntityManager();
        User user = new User("user", "As123456");
        User admin = new User("admin", "JK123456");
        User both = new User("user_admin", "DQ123456");

        em.getTransaction().begin();
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        user.addRole(userRole);
        admin.addRole(adminRole);
        both.addRole(userRole);
        both.addRole(adminRole);
        em.persist(userRole);
        em.persist(adminRole);
        em.persist(user);
        em.persist(admin);
        em.persist(both);

        Conference c1 = new Conference("conference 1", "Bornholm", 10, "20-1-23", "00:08:00");
        Conference c2 = new Conference("conference 2", "Lyngby", 10, "20-1-23", "00:09:00");
        Conference c3 = new Conference("conference 3", "Rom", 10, "20-1-23", "00:11:00");
        Speaker s1 = new Speaker("Boris", "retired", "m");
        Speaker s2 = new Speaker("Donald", "investor", "m");
        Speaker s3 = new Speaker("Beckham", "manager", "m");
        Talk t1 = new Talk("climate change on Bornholm", 120, "projector, whiteboard, sunscrean");
        Talk t2 = new Talk("Ideas for a new future", 90, "Gadgetset, waterbottles");
        Talk t3 = new Talk("Generic Ted-Talk", 20, "whiteboard");

        //       add talks to conferences
        c1.addTalk(t1);
        c3.addTalk(t2);
        c3.addTalk(t3);

// add talks to speakers
        s2.addTalk(t1);
        s2.addTalk(t2);
        s3.addTalk(t1);
        s3.addTalk(t3);

        em.persist(c1);
        em.persist(c2);
        em.persist(c3);
        em.persist(s1);
        em.persist(s2);
        em.persist(s3);
        em.persist(t1);
        em.persist(t2);
        em.persist(t3);
        em.getTransaction().commit();
        em.close();
    }

}
