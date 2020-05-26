package com.swehd.post;

import com.swehd.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Post {

    @Id
    @GeneratedValue
    private long pid;

    @ManyToOne(optional = false)
    private User user;

    /**
     *The name of the file.
     */
    @Column(nullable = false)
    private String picture;

    /**
     * The post's description text.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Date time when the post was created.
     */
    @Column(nullable = false)
    private LocalDateTime created;

    @PrePersist
    protected void onPersist() {
        this.created = LocalDateTime.now();
    }
}
