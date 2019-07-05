package personal.jpa.playground;

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import personal.jpa.playground.enums.TeamType;

public class TeamTest {

    private static EntityManagerFactory entityManagerFactory;


    @BeforeClass
    public static void setUpClass() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa-playground");
    }


    @Test
    public void persistTeam() {

        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Team team = new Team();

        team.setTeamName("team1");
        team.setTeamType(TeamType.A);
        team.setCreatedAt(new Date());
        team.setUpdatedAt(new Date());
        team.setDescription("this is team description.");

        entityManager.persist(team);

        entityManager.getTransaction().commit();

        final Team findTeam = entityManager.find(Team.class, team.getId());

        Assert.assertNotNull(findTeam);
        Assert.assertEquals(team, findTeam);
    }


    @Test(expected = PersistenceException.class)
    public void persistTeamIfNotSetTeamName() {

        // Not Null 제약조건을 위반하는 경우

        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Team team = new Team();

        team.setTeamType(TeamType.A);
        team.setCreatedAt(new Date());
        team.setUpdatedAt(new Date());
        team.setDescription("this is team description.");

        entityManager.persist(team);

        entityManager.getTransaction().commit();

        Assert.fail("expect exception.");
    }


    @Test(expected = PersistenceException.class)
    public void persistTeamIfDuplicateUniqueConstraint() {

        // 유니크 제약조건을 위반하는 경우

        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        final Team team1 = new Team();

        team1.setTeamName("team1");
        team1.setTeamType(TeamType.A);
        team1.setCreatedAt(new Date());
        team1.setUpdatedAt(new Date());
        team1.setDescription("this is team1 description.");

        entityManager.persist(team1);

        final Team team2 = new Team();

        team2.setTeamName("team1");
        team2.setTeamType(TeamType.A);
        team2.setCreatedAt(new Date());
        team2.setUpdatedAt(new Date());
        team2.setDescription("this is team2 description.");

        entityManager.persist(team2);

        entityManager.getTransaction().commit();

        Assert.fail("expect exception.");
    }
}
