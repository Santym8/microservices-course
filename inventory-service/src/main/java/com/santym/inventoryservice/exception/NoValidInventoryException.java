package com.santym.inventoryservice.exception;

public class NoValidInventoryException extends RuntimeException{

        public NoValidInventoryException(String message) {
            super(message);
        }
}
