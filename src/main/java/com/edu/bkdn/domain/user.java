package com.edu.bkdn.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "user")
public class user implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @Column(columnDefinition = "nvarchar(100) not null")
    private String email;
    @Column(columnDefinition = "nvarchar(100) not null")
    private String phone;
    @Column(columnDefinition = "nvarchar(100) not null")
    private String password;
    @Column(columnDefinition = "nvarchar(100) not null")
    private String firstName;
    @Column(columnDefinition = "nvarchar(100) not null")
    private String surname;
    @Column(nullable = false)
    private Boolean isActive;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date createAt;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date updateAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<message> messages;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<participant> participants;
}
