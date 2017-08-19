package com.mdata.thirdparty.dianli.frontend.config.thymeleaf;


import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeDefinition;
import org.thymeleaf.engine.AttributeDefinitions;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.engine.IAttributeDefinitionsAware;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.requestdata.RequestDataValueProcessorUtils;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.standard.util.StandardProcessorUtils;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.util.Validate;
import org.unbescape.html.HtmlEscape;


/**
 * Created by administrator on 17/7/17.
 */

public class ResourceAttributeTagProcessor extends AbstractStandardExpressionAttributeTagProcessor implements IAttributeDefinitionsAware {

    private static final int PRECEDENCE = 10000;
    private String attrName;
    private AttributeDefinition targetAttributeDefinition;
    private static final TemplateMode TEMPLATE_MODE = TemplateMode.HTML;
    private long startTime;


    public void setAttributeDefinitions(final AttributeDefinitions attributeDefinitions) {
        Validate.notNull(attributeDefinitions, "Attribute Definitions cannot be null");
        // We precompute the AttributeDefinition of the target attribute in order to being able to use much
        // faster methods for setting/replacing attributes on the ElementAttributes implementation
        this.targetAttributeDefinition = attributeDefinitions.forName(TEMPLATE_MODE, attrName);
    }

    //attrName src or href
    public ResourceAttributeTagProcessor(final String dialectPrefix, String attrName, long startTime) {

        super(TemplateMode.HTML, dialectPrefix, attrName, PRECEDENCE, true);
        this.attrName = attrName;
        this.startTime = startTime;
    }


    @Override
    protected final void doProcess(
            final ITemplateContext context,
            final IProcessableElementTag tag,
            final AttributeName attributeName, final String attributeValue,
            final Object expressionResult,
            final IElementTagStructureHandler structureHandler) {
        String newAttributeValue = HtmlEscape.escapeHtml4Xml(expressionResult == null ? "" : expressionResult.toString());

        // Let RequestDataValueProcessor modify the attribute value if needed
        newAttributeValue = RequestDataValueProcessorUtils.processUrl(context, newAttributeValue) + "?v=" + startTime;
        if("link".equalsIgnoreCase(tag.getElementCompleteName())){
            attrName="href";
        }
        // Set the real, non prefixed attribute
        StandardProcessorUtils.replaceAttribute(structureHandler, attributeName, this.targetAttributeDefinition, attrName, (newAttributeValue == null ? "" : newAttributeValue));

    }


}
