package com.target.ready.library.system.repository;

import com.target.ready.library.system.entity.Inventory;

public interface InventoryRepository {
    public Inventory findByBookId(int bookId);
    public Inventory addInventory(Inventory inventory);
}
