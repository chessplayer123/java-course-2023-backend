package edu.java.repository.jpa.entity;

import edu.java.repository.dto.Chat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
@Table(name = "chat")
public class ChatEntity {
    @Id
    private Long id;

    @Column(name = "registered_at")
    private OffsetDateTime registrationTime;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
        name = "subscription",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "link_id")
    )
    private Set<LinkEntity> trackedLinks = new HashSet<>();

    public ChatEntity(Long id) {
        this.id = id;
        registrationTime = OffsetDateTime.now();
    }

    public void removeLink(LinkEntity link) {
        trackedLinks.remove(link);
        link.getSubscribedChats().remove(this);
    }

    public void addLink(LinkEntity link) {
        trackedLinks.add(link);
        link.getSubscribedChats().add(this);
    }

    public Chat toDto() {
        return new Chat(id, registrationTime);
    }
}
