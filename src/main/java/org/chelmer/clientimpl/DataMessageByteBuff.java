package org.chelmer.clientimpl;

import com.fasterxml.jackson.annotation.JacksonInject;
import io.netty.buffer.ByteBuf;
import org.chelmer.exceptions.CouldNotDeserializeException;
import org.chelmer.model.entity.LoxUuid;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Deserialize a WebSocket data message into a Java object.
 * Mapping is determined via the specified class.
 * Parameters will be deserialized in the order they appear in the classes constructor, using the following mappings:
 * <ul>
 * <li>Integer - 16 bit</li>
 * <li>Long - 32 bit</li>
 * <li>Double - 64 bit</li>
 * <li>Byte - byte</li>
 * <li>LoxUuid - loxone format uuid</li>
 * <li>String - assumption is that this is preceeded by its length</li>
 * </ul>
 */
public class DataMessageByteBuff {
    private final boolean unsigned;
    private final UuidComponentRegistry registry;

    public DataMessageByteBuff(boolean unsigned, UuidComponentRegistry registry) {
        this.unsigned = unsigned;
        this.registry = registry;
    }

    public DataMessageByteBuff(UuidComponentRegistry registry) {
        this(true, registry);
    }

    public <T> List<T> readObjects(ByteBuf bytes, Class<T> clazz, long messageLen) {
        List<T> objects = new ArrayList<T>();

        int startingIndex = bytes.readerIndex();
        while ((bytes.readerIndex() - startingIndex) < messageLen) {
            objects.add(readObject(bytes, clazz));
        }

        return objects;
    }

    public <T> List<T> readFixedNumberOfObjects(ByteBuf bytes, Class<T> clazz, long numberOfObjects) {
        List<T> objects = new ArrayList<T>();

        for (int i = 0; i < numberOfObjects; i++) {
            objects.add(readObject(bytes, clazz));
        }

        return objects;
    }

    public <T> T readObject(ByteBuf bytes, Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        if (constructors.length != 1) {
            throw new IllegalArgumentException(String.format("%s must only have one constructor to be used with DataMessageParser ", clazz.getName()));
        }

        Parameter[] parameters = constructors[0].getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class<?> type = param.getType();

            // todo: should be annotation
            boolean unsigned = this.unsigned;
            if (param.getName().endsWith("_unsigned")) {
                unsigned = true;
            }
            if (param.getName().endsWith("_signed")) {
                unsigned = false;
            }

            if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
                args[i] = (unsigned ? bytes.readUnsignedShortLE() : bytes.readShort());
            } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
                args[i] = (unsigned ? bytes.readUnsignedIntLE() : bytes.readIntLE());
            } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
                args[i] = Double.longBitsToDouble(bytes.readLongLE());
            } else if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {
                args[i] = bytes.readByte();
            } else if (type.equals(LoxUuid.class)) {
                long uuid1 = bytes.readUnsignedIntLE();
                long uuid2 = bytes.readUnsignedShortLE();
                long uuid3 = bytes.readUnsignedShortLE();
                byte[] uuid4 = new byte[8];
                for (int j = 0; j < 8; j++) {
                    uuid4[j] = bytes.readByte();
                }
                args[i] = new LoxUuid(String.format("%08x-%04x-%04x-%02x%02x%02x%02x%02x%02x%02x%02x", uuid1, uuid2, uuid3, uuid4[0], uuid4[1], uuid4[2], uuid4[3], uuid4[4], uuid4[5], uuid4[6], uuid4[7]));
            } else if (type.equals(String.class)) {
                if (i == 0 || !(args[i - 1] instanceof Number)) {
                    throw new CouldNotDeserializeException("DataMessageByteBuff cannot read a string without knowing its length");
                }

                int textLen = ((Number) args[i - 1]).intValue();
                args[i] = bytes.readCharSequence(textLen, Charset.forName("US-ASCII"));

                int paddingBytes = textLen % 4;
                if (paddingBytes > 0) {
                    for (int j = 0; j < (4 - paddingBytes); j++) {
                        bytes.readByte();
                    }
                }
            }
        }

        try {
            T obj = (T) constructors[0].newInstance(args);
            injectRegistryIntoAnnotatedMethods(obj);
            return obj;
        } catch (InstantiationException e) {
            throw new CouldNotDeserializeException("DataMessageByteBuff cannot deserialize type " + clazz.getName(), e);
        } catch (IllegalAccessException e) {
            throw new CouldNotDeserializeException("DataMessageByteBuff cannot deserialize type " + clazz.getName(), e);
        } catch (InvocationTargetException e) {
            throw new CouldNotDeserializeException("DataMessageByteBuff cannot deserialize type " + clazz.getName(), e);
        }
    }

    private List<Method> injectRegistryIntoAnnotatedMethods(final Object obj) {
        final List<Method> methods = new ArrayList<Method>();
        Class<?> klass = obj.getClass();
        while (klass != Object.class) {
            final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));
            for (final Method method : allMethods) {
                if (method.isAnnotationPresent(JacksonInject.class)) {
                    if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == UuidComponentRegistry.class) {
                        try {
                            method.invoke(obj, registry);
                        } catch (Exception e) {
                            throw new CouldNotDeserializeException("Exception injecting component registry into binary class " + klass.getName());
                        }
                    } else {
                        throw new CouldNotDeserializeException("@JacksonInject only currently supported for UuidComponentRegistry on classes created with DataMessageByteBuff. Found " + method.getClass() + "." + method.getName());
                    }
                }
            }
            klass = klass.getSuperclass();
        }
        return methods;
    }
}
