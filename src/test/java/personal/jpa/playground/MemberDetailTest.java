package personal.jpa.playground;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MemberDetailTest {

    private static EntityManagerFactory entityManagerFactory;


    @BeforeClass
    public static void setUpClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-playground");
    }


    @Test
    public void persistOneToOneRelation() {

        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Member member = new Member();
        member.setAge(25);
        member.setUsername("Tom");

        entityManager.persist(member);

        final MemberDetail memberDetail = new MemberDetail();
        memberDetail.setAddress("NewYork 123");
        memberDetail.setContactNumber("0800000000");
        member.setMemberDetail(memberDetail);

        entityManager.persist(memberDetail);

        entityManager.getTransaction().commit();

        entityManager.clear();

        final Member findMember = entityManager.find(Member.class, member.getId());

        Assert.assertEquals(findMember.getId(), member.getId());
        Assert.assertEquals(findMember.getAge(), member.getAge());
        Assert.assertEquals(findMember.getUsername(), member.getUsername());
        Assert.assertEquals(findMember.getMemberDetail().getAddress(), memberDetail.getAddress());
        Assert.assertEquals(findMember.getMemberDetail().getContactNumber(),
                            memberDetail.getContactNumber());
    }


    @Test
    public void persistOneToOneBidirectionalRelation() {

        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Member 엔티티 생성하는 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        final Member member = new Member();
        member.setUsername("test member");
        member.setAge(27);

        entityManager.persist(member);

        // 트랜잭션 1 커밋
        entityManager.getTransaction().commit();

        // MemberDetail 엔티티 생성하는 트랜잭션 2 시작
        entityManager.getTransaction().begin();

        final MemberDetail memberDetail = new MemberDetail();
        memberDetail.setAddress("address 1");
        memberDetail.setContactNumber("0700000000");

        entityManager.persist(memberDetail);

        // 트랜잭션 2 커밋
        entityManager.getTransaction().commit();

        // Member와 MemberDeatil의 일대일 양방향 관계 매핑하는 트랜잭션 3 시작
        entityManager.getTransaction().begin();

        member.setMemberDetail(memberDetail);

        // 트랜잭션 3 커밋
        entityManager.getTransaction().commit();

        // 영속성 컨텍스트 초기화
        entityManager.clear();

        final MemberDetail findMemberDetail =
                entityManager.find(MemberDetail.class, memberDetail.getId());
        Assert.assertNotNull(findMemberDetail);
        Assert.assertNotNull(findMemberDetail.getMember());
    }
}
