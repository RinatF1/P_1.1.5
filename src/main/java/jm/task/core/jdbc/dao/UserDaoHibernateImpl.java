package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final SessionFactory sessionFactory = Util.getSessionFactory();
    private Transaction transaction;



    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {


        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

            session.createSQLQuery("CREATE TABLE IF NOT EXISTS users " +
                    "(id INT PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                    " name VARCHAR(45), lastName VARCHAR(45), age INT(3))").executeUpdate();
            System.out.println("Таблица создана");

            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.createSQLQuery("DROP TABLE IF EXISTS users ").executeUpdate();
            System.out.println("Таблица удалена");

            transaction.commit();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.save(new User(name, lastName, age));

            transaction.commit();

        }catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }


    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.delete(session.get(User.class, id));

            transaction.commit();
        }catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }

    @Override
    public List<User> getAllUsers() {
        List <User> userList = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            userList = session.createNativeQuery("SELECT * FROM users", User.class)
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            }

        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.createQuery("delete User ").executeUpdate();

            transaction.commit();

        }  catch (Exception e) {
            e.printStackTrace();
        }



    }
}
