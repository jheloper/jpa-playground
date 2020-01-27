package personal.jpa.playground;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import personal.jpa.playground.enums.TeamType;

@Entity
@Table(name = "team",
       uniqueConstraints = {@UniqueConstraint(name = "TEAM_NAME_TEAM_TYPE_UNIQUE",
                                              columnNames = {"team_name", "team_type"})})
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "team_name", nullable = false, length = 100)
    private String teamName;

    // Enum 타입 매핑
    @Column(name = "team_type")
    @Enumerated(EnumType.STRING)
    private TeamType teamType;

    // 날짜 타입 매핑
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // 날짜 타입 매핑
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // CLOB 또는 BLOB 타입 매핑
    @Lob
    private String description;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getTeamName() {
        return teamName;
    }


    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }


    public TeamType getTeamType() {
        return teamType;
    }


    public void setTeamType(TeamType teamType) {
        this.teamType = teamType;
    }


    public Date getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public Date getUpdatedAt() {
        return updatedAt;
    }


    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public List<Member> getMembers() {
        return members;
    }


    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
