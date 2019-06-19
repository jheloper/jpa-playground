package personal.jpa.playground;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MemberTest {

    private static EntityManagerFactory entityManagerFactory;


    @BeforeClass
    public static void setUpClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-playground");
    }


    @Test
    public void persistMemberWithoutTransactionCommit() {

        // 트랜잭션 커밋이 없다면 데이터베이스에 적용되지 않는다.

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        final Member member = new Member();

        member.setId("test1");
        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        entityManager.clear();

        Assert.assertNull(entityManager.find(Member.class, "test1"));
    }
}
