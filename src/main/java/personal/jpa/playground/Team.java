package personal.jpa.playground;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import personal.jpa.playground.enums.TeamType;

@Entity
@Table(name = "team", uniqueConstraints = {
        @UniqueConstraint(name = "TEAM_NAME_TEAM_TYPE_UNIQUE", columnNames = {"team_name",
                "team_type"})})
public class Team {

    @Id
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
}
