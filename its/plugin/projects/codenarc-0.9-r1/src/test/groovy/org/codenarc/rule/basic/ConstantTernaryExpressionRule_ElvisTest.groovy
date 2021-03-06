/*
 * Copyright 2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenarc.rule.basic

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for ConstantTernaryExpressionRule handling of the "Elvis" operator.
 *
 * @see ConstantTernaryExpressionRuleTest
 *
 * @author Chris Mair
 * @version $Revision: 324 $ - $Date: 2010-04-19 00:01:21 +0400 (Пн, 19 апр 2010) $
 */
class ConstantTernaryExpressionRule_ElvisTest extends AbstractRuleTestCase {

    void testApplyTo_TrueBooleanExpression_IsAViolation() {
        final SOURCE = '''
            def x = true ?: 0
            def y
            def z = Boolean.TRUE ?: 0
        '''
        assertTwoViolations(SOURCE, 2, 'def x = true ?: 0', 4, 'def z = Boolean.TRUE ?: 0')
    }

    void testApplyTo_FalseBooleanExpression_IsAViolation() {
        final SOURCE = '''
            def x = false ?: 0
            println 'ok'
            def y = Boolean.FALSE ?: 0
        '''
        assertTwoViolations(SOURCE, 2, 'def x = false ?: 0', 4, 'def y = Boolean.FALSE ?: 0')
    }

    void testApplyTo_NullBooleanExpression_IsAViolation() {
        final SOURCE = '''
            def x = null ?: 0
        '''
        assertSingleViolation(SOURCE, 2, 'def x = null ?: 0')
    }

    void testApplyTo_LiteralStringBooleanExpression_IsAViolation() {
        final SOURCE = '''
            def x = "abc" ?: 0
            def y = "" ?: 0
        '''
        assertTwoViolations(SOURCE, 2, 'def x = "abc" ?: 0', 3, 'def y = "" ?: 0')
    }

    void testApplyTo_LiteralNumberBooleanExpression_IsAViolation() {
        final SOURCE = '''
            def x = 99.9 ?: 0
            def y = 0 ?: 0
        '''
        assertTwoViolations(SOURCE, 2, 'def x = 99.9 ?: 0', 3, 'def y = 0 ?: 0')
    }

    void testApplyTo_NoViolations() {
        final SOURCE = '''
            class MyClass {
                def x1 = z ?: 0
                def x2 = (z+2) ?: 0
                def x3 = "$abc" ?: 0
                def x4 = MAX_VALUE ?: 0
            }'''
        assertNoViolations(SOURCE)
    }

    protected Rule createRule() {
        return new ConstantTernaryExpressionRule()
    }

}