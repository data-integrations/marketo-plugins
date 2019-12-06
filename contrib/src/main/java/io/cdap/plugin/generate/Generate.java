/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.generate;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.marketo.common.api.entities.asset.gen.Entity;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;

/**
 * Class that generates schema for entities.
 * This class is not required anymore in near future and not intended to be used by end users...
 * ...but can be used later again if some massive changes to schema will be required.
 * <p>
 * Requires guava.
 */
public class Generate {
  public static void main(String... args) throws IOException {
    List<MethodSpec> methods = new ArrayList<>();

    MethodSpec.Builder schemaForEntityNameBuilder = MethodSpec.methodBuilder("schemaForEntityName")
      .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
      .returns(Schema.class)
      .addParameter(String.class, "entityName");

    schemaForEntityNameBuilder.beginControlFlow("switch(entityName)");
    getEntityClasses().forEach(aClass -> {
      schemaForEntityNameBuilder.beginControlFlow("case $S:", aClass.getSimpleName());
      schemaForEntityNameBuilder.addStatement("return $L()", generateSchemaGetterMethod(aClass));
      schemaForEntityNameBuilder.endControlFlow();
    });
    schemaForEntityNameBuilder.beginControlFlow("default:");
    schemaForEntityNameBuilder.addStatement(
      "throw new IllegalArgumentException(\"Unknown entity name:\" + entityName)");
    schemaForEntityNameBuilder.endControlFlow();
    schemaForEntityNameBuilder.endControlFlow();

    methods.add(schemaForEntityNameBuilder.build());

    // individual schemas
    getEntityClasses().forEach(aClass -> {
      MethodSpec.Builder entitySchemaGenBuilder = MethodSpec.methodBuilder(generateSchemaGetterMethod(aClass))
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .returns(Schema.class)
        .addStatement("$T<Schema.Field> fields = new $T<>()", List.class, ArrayList.class);
      for (Field field : aClass.getDeclaredFields()) {
        if (isSimpleType(field)) {
          entitySchemaGenBuilder.addStatement("fields.add(Schema.Field.of($S, $L))", field.getName(),
                                              simpleTypeToCdapType(field));
        } else if (isComplexType(field)) {
          entitySchemaGenBuilder.addStatement(
            "fields.add(Schema.Field.of($S, Schema.nullableOf(EntitySchemaHelper.$L())))",
            field.getName(), generateSchemaGetterMethod(field.getType()));
        } else if (isListType(field)) {
          entitySchemaGenBuilder.addStatement(
            "fields.add(Schema.Field.of($S, $L))",
            field.getName(), listTypeToCdapType(field));
        } else {
          throw new RuntimeException("Unknown field type.");
        }
      }
      entitySchemaGenBuilder.addStatement(
        "return Schema.recordOf(UUID.randomUUID().toString().replace(\"-\", \"\"), fields)");
      methods.add(entitySchemaGenBuilder.build());
    });

    // entity object to structured record
    MethodSpec.Builder recordFromEntityBuilder = MethodSpec.methodBuilder("structuredRecordFromEntity")
      .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
      .returns(StructuredRecord.class)
      .addParameter(String.class, "entityName")
      .addParameter(Object.class, "entity")
      .addParameter(Schema.class, "schema")
      .addStatement("StructuredRecord.Builder builder = StructuredRecord.builder(schema)")
      .beginControlFlow("switch(entityName)");
    getEntityClasses().forEach(aClass -> {
      recordFromEntityBuilder.beginControlFlow("case $S:", aClass.getSimpleName());
      recordFromEntityBuilder.addStatement("$T $L = ($L) entity",
                                           aClass, aClass.getSimpleName().toLowerCase(),
                                           aClass.getSimpleName());

      recordFromEntityBuilder.beginControlFlow("if (entity == null)");
      recordFromEntityBuilder.addStatement("break");
      recordFromEntityBuilder.endControlFlow();
      for (Field field : aClass.getDeclaredFields()) {
        if (isSimpleType(field)) {
          recordFromEntityBuilder.addStatement("builder.set($S, $L.$L())",
                                               field.getName(),
                                               aClass.getSimpleName().toLowerCase(),
                                               findGetterMethod(field, aClass));
        } else if (isComplexType(field)) {
          recordFromEntityBuilder.addStatement("builder.set($S, EntitySchemaHelper.structuredRecordFromEntity(\n" +
                                                 " $S,\n" +
                                                 " $L.$L(),\n" +
                                                 " schema.getField($S).getSchema().getNonNullable()))",
                                               field.getName(),
                                               field.getType().getSimpleName(),
                                               aClass.getSimpleName().toLowerCase(),
                                               findGetterMethod(field, aClass),
                                               field.getName());
        } else if (isListType(field)) {
          ParameterizedType integerListType = (ParameterizedType) field.getGenericType();
          Class<?> listType = (Class<?>) integerListType.getActualTypeArguments()[0];
          if (isSimpleType(listType)) {
            recordFromEntityBuilder.addStatement("builder.set($S, $L.$L())",
                                                 field.getName(),
                                                 aClass.getSimpleName().toLowerCase(),
                                                 findGetterMethod(field, aClass));
          } else {
            recordFromEntityBuilder.addStatement(
              "builder.set(\n" +
                "  $S,\n" +
                "  $L.$L().stream()\n" +
                "    .map(ent -> EntitySchemaHelper.structuredRecordFromEntity(\n" +
                "      $S,\n" +
                "      ent,\n" +
                "      schema.getField($S).getSchema().getNonNullable().getComponentSchema()))\n" +
                "    .collect(Collectors.toList())\n" +
                ")",
              field.getName(), aClass.getSimpleName().toLowerCase(), findGetterMethod(field, aClass),
              listType.getSimpleName(), field.getName()
            );
          }
        } else {
          throw new RuntimeException("Unknown field type.");
        }
      }
      recordFromEntityBuilder.addStatement("break");
      recordFromEntityBuilder.endControlFlow();
    });

    recordFromEntityBuilder.beginControlFlow("default:");
    recordFromEntityBuilder.addStatement(
      "throw new IllegalArgumentException(\"Unknown entity name:\" + entityName)");
    recordFromEntityBuilder.endControlFlow();
    recordFromEntityBuilder.endControlFlow(); // switch statement
    recordFromEntityBuilder.addStatement("return builder.build()");
    methods.add(recordFromEntityBuilder.build());

    TypeSpec.Builder entityTypeEnumBuilder = TypeSpec.enumBuilder("EntityType")
      .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
      .addField(String.class, "value", Modifier.PRIVATE, Modifier.FINAL);

    getTopLevelEntityClasses().forEach(aClass -> {
      entityTypeEnumBuilder.addEnumConstant(
        aClass.getSimpleName().toUpperCase(),
        TypeSpec.anonymousClassBuilder("$S", aClass.getSimpleName()).build()
      );
    });

    entityTypeEnumBuilder.addMethod(
      MethodSpec.constructorBuilder()
        .addParameter(String.class, "value")
        .addStatement("this.$N = $N", "value", "value")
        .build()
    );

    entityTypeEnumBuilder.addMethod(
      MethodSpec.methodBuilder("fromString")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .returns(ClassName.bestGuess("EntityType"))
        .addParameter(String.class, "value")
        .beginControlFlow("for (EntityType entityType : EntityType.values())")
        .beginControlFlow("if (entityType.value.equals(value))")
        .addStatement("return entityType")
        .endControlFlow()
        .endControlFlow()
        .addStatement("throw new IllegalArgumentException(\"Unknown entity type type: \" + value)")
        .build()
    );

    TypeSpec schemaHelperSpec = TypeSpec.classBuilder("EntitySchemaHelper")
      .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
      .addMethods(methods)
      .addType(entityTypeEnumBuilder.build())
      .build();

    JavaFile javaFile = JavaFile.builder("io.cdap.plugin.marketo.source.batch.entity", schemaHelperSpec)
      .skipJavaLangImports(true)
      .build();
    System.out.println(javaFile.toString());
  }

  private static final List<Class> SIMPLE_TYPES = ImmutableList.of(Boolean.class, Integer.class, int.class,
                                                                   boolean.class, String.class);

  private static final List<Class> COLLECTION_TYPES = ImmutableList.of(List.class);

  public static boolean isSimpleType(Field field) {
    return SIMPLE_TYPES.contains(field.getType());
  }

  public static boolean isSimpleType(Class cls) {
    return SIMPLE_TYPES.contains(cls);
  }

  public static String generateSchemaGetterMethod(Class cls) {
    return "get" + capitalize(cls.getSimpleName()) + "Schema";
  }

  public static boolean isListType(Field field) {
    return COLLECTION_TYPES.contains(field.getType());
  }

  public static boolean isComplexType(Field field) {
    return !SIMPLE_TYPES.contains(field.getType()) && !COLLECTION_TYPES.contains(field.getType());
  }

  public static String simpleTypeToCdapType(Field field) {
    if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
      return "Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))";
    } else if (field.getType().equals(String.class)) {
      return "Schema.nullableOf(Schema.of(Schema.Type.STRING))";
    } else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
      return "Schema.nullableOf(Schema.of(Schema.Type.INT))";
    } else {
      throw new RuntimeException("Unsupported simple type");
    }
  }

  public static String listTypeToCdapType(Field field) {
    ParameterizedType integerListType = (ParameterizedType) field.getGenericType();
    Class<?> listType = (Class<?>) integerListType.getActualTypeArguments()[0];
    if (listType.equals(Boolean.class) || listType.equals(boolean.class)) {
      return "SSchema.nullableOf(Schema.arrayOf(Schema.of(Schema.Type.BOOLEAN)))";
    } else if (listType.equals(String.class)) {
      return "Schema.nullableOf(Schema.arrayOf(Schema.of(Schema.Type.STRING)))";
    } else if (listType.equals(Integer.class) || listType.equals(int.class)) {
      return "Schema.nullableOf(Schema.arrayOf(Schema.of(Schema.Type.INT)))";
    } else {
      return "Schema.nullableOf(Schema.arrayOf(EntitySchemaHelper." + generateSchemaGetterMethod(listType) + "()))";
    }
  }

  public static String findGetterMethod(Field field, Class cls) {
    if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
      String getterName = "get" + capitalize(field.getName());
      try {
        cls.getMethod(getterName);
        return getterName;
      } catch (NoSuchMethodException e) {
        if (field.getName().startsWith("is")) {
          String alternateGetter = "get" + capitalize(field.getName().substring(2));
          try {
            cls.getMethod(alternateGetter);
            return alternateGetter;
          } catch (NoSuchMethodException altE) {
            throw new RuntimeException(altE);
          }
        }
      }
    } else {
      String getterName = "get" + capitalize(field.getName());
      try {
        cls.getMethod(getterName);
        return getterName;
      } catch (NoSuchMethodException altE) {
        throw new RuntimeException(altE);
      }
    }
    throw new RuntimeException();
  }

  public static String capitalize(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  public static List<Class> getEntityClasses() throws IOException {
    return ClassPath.from(Generate.class.getClassLoader())
      .getTopLevelClasses("io.cdap.plugin.marketo.common.api.entities.asset").stream()
      .map(ClassPath.ClassInfo::load)
      .filter(Generate::isEntity)
      .sorted(Comparator.comparing(Class::getSimpleName))
      .collect(Collectors.toList());
  }

  public static List<Class> getTopLevelEntityClasses() throws IOException {
    return ClassPath.from(Generate.class.getClassLoader())
      .getTopLevelClasses("io.cdap.plugin.marketo.common.api.entities.asset").stream()
      .map(ClassPath.ClassInfo::load)
      .filter(Generate::isTopLevelEntity)
      .sorted(Comparator.comparing(Class::getSimpleName))
      .collect(Collectors.toList());
  }

  public static boolean isEntity(Class cls) {
    for (Annotation a : cls.getAnnotations()) {
      if (a.annotationType().equals(Entity.class)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isTopLevelEntity(Class cls) {
    for (Annotation a : cls.getAnnotations()) {
      if (a.annotationType().equals(Entity.class)) {
        Entity e = (Entity) a;
        if (e.topLevel()) {
          return true;
        }
      }
    }
    return false;
  }
}
