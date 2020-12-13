/*
 * Copyright 2020 Arthur Sadykov.
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
package com.github.isarthur.netbeans.editor.textselector.selection;

/**
 *
 * @author Arthur Sadykov
 */
public class Region {

    private final int startOffset;
    private final int endOffset;

    public Region(int startOffset, int endOffset) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final Region other = (Region) object;
        if (this.endOffset != other.getEndOffset()) {
            return false;
        }
        return this.startOffset == other.getStartOffset();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.endOffset;
        hash = 97 * hash + this.startOffset;
        return hash;
    }

    @Override
    public String toString() {
        return "Region{" + "startOffset=" + startOffset + ", endOffset=" + endOffset + '}';
    }
}
