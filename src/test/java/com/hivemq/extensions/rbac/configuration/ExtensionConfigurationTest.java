/*
 *
 * Copyright 2019 HiveMQ GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hivemq.extensions.rbac.configuration;

import com.hivemq.extensions.rbac.configuration.entities.ExtensionConfig;
import com.hivemq.extensions.rbac.configuration.entities.PasswordType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;

import static com.hivemq.extensions.rbac.configuration.ExtensionConfiguration.EXTENSION_CONFIG_FILE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class ExtensionConfigurationTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void test_read_extension_configuration() throws Exception {

        final File configFile = new File(temporaryFolder.getRoot(), EXTENSION_CONFIG_FILE_NAME);

        Files.writeString(configFile.toPath(),
                "<extension-configuration><credentials-reload-interval>999</credentials-reload-interval></extension-configuration>");

        final ExtensionConfiguration extensionConfiguration = new ExtensionConfiguration(temporaryFolder.getRoot());

        final ExtensionConfig extensionConfig = extensionConfiguration.getExtensionConfig();

        assertNotNull(extensionConfig);
        assertEquals(999, extensionConfig.getReloadInterval());
    }

    @Test
    public void test_read_extension_file_not_present() throws Exception {

        final ExtensionConfiguration extensionConfiguration = new ExtensionConfiguration(temporaryFolder.getRoot());

        final ExtensionConfig extensionConfig = extensionConfiguration.getExtensionConfig();

        assertNotNull(extensionConfig);
        //check that default values are used
        assertEquals(60, extensionConfig.getReloadInterval());
        assertEquals(PasswordType.HASHED, extensionConfig.getPasswordType());
    }

    @Test
    public void test_read_extension_configuration_invalid_reload_interval() throws Exception {

        final File configFile = new File(temporaryFolder.getRoot(), EXTENSION_CONFIG_FILE_NAME);

        Files.writeString(configFile.toPath(),
                "<extension-configuration><credentials-reload-interval>-1</credentials-reload-interval></extension-configuration>");

        final ExtensionConfiguration extensionConfiguration = new ExtensionConfiguration(temporaryFolder.getRoot());

        final ExtensionConfig extensionConfig = extensionConfiguration.getExtensionConfig();

        assertNotNull(extensionConfig);
        assertEquals(60, extensionConfig.getReloadInterval());
    }

    @Test
    public void test_read_extension_configuration_invalid_pw_type() throws Exception {

        final File configFile = new File(temporaryFolder.getRoot(), EXTENSION_CONFIG_FILE_NAME);

        Files.writeString(configFile.toPath(),
                "<extension-configuration><password-type>ABC</password-type></extension-configuration>");

        final ExtensionConfiguration extensionConfiguration = new ExtensionConfiguration(temporaryFolder.getRoot());

        final ExtensionConfig extensionConfig = extensionConfiguration.getExtensionConfig();

        assertNotNull(extensionConfig);
        assertEquals(PasswordType.HASHED, extensionConfig.getPasswordType());
        assertNull(extensionConfig.getListenerNames());
    }

    @Test
    public void test_read_extension_configuration_existing_listener_names() throws Exception {

        final File configFile = new File(temporaryFolder.getRoot(), EXTENSION_CONFIG_FILE_NAME);

        Files.writeString(configFile.toPath(),
                "<extension-configuration><listener-names><listener-name>listener-1</listener-name><listener-name>listener-2</listener-name></listener-names></extension-configuration>");

        final ExtensionConfiguration extensionConfiguration = new ExtensionConfiguration(temporaryFolder.getRoot());

        final ExtensionConfig extensionConfig = extensionConfiguration.getExtensionConfig();

        assertNotNull(extensionConfig);
        assertEquals(2, extensionConfig.getListenerNames().size());
    }

}
