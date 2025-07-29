package org.example.code.config;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Set;

public class FormatterExpressionDialect implements IExpressionObjectDialect {

    private final formatter formatter;

    public FormatterExpressionDialect(formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public String getName() {
        return "Formatter Expression Dialect";
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {

            private final String UTILS_EXPRESSION_NAME = "utils";

            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton(UTILS_EXPRESSION_NAME);
            }

            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                if (UTILS_EXPRESSION_NAME.equals(expressionObjectName)) {
                    return formatter;
                }
                return null;
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }
        };
    }
}