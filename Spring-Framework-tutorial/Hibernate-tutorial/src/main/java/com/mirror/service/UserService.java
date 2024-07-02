package com.mirror.service;

import com.mirror.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author mirror
 */
@Component
@Transactional
public class UserService {

//    @Autowired
//    SessionFactory sessionFactory;

    @PersistenceContext
    EntityManager em;
    public User fetchUserById(long id) {

//        return sessionFactory.getCurrentSession().byId(User.class).load(id);

        return em.find(User.class,id);
    }

    public User getUserById(long id) {
        User user = fetchUserById(id);
        if (user == null) {
            throw new RuntimeException("user not found by id: " + id);
        }
        return user;
    }

    public User fetchUserByEmail(String email) {
        User example = new User();
        example.setEmail(email);
//        List<User> list = sessionFactory.getCurrentSession()
//                .createQuery("from User u where u.email = ?1", User.class).
//                setParameter(1, email)
//                .list();
        // JPQL查询:
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :e", User.class);
        query.setParameter("e", email);
        List<User> list = query.getResultList();

        return list.isEmpty() ? null : list.get(0);
    }

    public User getUserByEmail(String email) {
        User user = fetchUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("user not found by email: " + email);
        }
        return user;
    }

    public List<User> getUsers(int pageIndex) {
        int pageSize = 100;
//        return sessionFactory.getCurrentSession().
//                createQuery("from User u", User.class).
//                setFirstResult((pageIndex - 1) * pageSize)
//                .setMaxResults(pageSize)
//                .list();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        query.setFirstResult((pageIndex - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    public User signin(String email, String password) {
//        List<User> list = sessionFactory.getCurrentSession()
//                .createQuery("from User u where u.email = ?1 and u.password = ?2", User.class)
//                .setParameter(1, email)
//                .setParameter(2, password).list();
        TypedQuery<User> query = em
                .createQuery("select u from User u where u.email = ?1 and u.password = ?2", User.class)
                .setParameter(1, email)
                .setParameter(2, password);
        List<User> list=query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public User login(String email, String password) {
//        List<User> list = sessionFactory.getCurrentSession().createNamedQuery("login", User.class) // named query
//                .setParameter("e", email).setParameter("pwd", password).list();

        TypedQuery<User> query = em.createNamedQuery("login", User.class);
        query.setParameter("e", email);
        query.setParameter("pwd", password);
        List<User> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public User register(String email, String password, String name) {
        //创建对象
        User user = new User();
        //设置属性
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
//        保存到数据库
//        sessionFactory.getCurrentSession().persist(user);
        em.persist(user);
        System.out.println(user.getId());
        return user;
    }

    public void updateUser(Long id, String name) {
        User user = getUserById(id);
        user.setName(name);
//        sessionFactory.getCurrentSession().merge(user);
        em.refresh(user);
    }

    public boolean deleteUser(Long id) {
        User user = fetchUserById(id);
        if (user != null) {
//            sessionFactory.getCurrentSession().remove(user);

            em.remove(user);
            return true;
        }
        return false;
    }
}
