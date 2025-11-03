package com.chess.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chess.model.storageEntity.StorageGame;

public interface GameRepository extends MongoRepository<StorageGame, String> {
    
}