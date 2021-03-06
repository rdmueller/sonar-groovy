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
package org.codenarc.rule.concurrency

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for SynchronizedOnThisRule.
 *
 * @author Hamlet D'Arcy
 * @version $Revision: 329 $ - $Date: 2010-04-29 06:20:25 +0400 (Чт, 29 апр 2010) $
 */
class SynchronizedOnThisRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'SynchronizedOnThis'
    }

    void testApplyTo_Violation() {
        final SOURCE = '''
            class MyClass {
                private final def lock = new Object()

                def Method1() {
                    synchronized(this) { ; }
                }
                def Method2() {
                    synchronized(lock) { ; }
                }
                def Method3() {
                    synchronized(this) { ; }
                }
                def Method4() {
                    synchronized(lock) { ; }
                }
            }
        '''
        assertTwoViolations(SOURCE,
                6, 'synchronized(this) { ; }',
                12, 'synchronized(this) { ; }')
    }

    void testApplyTo_NoViolations() {
        final SOURCE = '''
            class MyClass {
                private final def lock = new Object()

                def Method1() {
                    synchronized(lock) { ; }
                }
                def Method2() { 
                    synchronized(lock) { ; }
                }
            }'''
        assertNoViolations(SOURCE)
    }

    protected Rule createRule() {
        return new SynchronizedOnThisRule()
    }

}
