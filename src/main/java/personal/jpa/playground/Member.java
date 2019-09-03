package personal.jpa.playground;

import javax.persistence.*;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String username;

    private Integer age;

    @ManyToOne
    // 생략해도 기본 전략에 따라 외래키 지정됨
    // @JoinColumn(name = "team_ref_id")
    private Team team;

    @OneToOne
    @JoinColumn(name = "member_detail_id")
    private MemberDetail memberDetail;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public Integer getAge() {
        return age;
    }


    public void setAge(Integer age) {
        this.age = age;
    }


    public Team getTeam() {
        return team;
    }


    public void setTeam(Team team) {
        this.team = team;
    }


    public MemberDetail getMemberDetail() {
        return memberDetail;
    }


    public void setMemberDetail(MemberDetail memberDetail) {
        this.memberDetail = memberDetail;
    }
}
