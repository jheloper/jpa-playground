package personal.jpa.playground;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import personal.jpa.playground.enums.TeamType;

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

        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        entityManager.clear();

        Assert.assertNull(entityManager.find(Member.class, member.getId()));
    }


    @Test
    public void persistMemberWithTransactionCommit() {

        // 트랜잭션 커밋하면 데이터베이스에 적용된다.

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Member member = new Member();

        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        entityManager.getTransaction().commit();

        entityManager.clear();

        Assert.assertNotNull(entityManager.find(Member.class, member.getId()));
    }


    @Test
    public void updateMember() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 엔티티를 영속 상태로 만드는 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        final Member member = new Member();

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

        final Member findMember = entityManager.find(Member.class, member.getId());

        Assert.assertNotNull(findMember);
        Assert.assertEquals(Integer.valueOf(20), findMember.getAge());
    }


    @Test
    public void updateMemberWithDetach() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 엔티티를 영속 상태로 만드는 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        final Member member = new Member();

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

        final Member findMember = entityManager.find(Member.class, member.getId());

        Assert.assertNotNull(findMember);
        Assert.assertEquals(Integer.valueOf(10), findMember.getAge());
    }


    @Test
    public void removeMember() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 엔티티를 영속 상태로 만드는 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        final Member member = new Member();

        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        // 트랜잭션 1 커밋
        entityManager.getTransaction().commit();

        // 영속성 컨텍스트 강제 초기화
        entityManager.clear();

        final Member findMember = entityManager.find(Member.class, member.getId());

        Assert.assertNotNull(findMember);

        // 엔티티를 삭제하는 트랜잭션 2 시작
        entityManager.getTransaction().begin();

        // 엔티티를 삭제한다
        entityManager.remove(findMember);

        // 트랜잭션 2 커밋
        entityManager.getTransaction().commit();

        // 영속성 컨텍스트 강제 초기화
        entityManager.clear();

        final Member removedMember = entityManager.find(Member.class, findMember.getId());

        Assert.assertNull(removedMember);
    }


    @Test
    public void sameMemberEntity() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Member member = new Member();

        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        entityManager.getTransaction().commit();

        // 영속성 컨텍스트 강제 초기화
        entityManager.clear();

        final Member findMember1 = entityManager.find(Member.class, member.getId());
        final Member findMember2 = entityManager.find(Member.class, member.getId());

        Assert.assertNotNull(findMember1);
        Assert.assertNotNull(findMember2);

        Assert.assertSame(findMember1, findMember2);
    }


    @Test
    public void mergeDetachedMember() {

        // 준영속 상태의 엔티티를 병합하여 영속 상태로 변경할 수 있다.

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 엔티티를 영속 상태로 만드는 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        final Member member = new Member();

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

        final Member findMember = entityManager.find(Member.class, member.getId());

        Assert.assertNotNull(findMember);
        Assert.assertEquals(Integer.valueOf(10), findMember.getAge());

        // 엔티티를 준영속 상태에서 영속 상태로 변경하고 속성 값을 수정하는 트랜잭션 3 시작
        entityManager.getTransaction().begin();

        // 병합을 통해 준영속 상태에서 영속 상태로 변경한 엔티티 반환.
        // 이 때 중요한 것은 기존의 준영속 상태의 엔티티 자체가 영속 상태로 변하는 것이 아니라,
        // 영속 상태로 바뀐 다른 엔티티 객체가 반환된다는 것.
        final Member mergedMember = entityManager.merge(member);

        mergedMember.setAge(30);

        // 트랜잭션 3 커밋
        entityManager.getTransaction().commit();

        final Member findMember2 = entityManager.find(Member.class, mergedMember.getId());

        Assert.assertNotNull(findMember2);
        Assert.assertEquals(Integer.valueOf(30), findMember2.getAge());
    }


    @Test
    public void mergeNewMember() {

        // 비영속 상태의 엔티티를 병합하여 영속 상태로 변경할 수 있다.

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 비영속 엔티티를 병합하는 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        Member member = new Member();

        member.setAge(10);
        member.setUsername("Tom");

        // 비영속 엔티티를 병합하면 엔티티를 데이터베이스에 삽입하고 영속상태의 엔티티를 반환
        member = entityManager.merge(member);

        // 트랜잭션 1 커밋
        entityManager.getTransaction().commit();

        final Member findMember = entityManager.find(Member.class, member.getId());

        Assert.assertNotNull(findMember);
        Assert.assertEquals(member, findMember);
    }


    @Test
    public void manyToOneUnidirectionalRelation() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        Member member = new Member();

        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        // 트랜잭션 1 커밋
        entityManager.getTransaction().commit();

        // 트랜잭션 2 시작
        entityManager.getTransaction().begin();

        Team team = new Team();

        team.setTeamName("team1");
        team.setTeamType(TeamType.A);
        team.setDescription("this team is first team.");

        entityManager.persist(team);

        member.setTeam(team);

        // 트랜잭션 2 커밋
        entityManager.getTransaction().commit();

        // 영속성 컨텍스트 초기화
        entityManager.clear();

        Member findMember = entityManager.find(Member.class, member.getId());

        System.out.println(findMember.getTeam().getId());
        Assert.assertEquals(team.getId(), findMember.getTeam().getId());
    }


    @Test
    public void getTeamWihJpqlManyToOneUnidirectionalRelation() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        Member member = new Member();

        member.setAge(10);
        member.setUsername("Tom");

        entityManager.persist(member);

        // 트랜잭션 1 커밋
        entityManager.getTransaction().commit();

        // 트랜잭션 2 시작
        entityManager.getTransaction().begin();

        Team team = new Team();

        team.setTeamName("team1");
        team.setTeamType(TeamType.A);
        team.setDescription("this team is first team.");

        entityManager.persist(team);

        member.setTeam(team);

        // 트랜잭션 2 커밋
        entityManager.getTransaction().commit();

        // 영속성 컨텍스트 초기화
        entityManager.clear();

        final String jpql = "SELECT m FROM Member m JOIN m.team t WHERE t.teamName = :teamName";
        List<Member> findMembers =
                entityManager.createQuery(jpql, Member.class).setParameter("teamName", "team1")
                        .getResultList();

        for(final Member findMember : findMembers) {
            System.out.println(findMember.getId());
            Assert.assertEquals(team.getId(), findMember.getTeam().getId());
        }
    }
}
