package com.chess;

import java.util.List;

import com.chess.model.StorageMove;

import org.springframework.data.repository.CrudRepository;

public interface MoveRepository extends CrudRepository<StorageMove, Long> {

    List<StorageMove> findByGameIdOrderByIndex(long gameId);
    
    StorageMove findById(long id);

    void deleteByIndexAndGameId(int index, long gameId);

    int countByGameId(long gameId);

    void deleteByGameId(long gameId);

}