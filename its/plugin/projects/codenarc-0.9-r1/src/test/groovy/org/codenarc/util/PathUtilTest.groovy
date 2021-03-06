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
package org.codenarc.util

import org.codenarc.test.AbstractTestCase

/**
 * Tests for PathUtil
 *
 * @author Chris Mair
 * @version $Revision: 290 $ - $Date: 2010-01-17 05:33:12 +0300 (Вс, 17 янв 2010) $
 */
class PathUtilTest extends AbstractTestCase{

    void testGetName() {
        assert PathUtil.getName(null) == null
        assert PathUtil.getName('') == null
        assert PathUtil.getName('abc') == 'abc'
        assert PathUtil.getName('/abc') == 'abc'
        assert PathUtil.getName('/dir/abc') == 'abc'
        assert PathUtil.getName('\\abc') == 'abc'
    }

}