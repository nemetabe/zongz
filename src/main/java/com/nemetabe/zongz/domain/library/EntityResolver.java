package com.nemetabe.zongz.domain.library;

import java.util.Optional;

public interface EntityResolver<T extends AbstractLibraryEntity> {

    /**
     * Find an existing entity by raw name, or create a new one.
     * Never returns empty — callers don't need to handle the absent case.
     */
    T resolveOrCreate(String rawName);

    /**
     * Find only — returns empty if no match exists.
     * Use when you want to check existence without side effects.
     */
    Optional<T> resolve(String rawName);
}
