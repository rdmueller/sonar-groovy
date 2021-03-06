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
package org.codenarc.rule.exceptions

import org.codenarc.rule.AbstractAstVisitor
import org.codehaus.groovy.ast.stmt.CatchStatement

/**
 * AstVisitor implementation that checks for catching an exception type specified in the constructor
 * <p/>
 * This is an internal class and its API is subject to change.
 *
 * @author Chris Mair
 * @version $Revision: 139 $ - $Date: 2009-05-08 05:55:09 +0400 (Пт, 08 май 2009) $
 */
class CommonCatchAstVisitor extends AbstractAstVisitor  {
    private exceptionClassNameWithoutPackage

    /**
     * Construct a new instance, specifying the exception class name
     * @param exceptionClassNameWithoutPackage - the name of the exception class to check for, without the package
     */
    CommonCatchAstVisitor(String exceptionClassNameWithoutPackage) {
        this.exceptionClassNameWithoutPackage = exceptionClassNameWithoutPackage
    }

    void visitCatchStatement(CatchStatement catchStatement) {
        if (isFirstVisit(catchStatement) && catchStatement.exceptionType.nameWithoutPackage == exceptionClassNameWithoutPackage) {
            addViolation(catchStatement)
        }
        super.visitCatchStatement(catchStatement)
    }

}