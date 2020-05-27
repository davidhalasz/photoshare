package com.swehd.user;

import com.swehd.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class User {
    /**
     * User's id.
     */
    @Id
    @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    /**
     * The name of the user.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The user's email.
     */
    @Column(nullable = false)
    private String email;

    /**
     * The user's password.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The user's status. If the user is logged: true.
     */
    @Column(nullable = false)
    private boolean logged;

}
