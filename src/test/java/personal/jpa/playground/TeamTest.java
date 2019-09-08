package personal.jpa.playground;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import personal.jpa.playground.enums.TeamType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.Date;

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


    @Test
    public void persistManyToOneBidirectionalRelation() {

        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Team 엔티티 저장하는 트랜잭션 1 시작
        entityManager.getTransaction().begin();

        final Team team = new Team();
        team.setTeamName("test team");
        team.setTeamType(TeamType.A);
        team.setDescription("this is test team.");

        entityManager.persist(team);

        // 트랜잭션 1 커밋
        entityManager.getTransaction().commit();

        // Member 엔티티 저장하는 트랜잭션 2 시작
        entityManager.getTransaction().begin();

        final Member member1 = new Member();
        member1.setAge(25);
        member1.setUsername("test user 1");

        final Member member2 = new Member();
        member2.setAge(30);
        member2.setUsername("test user 2");

        entityManager.persist(member1);
        entityManager.persist(member2);

        // 트랜잭션 2 커밋
        entityManager.getTransaction().commit();

        // Team과 Member의 연관관계를 맺는 트랜잭션 3 시작
        entityManager.getTransaction().begin();

        member1.setTeam(team);
        member2.setTeam(team);
        team.setMembers(Arrays.asList(member1, member2));

        // 트랜잭션 3 커밋
        entityManager.getTransaction().commit();

        entityManager.clear();

        final Team findTeam = entityManager.find(Team.class, team.getId());
        Assert.assertNotNull(findTeam);
        Assert.assertEquals(2, findTeam.getMembers().size());
    }
}
