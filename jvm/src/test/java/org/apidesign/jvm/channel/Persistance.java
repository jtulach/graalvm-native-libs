package org.apidesign.jvm.channel;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

abstract class Persistance<T> {
    Persistance(Class<T> clazz, boolean subclasses, int id) {
    }

    static abstract class Output implements DataOutput {
        void writeObject(Object o) {
        }
    }

    static abstract class Input implements DataInput {
        <T> T readObject() throws IOException, ClassNotFoundException {
            throw new IOException();
        }
    }

    protected abstract void writeObject(T obj, Output out) throws IOException;
    protected abstract T readObject(Persistance.Input in) throws IOException, ClassNotFoundException;

}
