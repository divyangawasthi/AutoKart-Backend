package com.fastenal.microservices.userms.dtos;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="wishlist")
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="created_date")
    private Date createdDate;

    private Integer pid;

    public WishList(int id, User user, Date createdDate, Integer pid) {
        this.id = id;
        this.user = user;
        this.createdDate = createdDate;
        this.pid = pid;
    }

    public WishList() {
    }

    public WishList(User user, Integer pid) {
        this.user = user;
        this.pid = pid;
        this.createdDate = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
