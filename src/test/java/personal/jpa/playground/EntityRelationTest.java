package personal.jpa.playground;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import personal.jpa.playground.enums.TeamType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import java.util.List;

/**
 * @author joonghyeon.kim
 */
public class EntityRelationTest {

    private static EntityManagerFactory entityManagerFactory;


    @BeforeClass
    public static void setUpClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-playground");
    }


    @Test
    public void saveRelation() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Team team1 = new Team();
        team1.setTeamName("team1");
        team1.setTeamType(TeamType.A);
        entityManager.persist(team1);

        final Member member1 = new Member();
        member1.setUsername("member1");
        member1.setAge(20);
        member1.setTeam(team1);
        entityManager.persist(member1);

        final Member member2 = new Member();
        member2.setUsername("member2");
        member2.setAge(10);
        member2.setTeam(team1);
        entityManager.persist(member2);

        entityManager.getTransaction().commit();

        entityManager.clear();

        final Team findTeam1 = entityManager.find(Team.class, team1.getId());
        Assert.assertNotNull(findTeam1);
        Assert.assertEquals(2, findTeam1.getMembers().size());
    }


    @Test
    public void saveBidirectionalRelationNotOwner() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Member member1 = new Member();
        member1.setUsername("member1");
        member1.setAge(20);
        entityManager.persist(member1);

        final Member member2 = new Member();
        member2.setUsername("member2");
        member2.setAge(10);
        entityManager.persist(member2);

        final Team team1 = new Team();
        team1.setTeamName("team1");
        team1.setTeamType(TeamType.A);
        // 연관관계의 주인이 아닌 쪽에서 아래와 같이 연관관계를 저장해도 데이터베이스에는 저장되지 않는다.
        team1.setMembers(Lists.newArrayList(member1, member2));
        entityManager.persist(team1);

        entityManager.getTransaction().commit();

        entityManager.clear();

        final Team findTeam1 = entityManager.find(Team.class, team1.getId());
        Assert.assertNotNull(findTeam1);
        Assert.assertEquals(0, findTeam1.getMembers().size());
    }


    @Test
    public void retrieveRelation() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Team team1 = new Team();
        team1.setTeamName("team1");
        team1.setTeamType(TeamType.A);
        entityManager.persist(team1);

        final Member member1 = new Member();
        member1.setUsername("member1");
        member1.setAge(20);
        member1.setTeam(team1);
        entityManager.persist(member1);

        final Member member2 = new Member();
        member2.setUsername("member2");
        member2.setAge(10);
        member2.setTeam(team1);
        entityManager.persist(member2);

        entityManager.getTransaction().commit();

        entityManager.clear();

        // 객체 그래프 탐색
        final Member findMember1 = entityManager.find(Member.class, member1.getId());
        System.out.println(findMember1.getTeam().getTeamName());
        Assert.assertNotNull(findMember1.getTeam());

        final Member findMember2 = entityManager.find(Member.class, member2.getId());
        System.out.println(findMember2.getTeam().getTeamName());
        Assert.assertNotNull(findMember2.getTeam());

        entityManager.clear();

        // JPQL 사용
        final String jpql = "select m from Member m join m.team t where t.teamName=:teamName";
        final List<Member> resultList = entityManager.createQuery(jpql, Member.class)
                                                     .setParameter("teamName", team1.getTeamName())
                                                     .getResultList();
        for (final Member member : resultList) {
            System.out.println("member's username=" + member.getUsername());
        }
        Assert.assertEquals(2, resultList.size());
    }


    @Test
    public void updateRelation() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Team team1 = new Team();
        team1.setTeamName("team1");
        team1.setTeamType(TeamType.A);
        entityManager.persist(team1);

        final Member member = new Member();
        member.setUsername("member1");
        member.setAge(20);
        member.setTeam(team1);
        entityManager.persist(member);

        entityManager.getTransaction().commit();

        entityManager.clear();

        final Member findMember1 = entityManager.find(Member.class, member.getId());
        Assert.assertNotNull(findMember1);
        Assert.assertEquals(team1.getId(), findMember1.getTeam().getId());

        entityManager.getTransaction().begin();

        final Team team2 = new Team();
        team2.setTeamName("team2");
        team2.setTeamType(TeamType.B);
        entityManager.persist(team2);

        findMember1.setTeam(team2);

        entityManager.getTransaction().commit();

        entityManager.clear();

        final Member findMember2 = entityManager.find(Member.class, member.getId());
        Assert.assertNotNull(findMember2);
        Assert.assertEquals(team2.getId(), findMember2.getTeam().getId());
    }


    @Test
    public void deleteRelation() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Team team = new Team();
        team.setTeamName("team1");
        team.setTeamType(TeamType.A);
        entityManager.persist(team);

        final Member member = new Member();
        member.setUsername("member1");
        member.setAge(20);
        member.setTeam(team);
        entityManager.persist(member);

        entityManager.getTransaction().commit();

        entityManager.clear();

        final Member findMember = entityManager.find(Member.class, member.getId());
        Assert.assertNotNull(findMember);
        Assert.assertNotNull(findMember.getTeam());

        entityManager.getTransaction().begin();

        findMember.setTeam(null);

        entityManager.getTransaction().commit();

        entityManager.clear();

        final Member findMember2 = entityManager.find(Member.class, member.getId());
        Assert.assertNotNull(findMember2);
        Assert.assertNull(findMember2.getTeam());
    }


    @Test(expected = RollbackException.class)
    public void shouldDeleteAllRelationBeforeRemoveEntity() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Team team1 = new Team();
        team1.setTeamName("team1");
        team1.setTeamType(TeamType.A);
        entityManager.persist(team1);

        final Member member1 = new Member();
        member1.setUsername("member1");
        member1.setAge(20);
        member1.setTeam(team1);
        entityManager.persist(member1);

        final Member member2 = new Member();
        member2.setUsername("member2");
        member2.setAge(10);
        member2.setTeam(team1);
        entityManager.persist(member2);

        member1.setTeam(null);
        // member2.setTeam(null);
        entityManager.remove(team1);

        entityManager.getTransaction().commit();
    }
}
