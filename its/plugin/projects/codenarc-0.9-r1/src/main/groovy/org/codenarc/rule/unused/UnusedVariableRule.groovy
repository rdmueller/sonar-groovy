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
package org.codenarc.rule.unused

import org.codenarc.rule.AbstractAstVisitor
import org.codenarc.rule.AbstractAstVisitorRule
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codenarc.util.AstUtil
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.ConstantExpression

/**
 * Rule that checks for variables that are not referenced.
 *
 * @author Chris Mair
 * @version $Revision: 322 $ - $Date: 2010-04-16 06:46:29 +0400 (Пт, 16 апр 2010) $
 */
class UnusedVariableRule extends AbstractAstVisitorRule {
    String name = 'UnusedVariable'
    int priority = 2
    Class astVisitorClass = UnusedVariableAstVisitor
}

class UnusedVariableAstVisitor extends AbstractAstVisitor  {
    private variablesByBlockScope = [] as Stack
    private variablesInCurrentBlockScope

    void visitDeclarationExpression(DeclarationExpression declarationExpression) {
        if (isFirstVisit(declarationExpression)) {
            def varExpressions = AstUtil.getVariableExpressions(declarationExpression)
            varExpressions.each { varExpression ->
                variablesInCurrentBlockScope[varExpression] = false
            }
        }
        super.visitDeclarationExpression(declarationExpression)
    }

    void visitBlockStatement(BlockStatement block) {
        variablesInCurrentBlockScope = [:]
        variablesByBlockScope.push(variablesInCurrentBlockScope)

        super.visitBlockStatement(block)

        variablesInCurrentBlockScope.each { varExpression, isUsed ->
            if (!isUsed) {
                addViolation(varExpression)
            }
        }

        variablesByBlockScope.pop()
        variablesInCurrentBlockScope = variablesByBlockScope.empty() ? null : variablesByBlockScope.peek()
    }
    
    void visitVariableExpression(VariableExpression expression) {
        markVariableAsReferenced(expression.name, expression)

        // This causes problems (StackOverflow) in Groovy 1.7.0
        //super.visitVariableExpression(expression)
    }

    void visitMethodCallExpression(MethodCallExpression call) {
        // If there happens to be a method call on a method with the same name as the variable.
        // This handles the case of defining a closure and then executing it, e.g.:
        //      def myClosure = { println 'ok' }
        //      myClosure()
        // But this could potentially "hide" some unused variables (i.e. false negatives).
        if (call.isImplicitThis() &&
            call.method instanceof ConstantExpression) {
            markVariableAsReferenced(call.method.value, null)
        }
        super.visitMethodCallExpression(call)
    }

    private void markVariableAsReferenced(String varName, VariableExpression varExpression) {
        for(blockVariables in variablesByBlockScope) {
            for(var in blockVariables.keySet()) {
                if (var.name == varName && var != varExpression) {
                    blockVariables[var] = true
                    return
                }
            }
        }
    }
}