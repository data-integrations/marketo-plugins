package io.cdap.plugin.marketo.source.batch;

import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.marketo.common.api.Marketo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Various methods to deal with schema and record.
 */
public class MarketoReportingSchemaHelper {
  public static Schema getActivitySchema() {
    List<Schema.Field> fields = Marketo.ACTIVITY_FIELDS.stream()
      .map(s -> Schema.Field.of(s, Schema.nullableOf(Schema.of(Schema.Type.STRING))))
      .collect(Collectors.toList());
    return Schema.recordOf("activityRecord", fields);
  }

  public static Schema getSchema(MarketoReportingSourceConfig config) {
    switch (config.getReportType()) {
      case LEADS:
        List<Schema.Field> fields = config.getMarketo().describeLeads().stream().map(
          leadAttribute -> {
            if (leadAttribute.getRest() != null) {
              return Schema.Field.of(leadAttribute.getRest().getName(),
                                     Schema.nullableOf(Schema.of(Schema.Type.STRING)));
            } else {
              return null;
            }
          }
        ).filter(Objects::nonNull).collect(Collectors.toList());

        return Schema.recordOf("LeadsRecord", fields);
      case ACTIVITIES:
        return getActivitySchema();
    }
    throw new IllegalArgumentException("Failed to get schema for type " + config.getReportType());
  }

  public static StructuredRecord getRecord(Schema schema, Map<String, String> fields) {
    StructuredRecord.Builder builder = StructuredRecord.builder(schema);
    schema.getFields().forEach(
      field -> {
        if (fields.containsKey(field.getName())) {
          builder.set(field.getName(), fields.get(field.getName()));
        }
      }
    );
    return builder.build();
  }
}
