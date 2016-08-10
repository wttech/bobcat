/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.cognifide.qa.bb.aem.dialog.configurer;

import java.util.ArrayList;
import java.util.List;

import com.cognifide.qa.bb.aem.dialog.classic.field.AemFieldset;
import com.cognifide.qa.bb.aem.dialog.classic.field.AemList;
import com.cognifide.qa.bb.aem.ui.AemDialog;
import com.cognifide.qa.bb.aem.ui.AemDialogFieldResolver;
import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.utils.XpathUtils;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * This class configures components using data tables retrieved from scenarios. Use ComponentConfigurerFactory
 * to create instances of this class.
 */
@PageObject
@Frame("$cq")
public class ComponentConfigurer {

  private enum NestedFieldTypes {
    MULTIFIELD("multifield"), FIELDSET("fieldset");

    private final String type;

    NestedFieldTypes(String type) {
      this.type = type;
    }

    /**
     * Returns NestedFieldTypes for name
     *
     * @param type name of NestedFieldTypes
     * @return NestedFieldTypes
     */
    public static NestedFieldTypes getInstance(String type) {
      for (NestedFieldTypes fieldType : values()) {
        if (fieldType.type.equals(type)) {
          return fieldType;
        }
      }
      throw new IllegalArgumentException("Type: " + type + " defined in the table is incorrect");
    }
  }

  @Inject
  private AemDialogFieldResolver dialogFieldResolver;

  @Inject
  private DialogFieldMap dialogFieldMap;

  private final AemDialog aemDialog;

  /**
   * Constructs ComponentConfigurer. Initializes aemDialog field. Don't call it manually. Use the factory.
   *
   * @param aemDialog argument provided for {@link ComponentConfigurerFactory}
   */
  @Inject
  public ComponentConfigurer(@Assisted AemDialog aemDialog) {
    this.aemDialog = aemDialog;
  }

  /**
   * Updates fields in a dialog with data from a single row of the data table, represented by a list of
   * ConfigurationEntries.
   *
   * @param config list of {@link ConfigurationEntry}
   */
  public void configureDialog(List<ConfigurationEntry> config) {
    String currentTab = null;

    for (ConfigurationEntry entry : config) {
      currentTab = changeTab(entry.getTab(), currentTab);

      if (isNestedComponentDefined(entry)) {
        NestedFieldTypes type = parseType(entry);
        switch (type) {
          case FIELDSET:
            FieldsetConfig fieldsetConfig = new FieldsetConfig(entry, dialogFieldMap);
            setFieldsetValue(fieldsetConfig);
            break;
          case MULTIFIELD:
            MultifieldConfig multifieldConfig = new MultifieldConfig(entry, dialogFieldMap);
            setMultifieldValue(multifieldConfig);
            break;
          default:
            return;
        }
      } else {
        setDialogValue(entry);
      }
    }
  }

  /**
   * Get fields configuration based on list of ConfigurationEntries.
   *
   * @param config list of {@link ConfigurationEntry}
   * @return dialog configuration as list of ConfigurationEntry objects
   */
  public List<ConfigurationEntry> getDialogConfiguration(List<ConfigurationEntry> config) {
    String currentTab = null;
    List<ConfigurationEntry> dialogConfiguration = new ArrayList<>();
    for (ConfigurationEntry entry : config) {
      currentTab = changeTab(entry.getTab(), currentTab);
      if (isNestedComponentDefined(entry)) {
        NestedFieldTypes type = parseType(entry);
        switch (type) {
          case FIELDSET:
            ConfigurationEntry fieldsetConfigurationEntry = getFieldsetConfigurationEntry(entry);
            dialogConfiguration.add(fieldsetConfigurationEntry);
            break;
          case MULTIFIELD:
            ConfigurationEntry multifieldConfigurationEntry =
                getMultifieldConfigurationEntry(entry);
            dialogConfiguration.add(multifieldConfigurationEntry);
            break;
          default:
            throw new IllegalArgumentException("Incorrect field type provided");
        }
      } else {
        ConfigurationEntry fieldConfigurationEntry = new ConfigurationEntry(
            entry.getTab(), entry.getType(), entry.getLabel(), getDialogValue(entry));
        dialogConfiguration.add(fieldConfigurationEntry);
      }
    }
    return dialogConfiguration;
  }

  private ConfigurationEntry getFieldsetConfigurationEntry(ConfigurationEntry entry) {
    FieldsetConfig fieldsetConfig = new FieldsetConfig(entry, dialogFieldMap);
    return new ConfigurationEntry(
        entry.getTab(), entry.getType(), entry.getLabel(),
        getFieldSetValue(fieldsetConfig));
  }

  private ConfigurationEntry getMultifieldConfigurationEntry(ConfigurationEntry entry) {
    MultifieldConfig multifieldConfig = new MultifieldConfig(entry, dialogFieldMap);
    return new ConfigurationEntry(
        entry.getTab(), entry.getType(), entry.getLabel(),
        getMultifieldValue(multifieldConfig));
  }

  private void setDialogValue(ConfigurationEntry entry) {
    Class<?> fieldClass = dialogFieldMap.getField(entry.getType());
    aemDialog.setValueOfItemInCurrentTab(entry.getValue(), entry.getLabel(), fieldClass);
  }

  private String getDialogValue(ConfigurationEntry entry) {
    Class<?> fieldClass = dialogFieldMap.getField(entry.getType());
    return aemDialog.getValueOfItemInCurrentTab(entry.getLabel(), fieldClass);
  }

  private void setFieldsetValue(FieldsetConfig fieldsetConfig) {
    AemFieldset fieldset = dialogFieldResolver.getFieldByXpath(
        String.format(".//fieldset/legend/span[text()=%s]/../..",
            XpathUtils.quote(fieldsetConfig.getFieldsetLabel())), AemFieldset.class).expand();
    Class<?> fieldClass = fieldsetConfig.getFieldType();
    fieldset
        .setItemValue(fieldsetConfig.getFieldValue(), fieldsetConfig.getFieldLabel(), fieldClass);
  }

  private String getFieldSetValue(FieldsetConfig fieldsetConfig) {
    AemFieldset fieldset = dialogFieldResolver.getFieldByXpath(
        String.format(".//fieldset/legend/span[text()=%s]/../..",
            XpathUtils.quote(fieldsetConfig.getFieldsetLabel())), AemFieldset.class).expand();
    Class<?> fieldClass = fieldsetConfig.getFieldType();
    return fieldset.getItemValue(fieldsetConfig.getFieldLabel(), fieldClass);
  }

  private void setMultifieldValue(MultifieldConfig multifieldConfig) {
    AemList multifield =
        dialogFieldResolver.getField(multifieldConfig.getMultiFieldLabel(), AemList.class);

    Class<?> fieldClass = multifieldConfig.getFieldType();
    multifield.setItemValue(
        multifieldConfig.getFieldValue(),
        multifieldConfig.getFieldIndex(),
        multifieldConfig.getFieldLabel(),
        fieldClass);
  }

  private String getMultifieldValue(MultifieldConfig multifieldConfig) {
    AemList multifield =
        dialogFieldResolver.getField(multifieldConfig.getMultiFieldLabel(), AemList.class);
    Class<?> fieldClass = multifieldConfig.getFieldType();
    return multifield.getItemValue(
        multifieldConfig.getFieldIndex(),
        multifieldConfig.getFieldLabel(),
        fieldClass);
  }

  private NestedFieldTypes parseType(ConfigurationEntry entry) {
    return NestedFieldTypes.getInstance(entry.getType().split("#")[0]);
  }

  private boolean isNestedComponentDefined(ConfigurationEntry entry) {
    return entry.getType().contains("#");
  }

  private String changeTab(String targetTab, String currentTab) {
    if (!targetTab.equals(currentTab)) {
      aemDialog.clickTab(targetTab);
      return targetTab;
    }
    return currentTab;
  }
}
