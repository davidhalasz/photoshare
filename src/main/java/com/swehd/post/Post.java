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

    @Column(nullable=false)
    private String picture;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false)
    private LocalDateTime created;

    @PrePersist
    protected void onPersist() {
        this.created = LocalDateTime.now();
    }
}
