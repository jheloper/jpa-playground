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

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        Member member = new Member();
        member.setId("t123");
        member.setAge(25);
        member.setUsername("Tom");

        entityManager.persist(member);

        MemberDetail memberDetail = new MemberDetail();
        memberDetail.setAddress("NewYork 123");
        memberDetail.setContactNumber("0800000000");
        member.setMemberDetail(memberDetail);

        entityManager.persist(memberDetail);

        entityManager.getTransaction().commit();

        entityManager.clear();

        Member findMember = entityManager.find(Member.class, "t123");

        Assert.assertEquals(findMember.getId(), member.getId());
        Assert.assertEquals(findMember.getAge(), member.getAge());
        Assert.assertEquals(findMember.getUsername(), member.getUsername());
        Assert.assertEquals(findMember.getMemberDetail().getAddress(), memberDetail.getAddress());
        Assert.assertEquals(findMember.getMemberDetail().getContactNumber(), memberDetail.getContactNumber());
    }
}
