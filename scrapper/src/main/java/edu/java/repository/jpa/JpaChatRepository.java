package edu.java.repository.jpa;

import edu.java.repository.jpa.entity.ChatEntity;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface JpaChatRepository extends JpaRepository<ChatEntity, Long> {
}
