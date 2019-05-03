package personal.jpa.playground;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
        Assert.assertTrue(entityManager.isOpen());
    }
}
