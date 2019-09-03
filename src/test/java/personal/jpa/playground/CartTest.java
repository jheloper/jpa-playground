package personal.jpa.playground;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CartTest {

    private static EntityManagerFactory entityManagerFactory;


    @BeforeClass
    public static void setUpClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-playground");
    }

    @Test
    public void persistManyToManyRelation() {

        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Product product1 = new Product();
        product1.setProductName("test product 1");
        product1.setPrice(BigDecimal.valueOf(1000));
        product1.setQuantity(10);

        final Product product2 = new Product();
        product2.setProductName("test product 2");
        product2.setPrice(BigDecimal.valueOf(3000));
        product2.setQuantity(20);

        final Product product3 = new Product();
        product3.setProductName("test product 3");
        product3.setPrice(BigDecimal.valueOf(1500));
        product3.setQuantity(25);

        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.persist(product3);

        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();

        final List<Product> cartProducts1 = Arrays.asList(product1, product3);
        final Cart cart1 = new Cart();
        cart1.setOrderDateTime(LocalDateTime.now());
        cart1.setProducts(cartProducts1);

        final List<Product> cartProducts2 = Arrays.asList(product1, product2);
        final Cart cart2 = new Cart();
        cart2.setOrderDateTime(LocalDateTime.now());
        cart2.setProducts(cartProducts2);

        final List<Product> cartProducts3 = Arrays.asList(product1, product2, product3);
        final Cart cart3 = new Cart();
        cart3.setOrderDateTime(LocalDateTime.now());
        cart3.setProducts(cartProducts3);

        entityManager.persist(cart1);
        entityManager.persist(cart2);
        entityManager.persist(cart3);

        entityManager.getTransaction().commit();

        entityManager.clear();

        final Cart findCart1 = entityManager.find(Cart.class, cart1.getId());
        final Cart findCart2 = entityManager.find(Cart.class, cart2.getId());
        final Cart findCart3 = entityManager.find(Cart.class, cart3.getId());

        Assert.assertTrue(isSameProducts(cart1.getProducts(), findCart1.getProducts()));
        Assert.assertTrue(isSameProducts(cart2.getProducts(), findCart2.getProducts()));
        Assert.assertTrue(isSameProducts(cart3.getProducts(), findCart3.getProducts()));
    }


    private boolean isSameProducts(final List<Product> expected, final List<Product> actual) {

        if (expected.size() != actual.size()) {
            return false;
        }

        for (int i = 0; i < expected.size(); i++) {
            if (expected.get(i).getId() != actual.get(i).getId()) {
                return false;
            }
        }

        return true;
    }
}
