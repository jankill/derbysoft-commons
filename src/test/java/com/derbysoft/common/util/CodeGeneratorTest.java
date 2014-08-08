package com.derbysoft.common.util;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class CodeGeneratorTest {

    @Test
    public void test() {
        Set<String> codes = new HashSet<String>();
        for (int i = 0; i < 1000000; i++) {
            assertTrue(codes.add(CodeGenerator.generate()));
        }
    }
}
