package ananta.utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;


/**
 * @author Ananta0810
 * This class provides some methods related to reflection.
 * Most methods can handle NULL input well.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public final class MoreReflection {

    private static final Map<String, Class<?>> PRIMITIVE_WRAPPER_CLASS_MAP;

    private static final Set<Class<?>> WRAPPER_CLASSES;

    static {
        PRIMITIVE_WRAPPER_CLASS_MAP = Map.ofEntries(
            Map.entry("boolean", Boolean.class),
            Map.entry("byte", Byte.class),
            Map.entry("char", Character.class),
            Map.entry("short", Short.class),
            Map.entry("int", Integer.class),
            Map.entry("long", Long.class),
            Map.entry("double", Double.class),
            Map.entry("float", Float.class),
            Map.entry("void", Void.class)
        );

        WRAPPER_CLASSES = MoreSet.setOf(PRIMITIVE_WRAPPER_CLASS_MAP.values());
    }

    private MoreReflection() {
    }

    /**
     * Find field's value of an object.
     * NOTE: This method not support find value of static field.
     * If you want to find static field value, please use method {@link #findStaticFieldValue(Field field) findStaticFieldValue}
     *
     * @param field  field of the object. Field can be null.
     * @param object object that contains the value. Object can be null.
     * @return Optional.empty() if input is null or field can not be accessed. Otherwise, return Optional of object.
     */
    @NotNull
    public static Optional<?> findFieldValue(@Nullable final Object object, @Nullable final Field field) {
        if (field == null || object == null) {
            return Optional.empty();
        }
        if (isStaticField(field)) {
            return Optional.empty();
        }
        final boolean currentAccessState = field.canAccess(object);

        final boolean canAccess = field.trySetAccessible();
        if (!canAccess) {
            return Optional.empty();
        }

        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(object);
        } catch (final IllegalAccessException ignored) {
        }
        field.setAccessible(currentAccessState);

        return Optional.ofNullable(value);
    }

    /**
     * Find field's value of an object.
     * NOTE: This method not support find value of static field.
     * <pre>
     * Result example:
     *      - Object:
     *      {
     *          "father": {
     *              "child": "Hello world".
     *          }
     *      }
     *      - Field names: "father" -> Result: { "child": "Hello world". }
     *      - Field names: "father.child" -> Result: "Hello world"
     * </pre>
     *
     * @param object     object that contains the value. Object should not be null.
     * @param fieldNames fields name that you want to extract.
     *                   If you want to get nested field, please use comma between those nested fields.
     *                   Field name should not be null.
     * @return Optional.empty() if input is null or field can not be accessed. Otherwise, return Optional of object.
     */
    public static Optional<Object> findFieldValue(@Nullable final String fieldNames, @Nullable final Object object) {
        if (object == null || fieldNames == null) {
            return Optional.empty();
        }
        final String[] fieldNameList = fieldNames.split("\\.");
        Object result = object;
        for (final String field : fieldNameList) {
            result = findFieldValue0(result, field).orElse(null);
        }
        return Optional.ofNullable(result);
    }

    /**
     * Find field's value of static class.
     *
     * @param field field of the static class. Field can be null.
     * @return Optional.empty() if field is not static field or can not be accessed. Otherwise, return Optional of object.
     */
    @NotNull
    public static Optional<?> findStaticFieldValue(@Nullable final Field field) {
        if (!isStaticField(field)) {
            return Optional.empty();
        }
        final boolean currentAccessState = field.canAccess(null);

        final boolean canAccess = field.trySetAccessible();
        if (!canAccess) {
            return Optional.empty();
        }

        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(null);
        } catch (final IllegalAccessException ignored) {
        }
        field.setAccessible(currentAccessState);

        return Optional.ofNullable(value);
    }

    /**
     * Find field's value of static class.
     * NOTE: This method not support search nested fields.
     *
     * @param fieldName name of static field. Can be null.
     * @param clazz     class that you want to find static field. Can be null.
     * @return Optional.empty() if input is null or field is not static field or field can not be accessed.
     * Otherwise, return Optional of object.
     */
    @NotNull
    public static Optional<?> findStaticFieldValue(@Nullable final String fieldName, @Nullable final Class<?> clazz) {
        if (fieldName == null || clazz == null) {
            return Optional.empty();
        }
        return staticFieldsOf(clazz)
            .stream()
            .filter(field -> Objects.equals(field.getName(), fieldName))
            .findFirst()
            .flatMap(MoreReflection::findStaticFieldValue);
    }

    @NotNull
    private static Optional<?> findFieldValue0(@Nullable final Object object, @Nullable final String fieldName) {
        if (fieldName == null || object == null) {
            return Optional.empty();
        }
        return findField(fieldName, object.getClass()).flatMap(field -> findFieldValue(object, field));
    }

    /**
     * Set field value for object.
     *
     * @param object     Object that you want to set value. can be null.
     * @param fieldName  Name of field to set value. can be null.
     * @param fieldValue Value to set. can be null.
     * @return true if set value successfully. Otherwise, return false.
     * If return false, it might be due to some follow reasons:<br/>
     * - Object or field name is null<br/>
     * - Field not found.
     */
    public static boolean setFieldValue(@Nullable final Object object, @Nullable final String fieldName, @Nullable final Object fieldValue) {
        if (object == null || fieldName == null) {
            return false;
        }

        final Optional<Field> fieldOpt = findField(fieldName, object.getClass());
        final boolean fieldNotFound = fieldOpt.isEmpty();

        if (fieldNotFound) {
            return false;
        }
        final Field field = fieldOpt.get();
        return setFieldValue0(object, field, fieldValue);
    }

    /**
     * Set field value for object.
     *
     * @param object     Object that you want to set value. can be null.
     * @param field      Field to set value. can be null.
     * @param fieldValue Value to set. can be null.
     * @return true if set value successfully. Otherwise, return false.
     * If return false, it might be due to some follow reasons:<br/>
     * - Object or field name is null<br/>
     * - Field not found.
     */
    public static boolean setFieldValue(@Nullable final Object object, @Nullable final Field field, @Nullable final Object fieldValue) {
        if (object == null || field == null) {
            return false;
        }

        return setFieldValue0(object, field, fieldValue);
    }

    /**
     * Set value for static field.
     *
     * @param clazz      Class that having static field you want to set. can be null.
     * @param fieldName  Name of field to set value. can be null.
     * @param fieldValue Value to set. can be null.
     * @return True if set value successfully. Otherwise, return false.
     * If return false, it might be due to some follow reasons:<br/>
     * - Class or field name is null<br/>
     * - Field not found.
     * @throws IllegalArgumentException When founded field is not static field.
     */
    public static boolean setValueForStaticField(@Nullable final String fieldName, @Nullable final Object fieldValue, @Nullable final Class<?> clazz) {
        if (clazz == null || fieldName == null) {
            return false;
        }

        final Optional<Field> fieldOpt = findField(fieldName, clazz);
        final boolean fieldNotFound = fieldOpt.isEmpty();

        if (fieldNotFound) {
            return false;
        }
        final Field field = fieldOpt.get();
        return setValueForStaticField(field, fieldValue);
    }

    /**
     * Set value for static field.
     *
     * @param field      Field to set value. can be null.
     * @param fieldValue Value to set. can be null.
     * @return true if set value successfully. Otherwise, return false.
     * If return false, it might be due to some follow reasons:<br/>
     * - Object or field name is null<br/>
     * - Field not found.
     * @throws IllegalArgumentException When field is not static field.
     */
    public static boolean setValueForStaticField(@Nullable final Field field, @Nullable final Object fieldValue) {
        if (field == null) {
            return false;
        }
        if (!isStaticField(field)) {
            throw new IllegalArgumentException(String.format("Field %s is not static field.", field.getName()));
        }

        return setFieldValue0(null, field, fieldValue);
    }

    private static boolean setFieldValue0(@Nullable final Object object, @NotNull final Field field, @Nullable final Object fieldValue) {
        final boolean canAccess = field.canAccess(object);
        field.setAccessible(true);
        try {
            field.set(object, fieldValue);
        } catch (final IllegalAccessException e) {
            field.setAccessible(canAccess);
            return false;
        }
        field.setAccessible(canAccess);
        return true;
    }

    //=================================================// Listing //=================================================//
    /**
     * Find all fields of a class. Those fields including private and public fields in
     * that one class and its ancestors.
     *
     * @param clazz The class that you want to extract. Class can be null.
     * @return empty list if class is null, otherwise return its fields.
     */
    @NotNull
    public static List<Field> fieldsOf(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            return MoreList.emptyList();
        }
        final List<Class<?>> classes = ancestorClassesOf(clazz);
        return classes.stream().map(Class::getDeclaredFields).flatMap(Stream::of).collect(Collectors.toList());
    }

    /**
     * Find all static fields of certain classes. Those classes include ones from parent classes.
     *
     * @param clazz Class that you want to find static fields from.
     * @return Empty list if class passed is null. Otherwise, return static fields of class and its parent classes.
     */
    public static List<Field> staticFieldsOf(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            return MoreList.emptyList();
        }
        return MoreReflection.fieldsOf(clazz).stream().filter(MoreReflection::isStaticField).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Find all non-static fields of a class. Those fields including private and public fields in
     * that one class and its ancestors.
     *
     * @param clazz The class that you want to extract. Class can be null.
     * @return empty list if class is null, otherwise return its fields.
     */
    @NotNull
    public static List<Field> nonStaticFieldsOf(@Nullable final Class<?> clazz) {
        return fieldsOf(clazz)
            .stream()
            .filter(not(MoreReflection::isStaticField))
            .collect(Collectors.toList());
    }
    
    /**
     * Find all fields name of a class. Those fields including private and public fields in
     * that one class and its ancestors.
     * @param clazz The class that you want to extract. Class can be null.
     * @return empty list if class is null. Otherwise, return list of field's name.
     */
    @NotNull
    public static List<String> fieldNamesOf(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            return MoreList.emptyList();
        }
        return nonStaticFieldsOf(clazz).stream().map(Field::getName).collect(Collectors.toList());
    }
    
    /**
     * Find all fields name of a class. Those fields including private and public fields in
     * that one class and its ancestors.
     * @param clazz The class that you want to extract. Class can be null.
     * @return empty set if class is null. Otherwise, return set of field's name.
     */
    @NotNull
    public static Set<String> fieldNameSetOf(@Nullable final Class<?> clazz) {
        return MoreSet.setOf(fieldNamesOf(clazz));
    }
    
    /**
     * Find all annotations of a class and its ancestors.
     * @param clazz The class that you want to extract. Class can be null.
     * @return empty list if class is null. Otherwise, return annotations of class.
     */
    @NotNull
    public static List<Annotation> annotationsOf(@Nullable final Class<?> clazz) {
        if (clazz == null){
            return MoreList.emptyList();
        }
        final List<Class<?>> classes = ancestorClassesOf(clazz);
        return classes.stream().map(Class::getAnnotations).flatMap(Stream::of).collect(Collectors.toList());
    }
    
    /**
     * Find all annotations of a field
     * @param field can be null.
     * @return empty list if field is null. Otherwise, return annotations of the field.
     */
    @NotNull
    public static List<Annotation> annotationsOf(@Nullable final Field field) {
        if (field == null) {
            return MoreList.emptyList();
        }
        return MoreList.listOf(field.getDeclaredAnnotations());
    }
    
    /**
     * Find all annotations of a field as a list.
     * @param fieldName name of the field to extract. Field can be null.
     * @param clazz Class that contains the field. Class can be null.
     * @return Empty list if input are null or field not found. Otherwise, return list of annotations found.
     */
    @NotNull
    public static List<Annotation> annotationsOf(@Nullable final String fieldName, @Nullable final Class<?> clazz) {
        if (fieldName == null|| clazz == null) {
            return List.of();
        }
        return findField(fieldName, clazz).map(field -> MoreList.listOf(field.getAnnotations())).orElseGet(MoreList::emptyList);
    }
    
    /**
     * Find all ancestors of a class including the class itself.
     * @param clazz The class that you want to extract. Class can be null.
     * @return empty list if input is null. Otherwise, return class's ancestor classes.
     */
    @NotNull
    public static List<Class<?>> ancestorClassesOf(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            return MoreList.emptyList();
        }
        final List<Class<?>> classes = MoreList.emptyList();
        Class<?> tempClass = clazz;
        while (tempClass != null) {
            classes.add(tempClass);
            classes.addAll(MoreList.listOf(tempClass.getInterfaces()));
            tempClass = tempClass.getSuperclass();
        }

        return classes;
    }

    /**
     * Find static classes that extended from certain class, inside some wrapper classes.
     * @param wrapperClasses classes that contains static classes.
     * @param parentClass class that those static classes extended from.
     * @return list of extended classes found in wrapper classes.
     */
    @NotNull
    public static <T, R> List<Class<R>> findStaticClassesInside(
        final @Nullable Collection<Class<T>> wrapperClasses,
        final @Nullable Class<R> parentClass
    ) {
        if (wrapperClasses == null || parentClass == null) {
            return MoreList.emptyList();
        }

        return wrapperClasses
            .stream()
            .map(Class::getDeclaredClasses)
            .flatMap(Arrays::stream)
            .filter(clazz -> MoreReflection.isChildClassOf(parentClass, clazz))
            .map(entity -> MoreReflection.castToClass(parentClass, entity))
            .flatMap(Optional::stream)
            .collect(Collectors.toList());
    }

    //=================================================// Finders //=================================================//
    /**
     * Find certain annotation of a class.
     * @param annotationClass Annotation you want to find. Can't be null.
     * @param clazz Class that you want to get annotation from. Can't be null.
     * @return Empty Optional if not found. Otherwise, return Optional of annotation.
     */
    @NotNull
    public static <T> Optional<T> findAnnotation(@Nullable final Class<T> annotationClass, @Nullable final Class<?> clazz) {
        if (annotationClass == null || clazz == null) {
            return Optional.empty();
        }
        return annotationsOf(clazz).stream()
            .filter(ann -> ann.annotationType().isAssignableFrom(annotationClass))
            .map(annotationClass::cast)
            .findFirst();
    }
    
    /**
     * Find certain annotation of a field.
     * @param annotationClass Annotation you want to find. Can't be null.
     * @param field Field that you want to get annotation from. Can't be null.
     * @return Empty Optional if not found. Otherwise, return Optional of annotation.
     */
    @NotNull
    public static <T> Optional<T> findAnnotation(@Nullable final Class<T> annotationClass, @Nullable final Field field) {
        if (annotationClass == null || field == null) {
            return Optional.empty();
        }
        return Arrays.stream(field.getAnnotations())
            .filter(ann -> ann.annotationType().isAssignableFrom(annotationClass))
            .map(annotationClass::cast)
            .findFirst();
    }
    
    /**
     * Find field of a class based on its name (ignore case).
     * @param fieldName name of the field. If null then return Optional.empty()
     * @param clazz class which contains the field. If null then return Optional.empty().
     * @return Empty if input is null or can't find the field.
     */
    @NotNull
    public static Optional<Field> findField(@Nullable final String fieldName, @Nullable final Class<?> clazz) {
        if (fieldName == null|| clazz == null) {
            return Optional.empty();
        }
        final List<Field> fields = fieldsOf(clazz);
        return fields.stream().filter(field -> field.getName().equalsIgnoreCase(fieldName)).findFirst();
    }

    /**
     * Find generic type for superclass.
     * @param type can be null.
     * @param index the index of the generic type you want.
     * @return Optional of generic type. Return empty if any exception caught or input is null.
     */
    @NotNull
    public static Optional<Class<?>> genericTypeOf(@Nullable final Type type, final int index) {
        if (type == null) {
            return Optional.empty();
        }
        try {
            final Type genericType = ((ParameterizedType) type).getActualTypeArguments()[index];
            return Optional.of((Class<?>) genericType);
        } catch (final ClassCastException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Find generic type for superclass.
     * @param type can be null.
     * @return Optional of generic type. Return empty if any exception caught or input is null.
     */
    @NotNull
    public static Optional<Class<?>> genericTypeOf(@Nullable final Type type) {
        return genericTypeOf(type, 0);
    }
    
    /**
     * Find generic type for superclass.
     * @param clazz can be null. NOTE: This class must contains only one generic type.
     * @param index the index of the generic type you want.
     * @return Optional of generic type. Return empty if any exception caught or input is null.
     */
    @NotNull
    public static Optional<Class<?>> genericTypeOf(@Nullable final Class<?> clazz, final int index) {
        if (clazz == null) {
            return Optional.empty();
        }
        return genericTypeOf(clazz.getGenericSuperclass(), index);
    }
    
    /**
     * Find generic type for superclass.
     * @param clazz can be null. NOTE: This class must contains only one generic type.
     * @return Optional of generic type. Return empty if any exception caught or input is null.
     */
    @NotNull
    public static Optional<Class<?>> genericTypeOf(@Nullable final Class<?> clazz) {
        return genericTypeOf(clazz, 0);
    }
    
    /**
     * Find the type of the field. If field is collection, try to get the generic type.
     * @param field can be null.
     * @return empty if field is null. If field is collection, return generic of type.
     * Otherwise, return field's type.
     */
    @NotNull
    public static Optional<Class<?>> typeOf(@Nullable final Field field) {
        if (field == null) {
            return Optional.empty();
        }
        if (isCollection(field)) {
            return genericTypeOf(field.getGenericType(), 0);
        }
        return Optional.of(field.getType());
    }

    /**
     * Cast an object to other type.
     * @param clazz class that we want to cast to. must be not null.
     * @param object object that we want to cast. should be not null.
     * @return empty if input is null or cast failed. Otherwise, return casted value.
     */
    @NotNull
    public static <T> Optional<T> castTo(@Nullable final Class<T> clazz, @Nullable final Object object) {
        try {
            if (clazz == null || object == null) {
                return Optional.empty();
            }
            return Optional.of(clazz.cast(object));
        } catch (final ClassCastException e) {
            return Optional.empty();
        }
    }

    /**
     * Cast an object to other type.
     * @param clazz class that we want to cast to. must be not null.
     * @param object object that we want to cast. should be not null.
     * @return empty if input is null or cast failed. Otherwise, return casted value.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> Optional<Class<T>> castToClass(@Nullable final Class<T> clazz, @Nullable final Class<?> object) {
        try {
            if (clazz == null || object == null) {
                return Optional.empty();
            }
            return Optional.of(clazz.getClass().cast(object));
        } catch (final ClassCastException e) {
            return Optional.empty();
        }
    }

    /**
     * Find the wrapper class of a primitive type. If the input class is not primitive, return itself.
     * @param clazz input class, can be primitive type or not and should not be null.
     * @return input class itself if it is not primitive type. Otherwise, return wrapper class of primitive type.
     * @throws IllegalArgumentException if input class is null or primitive type has the wrapper class which is not supported.
     */
    @NotNull
    public static Class<?> wrapperClassOf(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class should not be null.");
        }
        if (!clazz.isPrimitive()) {
            return clazz;
        }
        final String className = clazz.getSimpleName();

        final Class<?> wrapperClass = PRIMITIVE_WRAPPER_CLASS_MAP.get(className);
        if (wrapperClass == null) {
            throw new IllegalArgumentException(MoreString.format("Can not find the wrapper class of {}.", className));
        }
        return wrapperClass;
    }

    //=================================================// Checkers //=================================================//
    /**
     * Check if a class is a builtin class or not.
     *
     * @param clazz Class that you want to check.
     * @return False if class is null or is not builtin class. Otherwise, return true.
     */
    public static boolean isBuiltinClass(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return clazz.getPackageName().startsWith("java.");
    }

    /**
     * Check if a class is static class or not.
     *
     * @param clazz Class that you want to check
     * @return False if class is null or is not static class. Otherwise, return true.
     */
    public static boolean isStaticClass(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return Modifier.isStatic(clazz.getModifiers());
    }

    /**
     * Check if a method is static method or not.
     *
     * @param method Method that you want to check
     * @return False if method is null or is not static method. Otherwise, return true.
     */
    public static boolean isStaticMethod(@Nullable final Method method) {
        if (method == null) {
            return false;
        }
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * Check if a field is static field or not.
     *
     * @param field Field that you want to check
     * @return False if field is null or is not static field. Otherwise, return true.
     */
    public static boolean isStaticField(@Nullable final Field field) {
        if (field == null) {
            return false;
        }
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * Check if certain class contains the annotation or not.
     *
     * @param clazz           can be null.
     * @param annotationClass can be null.
     * @return false if input is null or class doesn't have annotation. Otherwise, return true.
     */
    public static boolean isAnnotatedOn(@Nullable final Class<?> clazz, @Nullable final Class<? extends Annotation> annotationClass) {
        if (annotationClass == null || clazz == null) {
            return false;
        }
        final List<Annotation> annotations = annotationsOf(clazz);
        return annotations.stream().anyMatch(annotation -> annotation.annotationType().equals(annotationClass));
    }

    /**
     * Check if certain field contains the annotation or not.
     *
     * @param field           can be null.
     * @param annotationClass can be null.
     * @return false if any input is null or field not contains annotation. Otherwise, return true.
     */
    public static boolean isAnnotatedOn(@Nullable final Field field, @Nullable final Class<? extends Annotation> annotationClass) {
        if (annotationClass == null || field == null) {
            return false;
        }
        return field.getAnnotation(annotationClass) != null;
    }

    /**
     * Check if a field contains some annotations or not.
     *
     * @param method            Method that you want to check. Should not be null.
     * @param annotationClasses Annotation classes that you want to check if present is method.
     * @return False when:<p>
     * - Method is null.</p><p>
     * - No annotation passed down to method.</p><p>
     * - No required annotation found in method.</p><p>
     * Otherwise, return true.
     * </p>
     */
    @SafeVarargs
    public static boolean isAnnotatedOn(
        @Nullable final Method method,
        @Nullable final Class<? extends Annotation>... annotationClasses
    ) {
        if (method == null || annotationClasses == null || annotationClasses.length == 0) {
            return false;
        }
        if (annotationClasses.length == 1) {
            return method.isAnnotationPresent(annotationClasses[0]);
        }
        return MoreCollection.hasAnyOf(MoreList.listOf(annotationClasses), MoreList.listOf(method.getAnnotations()));
    }

    /**
     * Check if a field contains some annotations or not.
     *
     * @param method            Method that you want to check. Should not be null.
     * @param annotationClasses Annotation classes that you want to check if present is method.
     * @return False when:<p>
     * - Method is null.</p><p>
     * - No annotation passed down to method.</p><p>
     * - No required annotation found in method.</p><p>
     * Otherwise, return true.
     * </p>
     */
    public static boolean isAnnotatedOn(
        @Nullable final Method method,
        @Nullable final Collection<Class<? extends Annotation>> annotationClasses
    ) {
        if (method == null || MoreCollection.isEmpty(annotationClasses)) {
            return false;
        }
        final List<Class<? extends Annotation>> classes = Arrays
            .stream(method.getAnnotations())
            .map(Annotation::annotationType)
            .collect(Collectors.toList());
        return MoreCollection.hasAnyOf(annotationClasses, classes);
    }

    /**
     * Check if a field contains some annotations or not.
     *
     * @param clazz             Class that you want to check. Should not be null.
     * @param annotationClasses Annotation classes that you want to check if present is class.
     * @return False when:<p>
     * - Class is null.</p><p>
     * - No annotation passed down to class.</p><p>
     * - No required annotation found in class.</p><p>
     * Otherwise, return true.
     * </p>
     */
    @SafeVarargs
    public static boolean isAnnotatedOn(
        @Nullable final Class<?> clazz,
        @Nullable final Class<? extends Annotation>... annotationClasses
    ) {
        if (clazz == null || annotationClasses == null || annotationClasses.length == 0) {
            return false;
        }
        if (annotationClasses.length == 1) {
            assert annotationClasses[0] != null;
            return clazz.isAnnotationPresent(annotationClasses[0]);
        }
        return MoreCollection.hasAnyOf(MoreList.listOf(annotationClasses), MoreList.listOf(clazz.getAnnotations()));
    }

    /**
     * Check if a field contains some annotations or not.
     *
     * @param clazz             Class that you want to check. Should not be null.
     * @param annotationClasses Annotation classes that you want to check if present is class.
     * @return False when:<p>
     * - Class is null.</p><p>
     * - No annotation passed down to class.</p><p>
     * - No required annotation found in class.</p><p>
     * Otherwise, return true.
     * </p>
     */
    public static boolean isAnnotatedOn(
        @Nullable final Class<?> clazz,
        @Nullable final Collection<Class<? extends Annotation>> annotationClasses
    ) {
        if (clazz == null || MoreCollection.isEmpty(annotationClasses)) {
            return false;
        }
        final List<Class<? extends Annotation>> classes = Arrays
            .stream(clazz.getAnnotations())
            .map(Annotation::annotationType)
            .collect(Collectors.toList());
        return MoreCollection.hasAnyOf(annotationClasses, classes);
    }

    /**
     * Check if a field is exists in certain class or not.
     * This method will ignore case while checking.
     *
     * @param fieldName can be null.
     * @param clazz     can be null.
     * @return false if inputs are null or when field is not exist in the class. Otherwise, return true.
     */
    public static boolean hasField(@Nullable final String fieldName, @Nullable final Class<?> clazz) {
        if (fieldName == null || clazz == null) {
            return false;
        }
        return fieldNamesOf(clazz).stream().anyMatch(fieldName::equalsIgnoreCase);
    }
    
    /**
     * Check if annotation is defined in certain package.
     * @param annotation can be null.
     * @param packageName can be null.
     * @return false if input is null or if annotation is not in the package. Otherwise, return true.
     */
    public static boolean isOfPackage(@Nullable final Annotation annotation, @Nullable final String packageName) {
        if (annotation == null || packageName == null){
            return false;
        }
        return annotation.annotationType().getPackageName().equals(packageName);
    }
    
    /**
     * Check if a class is extended from other class.
     * @param parentClass can be null.
     * @param clazz can be null.
     * @return true if the class extends or implements the parent class or the input classes is null, otherwise false.
     */
    public static boolean isChildClassOf(@Nullable final Class<?> parentClass, @Nullable final Class<?> clazz) {
        if (parentClass == null || clazz == null) {
            return false;
        }
        final List<Class<?>> classes = ancestorClassesOf(clazz);
        return classes.stream().anyMatch(parentClass::equals);
    }

    /**
     * Check if a class is abstract class or is interface.
     * @param clazz can be null.
     * @return true if the is abstract class or is interface. Otherwise, return false.
     */
    public static boolean isAbstractClassOrInterface(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return clazz.isInterface() || isAbstractClass(clazz);
    }

    /**
     * Check if a class is abstract class.
     * @param clazz can be null.
     * @return true if the is abstract class. Otherwise, return false.
     */
    public static boolean isAbstractClass(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return Modifier.isAbstract(clazz.getModifiers());
    }

    /**
     * Check whether a field is primitive type or wrapper or not.
     * @param field can be null.
     * @return false if input is null or field is not primitive or wrapper. Otherwise, return true.
     */
    public static boolean isPrimitiveOrWrapper(@Nullable final Field field) {
        if (field == null) {
            return false;
        }
        final Class<?> type = field.getType();
        return type.isPrimitive() || WRAPPER_CLASSES.contains(type);
    }
    
    /**
     * Check whether a field is collection or not.
     * @param field can be null.
     * @return false if input is null or field is not collection. Otherwise, return true.
     */
    public static boolean isCollection(@Nullable final Field field) {
        if (field == null) {
            return false;
        }
        return Collection.class.isAssignableFrom(field.getType());
    }

}
