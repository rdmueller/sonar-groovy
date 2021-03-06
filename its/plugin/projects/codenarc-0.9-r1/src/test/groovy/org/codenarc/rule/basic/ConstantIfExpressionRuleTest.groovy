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
 * Tests for ConstantIfExpressionRule
 *
 * @author Chris Mair
 * @version $Revision: 325 $ - $Date: 2010-04-19 05:46:07 +0400 (Пн, 19 апр 2010) $
 */
class ConstantIfExpressionRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'ConstantIfExpression'
    }

    void testApplyTo_TrueBooleanExpression_IsAViolation() {
        final SOURCE = '''
            if (true) { }
            if (Boolean.TRUE) { }
        '''
        assertTwoViolations(SOURCE, 2, 'if (true) { }', 3, 'if (Boolean.TRUE) { }')
    }

    void testApplyTo_FalseBooleanExpression_IsAViolation() {
        final SOURCE = '''
            if (false) { }
            if (Boolean.FALSE) { }
        '''
        assertTwoViolations(SOURCE, 2, 'if (false) { }', 3, 'if (Boolean.FALSE) { }')
    }

    void testApplyTo_NullBooleanExpression_IsAViolation() {
        final SOURCE = '''
            if (null) { }
        '''
        assertSingleViolation(SOURCE, 2, 'if (null) { }')
    }

    void testApplyTo_LiteralStringBooleanExpression_IsAViolation() {
        final SOURCE = '''
            if ("abc") { }
            if ("") { }
        '''
        assertTwoViolations(SOURCE, 2, 'if ("abc") { }', 3, 'if ("") { }')
    }

    void testApplyTo_LiteralNumberBooleanExpression_IsAViolation() {
        final SOURCE = '''
            if (99.9) { }
            if (0) { }
        '''
        assertTwoViolations(SOURCE, 2, 'if (99.9) { }', 3, 'if (0) { }')
    }

    void testApplyTo_NoViolations() {
        final SOURCE = '''
            if (z) { }
            if (z+2) { }
            if ("$abc") { }
            if (MAX_VALUE) { }
        '''
        assertNoViolations(SOURCE)
    }

    protected Rule createRule() {
        return new ConstantIfExpressionRule()
    }

}