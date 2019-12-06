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

package io.cdap.plugin.marketo.source.batch.entity;

import com.google.common.collect.ImmutableList;
import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.marketo.common.api.entities.asset.Email;
import io.cdap.plugin.marketo.common.api.entities.asset.EmailCCField;
import io.cdap.plugin.marketo.common.api.entities.asset.FolderDescriptor;
import io.cdap.plugin.marketo.common.api.entities.asset.Program;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class EntityHelperTest {
  @Test
  public void testNestedRecords() {
    Schema programSchema = EntityHelper.getProgramSchema();
    Program program = Program.builder().folder(new FolderDescriptor("1234", "Hello", null))
      .build();
    StructuredRecord programRecord = EntityHelper.structuredRecordFromEntity("Program", program,
                                                                             programSchema);

    Assert.assertEquals(programRecord.<StructuredRecord>get("folder").<String>get("id"), "1234");

    Schema emailSchema = EntityHelper.getEmailSchema();
    Email email = Email.builder().ccFields(ImmutableList.of(
      new EmailCCField("attr1", "cc1", "cc 1", "cc1"),
      new EmailCCField("attr2", "cc2", "cc 2", "cc2")
      )).build();
    StructuredRecord emailRecord = EntityHelper.structuredRecordFromEntity("Email", email,
                                                                           emailSchema);

    List<StructuredRecord> ccRecords = emailRecord.<List<StructuredRecord>>get("ccFields");
    Assert.assertEquals(2, ccRecords.size());
    Assert.assertEquals(ccRecords.get(0).<String>get("attributeId"), "attr1");
  }
}
