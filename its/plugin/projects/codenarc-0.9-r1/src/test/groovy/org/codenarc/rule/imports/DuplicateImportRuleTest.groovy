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
package org.codenarc.rule.imports

import org.codenarc.rule.Rule
import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.util.GroovyVersion

/**
 * Tests for DuplicateImportRule
 *
 * @author Chris Mair
 * @version $Revision: 302 $ - $Date: 2010-02-01 03:06:00 +0300 (Пн, 01 фев 2010) $
 */
class DuplicateImportRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 3
        assert rule.name == 'DuplicateImport'
    }

    void testApplyTo_Violation() {
        final SOURCE = '''
            import java.io.InputStream
            import java.io.OutputStream
            import java.io.InputStream
        '''
        if (GroovyVersion.isGroovy1_5() || GroovyVersion.isGroovy1_6()) {
            assertSingleViolation(SOURCE, 2, 'import java.io.InputStream')
        }
    }

    void testApplyTo_MultipleDuplicateImports() {
        final SOURCE = '''
            import abc.def.MyClass
            import java.io.OutputStream
            import abc.def.MyClass
            import xyz.OtherClass
            import abc.def.MyClass
        '''
        if (GroovyVersion.isGroovy1_5() || GroovyVersion.isGroovy1_6()) {
            // Can't distinguish between multiple duplicate imports of the same class
            assertTwoViolations(SOURCE, 2, 'import abc.def.MyClass', 2, 'import abc.def.MyClass')
        }
    }

    void testApplyTo_NoViolations() {
        final SOURCE = '''
            import java.io.InputStream
            import java.io.OutputStream
            import java.util.HashMap
        '''
        assertNoViolations(SOURCE)
    }

    protected Rule createRule() {
        return new DuplicateImportRule()
    }

}