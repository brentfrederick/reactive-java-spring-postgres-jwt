package com.brentcodes.util;

import org.springframework.util.AlternativeJdkIdGenerator;

import java.util.UUID;

/**
 * Utility for generated random IDs
 */
public class RandomGenerator {
    private static final AlternativeJdkIdGenerator idGenerator = new AlternativeJdkIdGenerator();

    private RandomGenerator() {
    }

    /**
     * @see AlternativeJdkIdGenerator
     * @see UUID
     */
    public static UUID nextId() {
        return idGenerator.generateId();
    }
}