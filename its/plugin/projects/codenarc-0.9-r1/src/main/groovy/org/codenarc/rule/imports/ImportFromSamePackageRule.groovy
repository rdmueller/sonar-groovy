/*
 * Copyright 2008 the original author or authors.
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

import org.codenarc.rule.AbstractRule
import org.codenarc.source.SourceCode

/**
 * Checks for an import of a class that is within the same package as the importing class.
 *
 * @author Chris Mair
 * @version $Revision: 303 $ - $Date: 2010-02-03 04:36:02 +0300 (Ср, 03 фев 2010) $
 */
class ImportFromSamePackageRule extends AbstractRule {
    String name = 'ImportFromSamePackage'
    int priority = 3

    void applyTo(SourceCode sourceCode, List violations) {
        if (sourceCode.ast?.imports && sourceCode.ast.packageName) {
            def rawPackage = sourceCode.ast.packageName
            def filePackageName = rawPackage.endsWith('.') ? rawPackage[0..-2] : rawPackage
            getImportsSortedByLineNumber(sourceCode).each { importNode ->
                def importPackageName = packageNameForImport(importNode)
                if (importPackageName == filePackageName) {
                    violations.add(createViolationForImport(sourceCode, importNode))
                }
            }
        }
    }

}