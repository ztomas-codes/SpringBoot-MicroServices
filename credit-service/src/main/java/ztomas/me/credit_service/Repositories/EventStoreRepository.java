package ztomas.me.credit_service.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ztomas.me.credit_service.Entities.EventStore;

@Repository
public interface EventStoreRepository extends JpaRepository<EventStore, Integer> {
}
    
