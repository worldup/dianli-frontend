package com.mdata.thirdparty.dianli.frontend.config.thymeleaf;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by administrator on 17/7/17.
 */
public class ResourcesHrefDialect extends AbstractProcessorDialect {

    private final static String attrName="href";
    private long startTime;
    public ResourcesHrefDialect( long startTime) {
        super(
                "Resources Dialect",    // Dialect name
                "rs",            // Dialect prefix (hello:*)
                1000);              // Dialect precedence
        this.startTime=startTime;
    }


    /*
     * Initialize the dialect's processors.
     *
     * Note the dialect prefix is passed here because, although we set
     * "hello" to be the dialect's prefix at the constructor, that only
     * works as a default, and at engine configuration time the user
     * might have chosen a different prefix to be used.
     */
    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new ResourceAttributeTagProcessor(dialectPrefix,attrName,startTime));
        return processors;
    }


}