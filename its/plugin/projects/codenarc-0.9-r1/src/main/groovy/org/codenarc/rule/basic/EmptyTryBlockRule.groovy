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
package org.codenarc.rule.basic

import org.codenarc.rule.AbstractAstVisitor
import org.codehaus.groovy.ast.stmt.CatchStatement
import org.codenarc.rule.AbstractAstVisitorRule
import org.codehaus.groovy.ast.stmt.TryCatchStatement
import org.codenarc.util.AstUtil

/**
 * Rule that checks for empty try blocks
 *
 * @author Chris Mair
 * @version $Revision: 139 $ - $Date: 2009-05-08 05:55:09 +0400 (Пт, 08 май 2009) $
 */
class EmptyTryBlockRule extends AbstractAstVisitorRule {
    String name = 'EmptyTryBlock'
    int priority = 2
    Class astVisitorClass = EmptyTryBlockAstVisitor
}

class EmptyTryBlockAstVisitor extends AbstractAstVisitor  {
    void visitTryCatchFinally(TryCatchStatement tryCatchStatement) {
        if (isFirstVisit(tryCatchStatement) && AstUtil.isEmptyBlock(tryCatchStatement.tryStatement)) {
            addViolation(tryCatchStatement)
        }
        super.visitTryCatchFinally(tryCatchStatement)
    }

}