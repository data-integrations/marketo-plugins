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

package io.cdap.plugin.marketo.common.api.entities;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Deserializer for warning messages that can handle simple string warning messages and code+message warnings.
 */
public class WarningDeserializer implements JsonDeserializer<Warning> {
  @Override
  public Warning deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
    throws JsonParseException {
    if (jsonElement.isJsonPrimitive()) {
      return new Warning(-1, jsonElement.getAsString());
    } else if (jsonElement.isJsonObject()) {
      JsonObject obj = jsonElement.getAsJsonObject();
      int code = -1;
      String message = "";

      if (obj.has("code")) {
        code = obj.get("code").getAsInt();
      }

      if (obj.has("message")) {
        message = obj.get("message").getAsString();
      }

      return new Warning(code, message);
    } else {
      throw new RuntimeException("Failed to deserialize warning message: " + jsonElement.toString());
    }
  }
}
