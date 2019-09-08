package personal.jpa.playground;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Arrays;

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


    @Test
    public void persistOneToManyBidirectionalRelation() {

        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        // ProductGroup 생성하는 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        final ProductGroup productGroup = new ProductGroup();
        productGroup.setGroupName("test group 1");

        entityManager.persist(productGroup);

        // 트랜잭션 1 커밋
        entityManager.getTransaction().commit();

        // Product 생성하는 트랜잭션 2 시작
        entityManager.getTransaction().begin();

        final Product product1 = new Product();
        product1.setProductName("test product 1");
        product1.setPrice(BigDecimal.valueOf(1750));
        product1.setQuantity(30);

        final Product product2 = new Product();
        product2.setProductName("test product 2");
        product2.setPrice(BigDecimal.valueOf(2300));
        product2.setQuantity(50);

        entityManager.persist(product1);
        entityManager.persist(product2);

        // 트랜잭션 2 커밋
        entityManager.getTransaction().commit();

        // 일대다 관계를 매핑하는 트랜잭션 3 시작
        entityManager.getTransaction().begin();

        productGroup.setProducts(Arrays.asList(product1, product2));

        // 트랜잭션 3 커밋
        entityManager.getTransaction().commit();

        // 영속성 컨텍스트 초기화
        entityManager.clear();

        final Product findProduct1 = entityManager.find(Product.class, product1.getId());
        Assert.assertNotNull(findProduct1);
        Assert.assertNotNull(findProduct1.getProductGroup());

        final ProductGroup findProductGroup =
                entityManager.find(ProductGroup.class, productGroup.getId());
        Assert.assertNotNull(findProductGroup);
        Assert.assertEquals(2, findProductGroup.getProducts().size());
    }


    @Test
    public void persistManyToManyBidirectionalRelation() {

        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 트랜잭션 1 시작
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

        // 트랜잭션 1 커밋
        entityManager.getTransaction().commit();

        // 트랜잭션 2 시작
        entityManager.getTransaction().begin();

        final Cart cart1 = new Cart();
        cart1.setName("test cart 1");

        final Cart cart2 = new Cart();
        cart2.setName("test cart 2");

        final Cart cart3 = new Cart();
        cart3.setName("test cart 3");

        entityManager.persist(cart1);
        entityManager.persist(cart2);
        entityManager.persist(cart3);

        // 트랜잭션 2 커밋
        entityManager.getTransaction().commit();

        // 트랜잭션 3 시작
        entityManager.getTransaction().begin();

        cart1.setProducts(Arrays.asList(product1, product2));
        cart2.setProducts(Arrays.asList(product1, product2, product3));
        cart3.setProducts(Arrays.asList(product2, product3));

        // 트랜잭션 3 커밋
        entityManager.getTransaction().commit();

        entityManager.clear();

        final Product findProduct1 = entityManager.find(Product.class, product1.getId());
        final Product findProduct2 = entityManager.find(Product.class, product2.getId());
        final Product findProduct3 = entityManager.find(Product.class, product3.getId());

        Assert.assertNotNull(findProduct1);
        Assert.assertNotNull(findProduct2);
        Assert.assertNotNull(findProduct3);

        Assert.assertNotNull(findProduct1.getCarts());
        Assert.assertNotNull(findProduct2.getCarts());
        Assert.assertNotNull(findProduct3.getCarts());

        Assert.assertFalse(findProduct1.getCarts().isEmpty());
        Assert.assertFalse(findProduct2.getCarts().isEmpty());
        Assert.assertFalse(findProduct3.getCarts().isEmpty());

        Assert.assertEquals(2, findProduct1.getCarts().size());
        Assert.assertEquals(3, findProduct2.getCarts().size());
        Assert.assertEquals(2, findProduct3.getCarts().size());
    }
}
