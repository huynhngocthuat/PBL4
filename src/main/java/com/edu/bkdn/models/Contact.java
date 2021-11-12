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
@Table(name = "contact")
public class Contact extends BaseEntity implements Serializable {

    @Column(columnDefinition = "nvarchar(100) not null")
    private String email;

    @Column(columnDefinition = "nvarchar(100) not null")
    private String firstName;

    @Column(columnDefinition = "nvarchar(100) not null")
    private String surname;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<UserContact> userContacts = new ArrayList<>();
}
