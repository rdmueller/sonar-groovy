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
 * Tests for PropertyNameRule
 *
 * @author Chris Mair
 * @version $Revision: 257 $ - $Date: 2009-12-26 01:07:22 +0300 (Сб, 26 дек 2009) $
 */
class PropertyNameRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'PropertyName'
    }

    void testRegex_DefaultValue() {
        assert 'abc' ==~ rule.regex
        assert 'aXaX123' ==~ rule.regex
        assert !('abc_def' ==~ rule.regex)
        assert !('ABC123abc' ==~ rule.regex)
    }

    void testFinalRegex_DefaultValue() {
        assert rule.finalRegex == null
    }

    void testStaticRegex_DefaultValue() {
        assert rule.staticRegex == null
    }

    void testStaticFinalRegex_DefaultValue() {
        assert 'ABC' ==~ rule.staticFinalRegex
        assert 'ABC_123_DEF' ==~ rule.staticFinalRegex
        assert !('abc_def' ==~ rule.staticFinalRegex)
        assert !('ABC123abc' ==~ rule.staticFinalRegex)
    }

    void testRegexIsNull() {
        rule.regex = null
        shouldFailWithMessageContaining('regex') { applyRuleTo('class MyClass { int count }') }
    }

    void testApplyTo_DoesNotMatchDefaultRegex() {
        final SOURCE = '''
          class MyClass {
            BigDecimal deposit_amount
          }
        '''
        assertSingleViolation(SOURCE, 3, 'BigDecimal deposit_amount')
    }

    void testApplyTo_MatchesDefaultRegex() {
        final SOURCE = '''
            class MyClass {
                int count
            }
        '''
        assertNoViolations(SOURCE)
    }

    void testApplyTo_Static_DoesNotMatchDefaultRegex() {
        final SOURCE = '''
          class MyClass {
            static int Count
          }
        '''
        assertSingleViolation(SOURCE, 3, 'static int Count')
    }

    void testApplyTo_DoesNotMatchCustomRegex() {
        final SOURCE = '''
            class MyClass {
                int count
            }
        '''
        rule.regex = /z.*/
        assertSingleViolation(SOURCE, 3, 'int count')
    }

    void testApplyTo_MatchesCustomRegex() {
        final SOURCE = '''
            class MyClass {
              int zCount = 23
            }
        '''
        rule.regex = /z.*/
        assertNoViolations(SOURCE)
    }

    void testApplyTo_DoesNotMatchDefaultRegex_NoClassDefined() {
        final SOURCE = ' int Count '
        assertNoViolations(SOURCE)
    }

    void testApplyTo_MatchesCustomRegex_NoClassDefined() {
        final SOURCE = ' int zCount '
        rule.regex = /z.*/
        assertNoViolations(SOURCE)
    }

    void testApplyTo_Final_DefaultFinalRegex() {
        final SOURCE = '''
          class MyClass {
            final int Count
            final int ok
          }
        '''
        assertSingleViolation(SOURCE, 3, 'final int Count')
    }

    void testApplyTo_Final_FinalRegexSet() {
        final SOURCE = '''
            class MyClass {
                final int zCount
            }
        '''
        rule.finalRegex = /z.*/
        assertNoViolations(SOURCE)
    }

    void testApplyTo_Static_StaticRegexNotSet() {
        final SOURCE = '''
          class MyClass {
            static int Count
          }
        '''
        assertSingleViolation(SOURCE, 3, 'static int Count')
    }

    void testApplyTo_Static_StaticRegexSet() {
        final SOURCE = '''
            class MyClass {
                static int Count
            }
        '''
        rule.staticRegex = /C.*/
        assertNoViolations(SOURCE)
    }

    void testApplyTo_StaticFinal_DefaultStaticFinalRegex() {
        final SOURCE = '''
          class MyClass {
            static final int count
          }
        '''
        assertSingleViolation(SOURCE, 3, 'static final int count')
    }

    void testApplyTo_StaticFinal_CustomStaticFinalRegex() {
        final SOURCE = '''
            class MyClass {
                static final int Count
            }
        '''
        rule.staticRegex = /Z.*/    // ignored
        rule.finalRegex = /X.*/     // ignored
        rule.staticFinalRegex = /C.*/
        assertNoViolations(SOURCE)
    }

    void testApplyTo_StaticFinal_DefaultsToFinalRegex() {
        final SOURCE = '''
          class MyClass {
            static final int Count
          }
        '''
        rule.staticRegex = /C.*/       // ignored
        assertSingleViolation(SOURCE, 3, 'static final int Count')
    }

    void testApplyTo_StaticFinal_DefaultsToStaticRegex() {
        final SOURCE = '''
          class MyClass {
            static final int Count
          }
        '''
        rule.staticFinalRegex = null
        rule.staticRegex = /C.*/
        assertNoViolations(SOURCE)
    }

    void testApplyTo_StaticFinal_DefaultsToRegex() {
        final SOURCE = '''
          class MyClass {
            static final int Count
          }
        '''
        rule.staticFinalRegex = null
        rule.staticRegex = null
        rule.regex = /C.*/
        assertNoViolations(SOURCE)
    }

    void testApplyTo_ignorePropertyNames_MatchesSingleName() {
        final SOURCE = '''
          class MyClass {
            static int Count
          }
        '''
        rule.ignorePropertyNames = 'Count'
        assertNoViolations(SOURCE)
    }

    void testApplyTo_ignorePropertyNames_MatchesNoNames() {
        final SOURCE = '''
          class MyClass {
            int Count
          }
        '''
        rule.ignorePropertyNames = 'Other'
        assertSingleViolation(SOURCE, 3, 'int Count')
    }

    void testApplyTo_ignorePropertyNames_MultipleNamesWithWildcards() {
        final SOURCE = '''
          class MyClass {
            String GOOD_NAME = 'good'
            static int Count
            def _amount = 100.25
            def OTHER_name
          }
        '''
        rule.ignorePropertyNames = 'OTHER?name,_*,GOOD_NAME' 
        assertSingleViolation(SOURCE, 4, 'static int Count')
    }

    void testApplyTo_Script() {
        final SOURCE = '''
            BigDecimal deposit_amount       // not considered a field
            int COUNT                       // not considered a field
        '''
        assertNoViolations(SOURCE)
    }

    void testApplyTo_FieldDefinitions() {
        final SOURCE = '''
          class MyClass {
             public BigDecimal Deposit_amount   // a field, not a property
             protected int COUNT                // a field, not a property
             private _someName = 'abc'          // a field, not a property
          }
        '''
        assertNoViolations(SOURCE)
    }

    void testApplyTo_NoPropertyDefinition() {
        final SOURCE = ' class MyClass { } '
        assertNoViolations(SOURCE)
    }

    protected Rule createRule() {
        return new PropertyNameRule()
    }

}