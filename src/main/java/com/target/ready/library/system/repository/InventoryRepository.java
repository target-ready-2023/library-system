package com.target.ready.library.system.repository;

import com.target.ready.library.system.entity.Inventory;
import com.target.ready.library.system.exceptions.ResourceNotFoundException;

public interface InventoryRepository {
    public Inventory findByBookId(int bookId) throws ResourceNotFoundException;
    public Inventory addInventory(Inventory inventory);
}
