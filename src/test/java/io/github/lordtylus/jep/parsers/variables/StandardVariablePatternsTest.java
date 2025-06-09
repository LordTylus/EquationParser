/*
  Copyright 2025 Martin Rökker

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package io.github.lordtylus.jep.parsers.variables;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardVariablePatternsTest {

    @Test
    void noneHasTheRightPattern() {

        /* Given / When */

        VariablePattern sut = StandardVariablePatterns.NONE;

        /* Then */

        VariablePattern expected = new VariablePattern(false, '\u0000', '\u0000');

        assertEquals(expected, sut);
    }

    @Test
    void bracketsHasTheRightPattern() {

        /* Given / When */

        VariablePattern sut = StandardVariablePatterns.BRACKETS;

        /* Then */

        VariablePattern expected = new VariablePattern(true, '[', ']');

        assertEquals(expected, sut);
    }

    @Test
    void bracesHasTheRightPattern() {

        /* Given / When */

        VariablePattern sut = StandardVariablePatterns.BRACES;

        /* Then */

        VariablePattern expected = new VariablePattern(true, '{', '}');

        assertEquals(expected, sut);
    }

    @Test
    void tagsHasTheRightPattern() {

        /* Given / When */

        VariablePattern sut = StandardVariablePatterns.TAGS;

        /* Then */

        VariablePattern expected = new VariablePattern(true, '<', '>');

        assertEquals(expected, sut);
    }

}