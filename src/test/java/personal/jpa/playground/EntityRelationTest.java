package personal.jpa.playground;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import personal.jpa.playground.enums.TeamType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

        final Member findMember1 = entityManager.find(Member.class, member1.getId());
        Assert.assertNotNull(findMember1.getTeam());

        final Member findMember2 = entityManager.find(Member.class, member2.getId());
        Assert.assertNotNull(findMember2.getTeam());
    }
}
