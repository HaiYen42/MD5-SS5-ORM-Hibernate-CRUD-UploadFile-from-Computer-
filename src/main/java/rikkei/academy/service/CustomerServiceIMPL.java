package rikkei.academy.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import rikkei.academy.model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomerServiceIMPL implements ICustomerService {
    public static SessionFactory sessionFactory;
    public static EntityManager entityManager;

    static {
        sessionFactory = new Configuration().configure("hibernate.conf.xml").buildSessionFactory();
        entityManager = sessionFactory.createEntityManager();
    }

    @Override
    public List<Customer> findAll() {
        String qrFindAll = "SELECT c from Customer AS c";
        TypedQuery<Customer> query = entityManager.createQuery(qrFindAll, Customer.class);
        return query.getResultList();
    }

    @Override
    public Customer findById(Long id) {
        String qrFindById = "select c from  Customer as c where c.id=:id";
        TypedQuery<Customer> query = entityManager.createQuery(qrFindById, Customer.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void save(Customer customer) {
        Session session = null;
        Transaction transaction = null;
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        if (customer.getId() != null) {
            Customer customer1 = findById(customer.getId());
            customer1.setName(customer.getName());
            customer1.setAvatar(customer.getAvatar());
            session.saveOrUpdate(customer1);
        } else {
            session.saveOrUpdate(customer);
        }
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteById(Long id) {
        Session session = null;
        Transaction transaction = null;
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        session.delete(findById(id));
        transaction.commit();
        session.close();
    }
}
