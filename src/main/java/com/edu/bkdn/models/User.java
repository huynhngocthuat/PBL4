package com.edu.bkdn.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity implements Serializable {
    @Column(columnDefinition = "nvarchar(100) not null")
    private String email;

    @Column(columnDefinition = "nvarchar(15) not null")
    private String phone;

    @Column(columnDefinition = "nvarchar(100) not null")
    private String password;

    @Column(columnDefinition = "nvarchar(100) not null")
    private String firstName;

    @Column(columnDefinition = "nvarchar(100) not null")
    private String lastName;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(columnDefinition = "nvarchar(255) default 'https://dummyimage.com/600x600/000/fff&text=User'")
    private String urlAvatar = "https://dummyimage.com/600x600/000/fff&text=User";

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Message> messages = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Participant> participants = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<UserContact> userContacts = new ArrayList<>();
}
