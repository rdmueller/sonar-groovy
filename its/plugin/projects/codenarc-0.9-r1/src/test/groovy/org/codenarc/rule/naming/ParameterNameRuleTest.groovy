/*
 * Copyright 2009 the original author or authors.
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
package org.codenarc.rule.naming

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for ParameterNameRule
 *
 * @author Chris Mair
 * @version $Revision: 292 $ - $Date: 2010-01-17 06:09:23 +0300 (Вс, 17 янв 2010) $
 */
class ParameterNameRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'ParameterName'
    }

    void testRegex_DefaultValue() {
        assert 'abc' ==~ rule.regex
        assert 'aXaX123' ==~ rule.regex

        assert !('abc_def' ==~ rule.regex)
        assert !('ABC123abc' ==~ rule.regex)
    }

    void testRegexIsNull() {
        rule.regex = null
        shouldFailWithMessageContaining('regex') { applyRuleTo('def myMethod(int count) { }') }
    }

    void testApplyTo_DoesNotMatchDefaultRegex() {
        final SOURCE = '''
            class MyClass {
                def myMethod(BigDecimal deposit_amount) { }
            }
        '''
        assertSingleViolation(SOURCE, 3, 'BigDecimal deposit_amount')
    }

    void testApplyTo_MatchesDefaultRegex() {
        final SOURCE = '''
            class MyClass {
                def myMethod(BigDecimal depositAmount) { }
            }
        '''
        assertNoViolations(SOURCE)
    }

    void testApplyTo_DoesNotMatchCustomRegex() {
        final SOURCE = '''
            class MyClass {
                def myMethod(int count) { }
            }
        '''
        rule.regex = /z.*/
        assertSingleViolation(SOURCE, 3, 'int count')
    }

    void testApplyTo_MatchesCustomRegex() {
        final SOURCE = '''
            class MyClass {
                def myMethod(int _Count = 23) { }
            }
        '''
        rule.regex = /_.*/
        assertNoViolations(SOURCE)
    }

    void testApplyTo_Final_DoesNotMatchDefaultRegex() {
        final SOURCE = '''
            class MyClass {
                def myMethod(final BigDecimal deposit_amount) { }
            }
        '''
        assertSingleViolation(SOURCE, 3, 'final BigDecimal deposit_amount')
    }

    void testApplyTo_Constructor_MatchesDefaultRegex() {
        final SOURCE = '''
            class MyClass {
                MyClass(BigDecimal depositAmount) { }
            }
        '''
        assertNoViolations(SOURCE)
    }

    void testApplyTo_Constructor_DoesNotMatchDefaultRegex() {
        final SOURCE = '''
            class MyClass {
                private MyClass(BigDecimal deposit_amount) { }
            }
        '''
        assertSingleViolation(SOURCE, 3, 'BigDecimal deposit_amount')
    }

    void testApplyTo_DoesNotMatchDefaultRegex_NoClassDefined() {
        final SOURCE = 'def myMethod(int Count = 23) { }'
        assertSingleViolation(SOURCE, 1, 'int Count = 23')
    }

    void testApplyTo_Closure_DoesNotMatchDefaultRegex() {
        final SOURCE = '''
            class MyClass {
                def closure = { int Count = 23 ->
                    return Count
                }
            }
        '''
        assertSingleViolation(SOURCE, 3, 'int Count = 23')
    }

    void testApplyTo_ClosureAndConstructor() {
        final SOURCE = '''
            class MyClass {
                private MyClass(BigDecimal deposit_amount) { }
                def closure = { int Count = 23 ->
                    return Count
                }
            }
        '''
        // TODO This should not be order-dependent
        assertTwoViolations(SOURCE, 4, 'int Count = 23', 3, 'BigDecimal deposit_amount')
    }

    void testApplyTo_IgnoreParametersNames_MatchesSingleName() {
        final SOURCE = '''
            class MyClass {
                def myMethod(BigDecimal deposit_amount) { }
            }
        '''
        rule.ignoreParameterNames = 'deposit_amount'
        assertNoViolations(SOURCE)
    }

    void testApplyTo_IgnoreParameterNames_MatchesNoNames() {
        final SOURCE = '''
            class MyClass {
                def myMethod(BigDecimal deposit_amount) { }
            }
        '''
        rule.ignoreParameterNames = 'Other'
        assertSingleViolation(SOURCE, 3, 'BigDecimal deposit_amount')
    }

    void testApplyTo_IgnoreParameterNames_MultipleNamesWithWildcards() {
        final SOURCE = '''
            class MyClass {
                def myMethod(BigDecimal deposit_amount) { }
                String m2(int GOOD) { }
                def m4(int _amount) { 100.25 }
                def m5(int OTHER_name) { }
            }
        '''
        rule.ignoreParameterNames = 'OTHER?name,_*,GOOD'
        assertSingleViolation(SOURCE, 3, 'BigDecimal deposit_amount')
    }

    void testApplyTo_NoParameterDefinition() {
        final SOURCE = ' class MyClass { } '
        assertNoViolations(SOURCE)
    }

    protected Rule createRule() {
        return new ParameterNameRule()
    }

}