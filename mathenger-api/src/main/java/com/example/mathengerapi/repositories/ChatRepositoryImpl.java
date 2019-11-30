package com.example.mathengerapi.repositories;

import com.example.mathengerapi.models.Message;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@AllArgsConstructor
public class ChatRepositoryImpl {

    private EntityManager entityManager;

    @SuppressWarnings(value = "unchecked")
    public List<Message> findOlderMessages(LocalDateTime time, int limit, Long chatId) {
        Query query = entityManager
                .createQuery("SELECT m FROM Chat c JOIN c.messages m WHERE c.id = :chatId " +
                        "AND m.time <= :time ORDER BY m.time DESC");
        query.setParameter("chatId", chatId);
        query.setParameter("time", time);
        return query.setMaxResults(limit).getResultList();
    }
}
