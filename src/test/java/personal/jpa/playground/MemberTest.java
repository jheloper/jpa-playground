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


    @Test
    public void persistMemberWithTransactionCommit() {

        // 트랜잭션 커밋하면 데이터베이스에 적용된다.

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Member member = new Member();

        member.setId("test1");
        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        entityManager.getTransaction().commit();

        entityManager.clear();

        Assert.assertNotNull(entityManager.find(Member.class, "test1"));
    }


    @Test
    public void updateMember() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Member member = new Member();

        member.setId("test1");
        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        member.setAge(20);

        entityManager.getTransaction().commit();

        entityManager.clear();

        final Member findMember = entityManager.find(Member.class, "test1");

        Assert.assertNotNull(findMember);
        Assert.assertEquals(Integer.valueOf(20), findMember.getAge());
    }
}
