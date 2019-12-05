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

package io.cdap.plugin.marketo.common.api.entities.asset.gen;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that generates schema for entities.
 * This class is not required anymore in near future and not intended to be used by developers...
 * ...but can be used later again if some massive changes to schema will be required.
 *
 * Requires guava.
 */
public class Generate {
  public static void main(String... args) throws IOException {
    System.out.println("@SuppressWarnings(\"DuplicatedCode\")");
    System.out.println("public class EntitySchemaHelper {");

    // entity name to schema
    System.out.println("  public static Schema schemaForEntityName(String entityName) {");
    System.out.println("    switch(entityName) {");
    getEntityClasses().forEach(aClass -> {
      System.out.println("      case \"" + aClass.getSimpleName() + "\":");
      System.out.println("        return " + generateSchemaGetterMethod(aClass) + "();");
    });
    System.out.println("      default:\n");
    System.out.println("        throw new IllegalArgumentException(\"Unknown entity name:\" + entityName);");
    System.out.println("    }");
    System.out.println("  }");

    // entity object to structured record
    System.out.println("  public static StructuredRecord structuredRecordFromEntity(String entityName, Object " +
                         "entity, Schema schema) {");
    System.out.println("    StructuredRecord.Builder builder = StructuredRecord.builder(schema);");
    System.out.println("    switch(entityName) {");
    getEntityClasses().forEach(aClass -> {
      System.out.println("      case \"" + aClass.getSimpleName() + "\":");
      System.out.println("        " + aClass.getSimpleName() + " " + aClass.getSimpleName().toLowerCase() + " = ("
                           + aClass.getSimpleName() + ") entity;");
      System.out.println("        if (entity == null) {\n" +
                           "          break;\n" +
                           "        }");
      for (Field field : aClass.getDeclaredFields()) {
        if (isSimpleType(field)) {
          System.out.println("        builder.set(\"" + field.getName() + "\", " + aClass.getSimpleName().toLowerCase()
                               + "." + findGetterMethod(field, aClass) + "());");
        } else if (isComplexType(field)) {
          System.out.println("        builder.set(\"" + field.getName() + "\", EntitySchemaHelper" +
                               ".structuredRecordFromEntity(\n" +
                               "          \"" + field.getType().getSimpleName() + "\",\n" +
                               "          " + aClass.getSimpleName().toLowerCase() + "."
                               + findGetterMethod(field, aClass) + "()" + ",\n" +
                               "          schema.getField(\"" + field.getName() + "\").getSchema().getNonNullable())\n"
                               + "        );");
        } else if (isListType(field)) {
          ParameterizedType integerListType = (ParameterizedType) field.getGenericType();
          Class<?> listType = (Class<?>) integerListType.getActualTypeArguments()[0];
          if (isSimpleType(listType)) {
            System.out.println("        builder.set(\"" + field.getName() + "\", "
                                 + aClass.getSimpleName().toLowerCase() + "." + findGetterMethod(field, aClass)
                                 + "());");
          } else {
            System.out.println("        builder.set(\n" +
                                 "          \"" + field.getName() + "\",\n" +
                                 "          " + aClass.getSimpleName().toLowerCase() + "."
                                 + findGetterMethod(field, aClass) + "().stream()\n" +
                                 "            .map(ent -> EntitySchemaHelper.structuredRecordFromEntity(\n" +
                                 "              \"" + listType.getSimpleName() + "\",\n" +
                                 "              ent,\n" +
                                 "              schema.getField(\"" + field.getName() + "\").getSchema()" +
                                 ".getNonNullable().getComponentSchema()))\n" +
                                 "            .collect(Collectors.toList())\n" +
                                 "        );");
          }
        } else {
          throw new RuntimeException("Unknown field type.");
        }
      }
      System.out.println("        break;");
    });
    System.out.println("      default:\n");
    System.out.println("        throw new IllegalArgumentException(\"Unknown entity name:\" + entityName);");
    System.out.println("    }");
    System.out.println("");
    System.out.println("    return builder.build();");
    System.out.println("  }");

    // individual schemas
    getEntityClasses().forEach(aClass -> {
      System.out.println("  public static Schema " + generateSchemaGetterMethod(aClass) + "() {");
      System.out.println("    List<Schema.Field> fields = new ArrayList<>();");
      for (Field field : aClass.getDeclaredFields()) {
        if (isSimpleType(field)) {
          System.out.println("    fields.add(Schema.Field.of(\"" + field.getName() + "\", "
                               + simpleTypeToCdapType(field) + "));");
        } else if (isComplexType(field)) {
          System.out.println("    fields.add(Schema.Field.of(\"" + field.getName() + "\", " +
                               "Schema.nullableOf(EntitySchemaHelper."
                               + generateSchemaGetterMethod(field.getType()) + "())));");
        } else if (isListType(field)) {
          System.out.println("    fields.add(Schema.Field.of(\"" + field.getName() + "\", "
                               + listTypeToCdapType(field) + "));");
        } else {
          throw new RuntimeException("Unknown field type.");
        }
      }
      System.out.println("    return Schema.recordOf(UUID.randomUUID().toString().replace(\"-\", \"\"), fields);");
      System.out.println("  }");
      System.out.println("");
    });
    System.out.println("}");
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
}
