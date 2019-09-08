package personal.jpa.playground;

import javax.persistence.*;

@Entity
@Table(name = "member_detail")
public class MemberDetail {

    @Id
    @GeneratedValue
    @Column(name = "member_detail_id")
    private long id;

    @Column(name = "address")
    private String address;

    @Column(name = "contact_number")
    private String contactNumber;

    @OneToOne(mappedBy = "memberDetail")
    private Member member;


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public String getContactNumber() {
        return contactNumber;
    }


    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }


    public Member getMember() {
        return member;
    }


    public void setMember(Member member) {
        this.member = member;
    }
}
