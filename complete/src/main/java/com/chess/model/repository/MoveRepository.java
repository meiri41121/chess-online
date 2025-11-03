package com.chess.model.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chess.model.storageEntity.StorageMove;

public interface MoveRepository extends MongoRepository<StorageMove, String> {

    List<StorageMove> findByGameIdOrderByIndex(String gameId);
    
    void deleteByIndexAndGameId(int index, String gameId);

    int countByGameId(String gameId);

    void deleteByGameId(String gameId);

}