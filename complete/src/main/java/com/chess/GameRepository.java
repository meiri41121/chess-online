package com.chess;

import com.chess.model.*;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<StorageGame, Long> {
    
    StorageGame findById(long Id);
}