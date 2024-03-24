package edu.java.repository.jpa.entity;

import edu.java.repository.dto.Link;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "link")
public class LinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String url;

    private String description;

    @Column(name = "created_at")
    private OffsetDateTime creationTime;

    @Column(name = "last_check_time")
    private OffsetDateTime lastCheckTime;

    @ManyToMany(mappedBy = "trackedLinks")
    private Set<ChatEntity> subscribedChats = new HashSet<>();

    public LinkEntity(String url, String description, OffsetDateTime creationTime, OffsetDateTime lastCheckTime) {
        this.url = url;
        this.description = description;
        this.creationTime = creationTime;
        this.lastCheckTime = lastCheckTime;
    }

    public Link toDto() {
        return new Link(id, URI.create(url), description, creationTime, lastCheckTime);
    }
}
