/*
 * Copyright 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.config.util;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.util.InMemoryResource;

/**
 * @author Luke Taylor
 */
public class InMemoryXmlApplicationContext extends AbstractXmlApplicationContext {
    private static final String BEANS_OPENING =
                    "<b:beans xmlns='http://www.springframework.org/schema/security'\n" +
                    "    xmlns:context='http://www.springframework.org/schema/context'\n" +
                    "    xmlns:b='http://www.springframework.org/schema/beans'\n" +
                    "    xmlns:aop='http://www.springframework.org/schema/aop'\n" +
                    "    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n" +
                    "    xsi:schemaLocation='http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd\n" +
                    "http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd\n" +
                    "http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd\n" +
                    "http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security-";
    private static final String BEANS_CLOSE = "</b:beans>\n";

    Resource inMemoryXml;

    public InMemoryXmlApplicationContext(String xml) {
        this(xml, "3.2", null);
    }

    public InMemoryXmlApplicationContext(String xml, ApplicationContext parent) {
        this(xml, "3.2", parent);
    }

    public InMemoryXmlApplicationContext(String xml, String secVersion, ApplicationContext parent) {
        String fullXml = BEANS_OPENING + secVersion + ".xsd'>\n" + xml + BEANS_CLOSE;
        inMemoryXml = new InMemoryResource(fullXml);
        setAllowBeanDefinitionOverriding(true);
        setParent(parent);
        refresh();
    }

    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory(getInternalParentBeanFactory()) {
            @Override
            protected boolean allowAliasOverriding() {
                return true;
            }
        };
    }

    protected Resource[] getConfigResources() {
        return new Resource[] {inMemoryXml};
    }
}
