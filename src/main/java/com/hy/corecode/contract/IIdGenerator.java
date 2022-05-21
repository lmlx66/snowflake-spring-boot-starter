package com.hy.corecode.contract;

public interface IIdGenerator {
    long next() throws IdGeneratorException;
}
