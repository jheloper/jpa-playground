package personal.jpa.playground;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EntityManagerTest {

    private static EntityManagerFactory entityManagerFactory;


    @BeforeClass
    public static void setUpClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-playground");
    }


    @Test
    public void getEntityManager() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Assert.assertNotNull(entityManager);
        Assert.assertTrue(entityManager.isOpen());
    }


    @Test
    public void getTransaction() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        Assert.assertNotNull(transaction);
        Assert.assertFalse(transaction.isActive());

        transaction.begin();

        Assert.assertTrue(transaction.isActive());

        transaction.rollback();

        Assert.assertFalse(transaction.isActive());
    }
}
