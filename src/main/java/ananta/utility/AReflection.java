package ananta.utility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Ananta0810
 * This class provides some methods related to reflection.
 * Most methods can handle NULL input well.
 */
@SuppressWarnings("unused")
public final class AReflection {

    private final static Map<String, Class<?>> primitiveWrapperClassMap;

    static {
        primitiveWrapperClassMap = Map.ofEntries(
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
    }

    private AReflection() {}
    
    private static final Set<Class<?>> wrapperClasses = ASet.setOf(
        Boolean.class, Byte.class, Character.class, Double.class, Float.class, Integer.class, Long.class, Short.class, Void.class
    );
    
    /**
     * Find field's value of a object.
     * @param field field of the object. Field can be null.
     * @param object object that contains the value. Object can be null.
     * @return Optional.empty() if input is null or field can not be accessed. Otherwise, return Optional of object.
     */
    @NotNull
    public static Optional<?> getFieldValue(@Nullable final Field field, @Nullable final Object object) {
        if (field == null || object == null){
            return Optional.empty();
        }
        
        final boolean canAccess = field.canAccess(object);
        
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(object);
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
        field.setAccessible(canAccess);
        
        return Optional.ofNullable(value);
    }

    /**
     * Find field's value of a object.
     * @param fieldName field name that you want to extract. Field name should not be be null.
     * @param object object that contains the value. Object should not be be null.
     * @return Optional.empty() if input is null or field can not be accessed. Otherwise, return Optional of object.
     */
    @NotNull
    public static Optional<?> getFieldValue(@Nullable final String fieldName, @Nullable final Object object) {
        if (fieldName == null || object == null){
            return Optional.empty();
        }
        return findField(fieldName, object.getClass()).flatMap(field -> getFieldValue(field, object));
    }

    /**
     * Set field value for object.
     * @param object can be null
     * @param fieldName can be null
     * @param fieldValue can be null
     * @return true if set value successfully. Otherwise, return false.
     * If return false, it might be due to some follow reasons:<br/>
     * - Object or field name is null<br/>
     * - Field not found.
     */
    public static boolean setFieldValue(@Nullable final Object object, final String fieldName, @Nullable final Object fieldValue) {
        if (object == null || fieldName == null) {
            return false;
        }
        
        final Optional<Field> fieldOpt = findField(fieldName, object.getClass());
        final boolean fieldNotFound = fieldOpt.isEmpty();
        
        if (fieldNotFound){
            return false;
        }
        final Field field = fieldOpt.get();
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
     * @param clazz The class that you want to extract. Class can be null.
     * @return empty list if class is null, otherwise return its fields.
     */
    @NotNull
    public static List<Field> fieldsOf(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            return AList.emptyList();
        }
        final List<Class<?>> classes = ancestorClassesOf(clazz);
        return classes.stream().map(Class::getDeclaredFields).flatMap(Stream::of).collect(Collectors.toList());
    }
    
    /**
     * Find all non-static fields of a class. Those fields including private and public fields in
     * that one class and its ancestors.
     * @param clazz The class that you want to extract. Class can be null.
     * @return empty list if class is null, otherwise return its fields.
     */
    @NotNull
    public static List<Field> nonStaticFieldsOf(@Nullable final Class<?> clazz) {
        return fieldsOf(clazz)
            .stream()
            .filter(field -> !Modifier.isStatic(field.getModifiers()))
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
            return AList.emptyList();
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
        return ASet.setOf(fieldNamesOf(clazz));
    }
    
    /**
     * Find all annotations of a class and its ancestors.
     * @param clazz The class that you want to extract. Class can be null.
     * @return empty list if class is null. Otherwise, return annotations of class.
     */
    @NotNull
    public static List<Annotation> annotationsOf(@Nullable final Class<?> clazz) {
        if (clazz == null){
            return AList.emptyList();
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
            return AList.emptyList();
        }
        return AList.listOf(field.getDeclaredAnnotations());
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
        return findField(fieldName, clazz).map(field -> AList.listOf(field.getAnnotations())).orElseGet(AList::emptyList);
    }
    
    /**
     * Find all ancestors of a class including the class itself.
     * @param clazz The class that you want to extract. Class can be null.
     * @return empty list if input is null. Otherwise, return class's ancestor classes.
     */
    @NotNull
    public static List<Class<?>> ancestorClassesOf(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            return AList.emptyList();
        }
        final List<Class<?>> classes = AList.emptyList();
        Class<?> tempClass = clazz;
        while (tempClass != null) {
            classes.add(tempClass);
            classes.addAll(AList.listOf(tempClass.getInterfaces()));
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
            return AList.emptyList();
        }

        return wrapperClasses
            .stream()
            .map(Class::getDeclaredClasses)
            .flatMap(Arrays::stream)
            .filter(clazz -> AReflection.isChildClassOf(parentClass, clazz))
            .map(entity -> AReflection.castToClass(parentClass, entity))
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
     * Cast a object to other type.
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
     * Cast a object to other type.
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

        final Class<?> wrapperClass = primitiveWrapperClassMap.get(className);
        if (wrapperClass == null) {
            throw new IllegalArgumentException(AString.format("Can not find the wrapper class of {}.", className));
        }
        return wrapperClass;
    }
    
    //=================================================// Checkers //=================================================//
    /**
     * Check if a class has annotation or not.
     * @param annotationClass can be null.
     * @param clazz can be null.
     * @return false if input is null or class doesn't have annotation. Otherwise, return true.
     */
    public static boolean hasAnnotation(@Nullable final Class<? extends Annotation> annotationClass, @Nullable final Class<?> clazz) {
        if (annotationClass == null || clazz == null) {
            return false;
        }
        final List<Annotation> annotations = annotationsOf(clazz);
        return annotations.stream().anyMatch(annotation -> annotation.annotationType().equals(annotationClass));
    }
    
    /**
     * Check if certain field contains the annotation or not.
     * @param annotationClass can be null.
     * @param field can be null.
     * @return false if any input is null or field not contains annotation. Otherwise, return true.
     */
    public static boolean hasAnnotation(@Nullable final Class<? extends Annotation> annotationClass, @Nullable final Field field) {
        if (annotationClass == null || field == null) {
            return false;
        }
        return field.getAnnotation(annotationClass) != null;
    }
    
    /**
     * Check if a field is exists in certain class or not.
     * This method will ignore case while checking.
     * @param fieldName can be null.
     * @param clazz can be null.
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
        return clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers());
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
        return type.isPrimitive() || wrapperClasses.contains(type);
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
