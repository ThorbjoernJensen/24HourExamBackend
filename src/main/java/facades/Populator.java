/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Role;
import entities.User;
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
        //Create test users
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
//
////        Add owners
//        Owner o1 = new Owner("Skipper Bænt", "Persillehaven 40", "38383838");
//        Owner o2 = new Owner("Skipper Niels", "Persillehaven 42", "39393939");
//        Owner o3 = new Owner("Skipper Bente", "Persillehaven 38", "40404040");
//
//        Harbour h1 = new Harbour("Melsted Havn", "Melsted byvej", 8);
//        Harbour h2 = new Harbour("Nexø Havn", "Hovedvejen", 14);
//        Harbour h3 = new Harbour("Aakirkeby Havn", "Melsted byvej", 32);
//
//        Boat b1 = new Boat("Boatmaster", "speeder", "Martha", "https://img.fruugo.com/product/8/58/278398588_max.jpg");
//        Boat b2 = new Boat("Das Boot", "submarine", "Aase", "https://cdn.shopify.com/s/files/1/0626/0562/3537/products/31S6ddXfLmL.jpg?v=1659358008");
//        Boat b3 = new Boat("Hanger", "supersize", "King Lincoln", "https://upload.wikimedia.org/wikipedia/commons/2/2d/USS_Nimitz_%28CVN-68%29.jpg");
//
//        b1.addOwner(o1);
//        b2.addOwner(o1);
//        b2.addOwner(o2);
//        b3.addOwner(o1);
//        b3.addOwner(o3);
//
//        h1.addBoat(b1);
//        h3.addBoat(b2);
//        h3.addBoat(b3);
//
//        em.persist(o1);
//        em.persist(o2);
//        em.persist(o3);
//        em.persist(b1);
//        em.persist(b2);
//        em.persist(b3);
//        em.persist(h1);
//        em.persist(h2);
//        em.persist(h3);
//
        em.getTransaction().commit();
        System.out.println("PW: " + user.getUserPass());
        System.out.println("Testing user with OK password: " + user.verifyPassword("As123456"));
        System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
        System.out.println("Created TEST Users");

////        Create dummy -owners
////        FACADE.create(new Owner("Preben"));
////        FACADE.create(new Owner("Poul"));
        em.close();
    }

}
