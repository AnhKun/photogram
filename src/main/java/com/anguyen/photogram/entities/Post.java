package com.anguyen.photogram.entities;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String caption;

    @ElementCollection()
    private List<String> imageName;

    private Instant date;
    private Boolean status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comment;
}
