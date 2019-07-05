package personal.jpa.playground;

import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProductTest {

    private static EntityManagerFactory entityManagerFactory;


    @BeforeClass
    public static void setUpClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-playground");
    }


    @Test
    public void persistProduct() {

        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Product product = new Product();

        product.setProductName("product1");
        product.setQuantity(100);
        product.setPrice(BigDecimal.valueOf(1500));

        entityManager.persist(product);

        entityManager.getTransaction().commit();

        final Product findProduct = entityManager.find(Product.class, product.getId());

        Assert.assertNotNull(findProduct);
        Assert.assertEquals(1, findProduct.getId());
    }
}
