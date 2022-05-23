/*
 * Copyright 2018 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package event.logging.base.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import event.logging.Event;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class TestJacksonAnnotations {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestJacksonAnnotations.class);

    @Test
    void test() throws Exception {
        final Class<?> rootClass = Event.class;

        final Object object;
        try (final ScanResult scanResult =
                     new ClassGraph()
                             .enableAllInfo()             // Scan classes, methods, fields, annotations
                             .acceptPackages(rootClass.getPackage().getName())  // Scan com.xyz and subpackages (omit to scan all packages)
                             .scan()) {                   // Start the scan
            object = createObject(scanResult, Collections.emptySet(), rootClass, null);
        }

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        final String json = mapper.writeValueAsString(object);
        LOGGER.info(json);
        final Object result = mapper.readValue(json, rootClass);
        LOGGER.info(result.toString());
    }

    private Object createObject(final ScanResult scanResult,
                                final Set<Class<?>> parentStack,
                                final Class<?> clazz,
                                final Type type) {
        if (Object.class == clazz) {
            return "test";
        } else if (List.class.isAssignableFrom(clazz)) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            final Type[] types = parameterizedType.getActualTypeArguments();
            final Type type1 = types[0];
//                final ParameterizedType parameterizedType1 = (ParameterizedType) types[0];
//                final Class<?> typeClass1 = (Class<?>) parameterizedType1.getActualTypeArguments()[0];
//            final Class<?> typeClass1 = (Class<?>) ;

//            final Class<?> typeClass1 = clazz.getTypeParameters()[0].getGenericDeclaration();
            final Object o = createObject(scanResult, parentStack, (Class<?>) type1, type1);
            return Collections.singletonList(o);

//            final Type type = parameter.getParameterizedType();

//            final Object o;
//            if (Object.class == typeClass1) {
//                return "test";
//            } else {
//                o = inspect(scanResult, typeClass1);
//            }
//            objects[i] = Collections.singletonList(o);
        } else if (Map.class.isAssignableFrom(clazz)) {
            return Collections.emptyMap();
        } else if (Collection.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("Unexpected collection type: " + clazz);
        } else if (String.class.isAssignableFrom(clazz)) {
            return "test";
        } else if (Integer.class.isAssignableFrom(clazz)) {
            return 1;
        } else if (Long.class.isAssignableFrom(clazz)) {
            return 1L;
        } else if (Float.class.isAssignableFrom(clazz)) {
            return 1F;
        } else if (Double.class.isAssignableFrom(clazz)) {
            return 1D;
        } else if (Short.class.isAssignableFrom(clazz)) {
            return (short) 1;
        } else if (Boolean.class.isAssignableFrom(clazz)) {
            return true;
        } else if (Date.class.isAssignableFrom(clazz)) {
            return new Date();
        } else if (BigInteger.class.isAssignableFrom(clazz)) {
            return BigInteger.valueOf(1L);
        } else if (BigDecimal.class.isAssignableFrom(clazz)) {
            return BigDecimal.valueOf(1L);
        } else if (clazz.isEnum()) {
            return clazz.getEnumConstants()[0];
        } else {
            Class<?> selectedClass = clazz;

            // See if there are subclasses.
            final List<Class<?>> subClasses = getSubClasses(scanResult, clazz); // TODO : FORK HERE SOMEHOW
            if (subClasses.size() > 0) {
                selectedClass = subClasses.get(0);
            }

            if (parentStack.contains(selectedClass)) {
                // Don't go any deeper else we will SO.
                return null;
            } else {
                final Set<Class<?>> stack = new HashSet<>(parentStack);
                stack.add(clazz);
                return createCustomObject(scanResult, stack, selectedClass);
            }
        }
    }

    private Object createCustomObject(final ScanResult scanResult,
                                      final Set<Class<?>> parentStack,
                                      final Class<?> clazz) {
        // Determine which constructor to use.
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<?> constructor = null;
        if (constructors.length == 1) {
            constructor = constructors[0];
        } else {
            for (Constructor<?> c : constructors) {
                if (c.getParameterCount() > 0) {
                    if (constructor != null) {
                        throw new RuntimeException("Not sure which constructor to use");
                    }
                    constructor = c;
                }
            }
        }

        if (constructor == null) {
            throw new RuntimeException("Constructor not found: " + clazz);
        }

        final Parameter[] parameters = constructor.getParameters();
        final Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            final Parameter parameter = parameters[i];
            final Class<?> paramType = parameter.getType();
            objects[i] = createObject(scanResult, parentStack, paramType, parameter.getParameterizedType());
        }

        final Object result;
        try {
            result = constructor.newInstance(objects);
        } catch (final InstantiationException |
                       IllegalAccessException |
                       IllegalArgumentException |
                       InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }

    private List<Class<?>> getSubClasses(final ScanResult scanResult,
                                         final Class<?> superClass) {
        final List<String> subClassNames = getSubClasses(scanResult, superClass.getName());
        return subClassNames
                .stream()
                .map(subClassName -> {
                    try {
                        return superClass.getClassLoader().loadClass(subClassName);
                    } catch (final Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                })
                .collect(Collectors.toList());
    }

    private List<String> getSubClasses(final ScanResult scanResult,
                                       final String superClassName) {
        final List<String> subClasses = new ArrayList<>();
        scanResult.getClassesImplementing(superClassName).forEach(classInfo ->
                addToList(scanResult, classInfo.getName(), subClasses));
        scanResult.getSubclasses(superClassName).forEach(classInfo ->
                addToList(scanResult, classInfo.getName(), subClasses));
        return subClasses;
    }

//    private void addToList(final ScanResult scanResult, final List<String> source, final List<String> dest) {
//        for (String implementation : source) {
//            final List<String> subList = getSubClasses(scanResult, implementation);
//            if (subList.size() == 0) {
//                if (!dest.contains(implementation)) {
//                    dest.add(implementation);
//                }
//            } else {
//                for (final String sub : subList) {
//                    if (!dest.contains(sub)) {
//                        dest.add(sub);
//                    }
//                }
//            }
//        }
//    }

    private void addToList(final ScanResult scanResult, final String source, final List<String> dest) {
        final List<String> subList = getSubClasses(scanResult, source);
        if (subList.size() == 0) {
            if (!dest.contains(source)) {
                dest.add(source);
            }
        } else {
            for (final String sub : subList) {
                if (!dest.contains(sub)) {
                    dest.add(sub);
                }
            }
        }
    }
}
