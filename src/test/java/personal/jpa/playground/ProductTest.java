package personal.jpa.playground;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
}
