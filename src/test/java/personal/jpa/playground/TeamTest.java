package personal.jpa.playground;

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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

        team.setId(1);
        team.setTeamName("team1");
        team.setTeamType(TeamType.A);
        team.setCreatedAt(new Date());
        team.setUpdatedAt(new Date());
        team.setDescription("this is team description.");

        entityManager.persist(team);

        entityManager.getTransaction().commit();

        final Team findTeam = entityManager.find(Team.class, 1);

        Assert.assertNotNull(findTeam);
        Assert.assertEquals(team, findTeam);
    }
}
