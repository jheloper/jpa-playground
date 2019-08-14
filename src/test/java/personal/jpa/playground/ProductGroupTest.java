package personal.jpa.playground;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ProductGroupTest {

    private static EntityManagerFactory entityManagerFactory;

    
    @BeforeClass
    public static void setUpClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-playground");
    }


    @Test
    public void persistProductGroup() {

        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 엔티티 영속 상태로 만드는 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        final ProductGroup productGroup = new ProductGroup();
        productGroup.setGroupName("test group");

        entityManager.persist(productGroup);

        // 트랜잭션 1 커밋
        entityManager.getTransaction().commit();

        entityManager.clear();

        final ProductGroup findProductGroup = entityManager.find(ProductGroup.class, productGroup.getId());

        Assert.assertEquals(productGroup.getId(), findProductGroup.getId());
        Assert.assertEquals(productGroup.getGroupName(), findProductGroup.getGroupName());
    }
}
