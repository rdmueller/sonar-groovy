/*
 * Sonar Groovy Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.groovy;

import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.plugins.groovy.foundation.Groovy;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GroovyCommonRulesDecoratorTest {

  @Test
  public void test_declaration() throws Exception {
    GroovyCommonRulesDecorator decorator = new GroovyCommonRulesDecorator(new DefaultFileSystem(new File(".")), mock(CheckFactory.class), mock(ResourcePerspectives.class));
    assertThat(decorator.language()).isEqualTo(Groovy.KEY);
  }

}
