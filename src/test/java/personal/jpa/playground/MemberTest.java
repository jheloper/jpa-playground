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

        // 엔티티를 영속 상태로 만드는 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        final Member member = new Member();

        member.setId("test1");
        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        // 트랜잭션 1 커밋
        entityManager.getTransaction().commit();

        // 엔티티의 속성 값을 수정하는 트랜잭션 2 시작
        entityManager.getTransaction().begin();

        member.setAge(20);

        // 트랜잭션 2 커밋
        entityManager.getTransaction().commit();

        // 영속성 컨텍스트 강제 초기화
        entityManager.clear();

        final Member findMember = entityManager.find(Member.class, "test1");

        Assert.assertNotNull(findMember);
        Assert.assertEquals(Integer.valueOf(20), findMember.getAge());
    }


    @Test
    public void updateMemberWithDetach() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 엔티티를 영속 상태로 만드는 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        final Member member = new Member();

        member.setId("test1");
        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        // 트랜잭션 1 커밋
        entityManager.getTransaction().commit();

        // 엔티티의 속성 값을 수정하는 트랜잭션 2 시작
        entityManager.getTransaction().begin();

        // 엔티티를 준영속 상태로 만들어버리면 속성 값이 수정되지 않는다.
        entityManager.detach(member);

        member.setAge(20);

        // 트랜잭션 2 커밋
        entityManager.getTransaction().commit();

        // 영속성 컨텍스트 강제 초기화
        entityManager.clear();

        final Member findMember = entityManager.find(Member.class, "test1");

        Assert.assertNotNull(findMember);
        Assert.assertEquals(Integer.valueOf(10), findMember.getAge());
    }


    @Test
    public void removeMember() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 엔티티를 영속 상태로 만드는 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        final Member member = new Member();

        member.setId("test1");
        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        // 트랜잭션 1 커밋
        entityManager.getTransaction().commit();

        // 영속성 컨텍스트 강제 초기화
        entityManager.clear();

        final Member findMember = entityManager.find(Member.class, "test1");

        Assert.assertNotNull(findMember);

        // 엔티티를 삭제하는 트랜잭션 2 시작
        entityManager.getTransaction().begin();

        // 엔티티를 삭제한다
        entityManager.remove(findMember);

        // 트랜잭션 2 커밋
        entityManager.getTransaction().commit();

        // 영속성 컨텍스트 강제 초기화
        entityManager.clear();

        final Member removedMember = entityManager.find(Member.class, "test1");

        Assert.assertNull(removedMember);
    }


    @Test
    public void sameMemberEntity() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Member member = new Member();

        member.setId("test1");
        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        entityManager.getTransaction().commit();

        // 영속성 컨텍스트 강제 초기화
        entityManager.clear();

        final Member findMember1 = entityManager.find(Member.class, "test1");
        final Member findMember2 = entityManager.find(Member.class, "test1");

        Assert.assertNotNull(findMember1);
        Assert.assertNotNull(findMember2);

        Assert.assertSame(findMember1, findMember2);
    }
}
