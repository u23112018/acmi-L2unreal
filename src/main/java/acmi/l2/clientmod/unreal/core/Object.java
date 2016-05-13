/*
 * Copyright (c) 2016 acmi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package acmi.l2.clientmod.unreal.core;

import acmi.l2.clientmod.io.ObjectInput;
import acmi.l2.clientmod.io.UnrealPackage;
import acmi.l2.clientmod.io.annotation.Compact;
import acmi.l2.clientmod.io.annotation.ReadMethod;
import acmi.l2.clientmod.unreal.UnrealRuntimeContext;
import acmi.l2.clientmod.unreal.properties.L2Property;
import acmi.l2.clientmod.unreal.properties.PropertiesUtil;

import java.util.ArrayList;
import java.util.List;

import static acmi.l2.clientmod.io.UnrealPackage.ObjectFlag.HasStack;
import static acmi.l2.clientmod.io.UnrealPackage.ObjectFlag.getFlags;

public class Object {
    public UnrealPackage.ExportEntry entry;
    public StateFrame stateFrame;
    public List<L2Property> properties = new ArrayList<>();

    @ReadMethod
    public final void readObject(ObjectInput<UnrealRuntimeContext> input) {
        entry = input.getContext().getEntry();

        if (getFlags(input.getContext().getEntry().getObjectFlags()).contains(HasStack)) {
            stateFrame = new StateFrame();
            input.getSerializerFactory().forClass(StateFrame.class).readObject(stateFrame, input);
        }

        if (!(this instanceof Class))
            properties.addAll(PropertiesUtil.readProperties(input, this.entry.getFullClassName()));
    }

    public String getFullName() {
        return entry.getObjectFullName();
    }

    public String getClassFullName() {
        return entry.getFullClassName();
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", getFullName(), getClassFullName());
    }

    public static class StateFrame {
        @Compact
        public int node;
        @Compact
        public int stateNode;
        public long probeMask;
        public int latentAction;
        @Compact
        public int offset;
    }
}
