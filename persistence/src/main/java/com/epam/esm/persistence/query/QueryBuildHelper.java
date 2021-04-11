package com.epam.esm.persistence.query;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class QueryBuildHelper {
    private static final Pattern UPPER_CASE_SYMBOL_PATTERN = Pattern.compile("[A-Z]");
    private static final String ANY_STRING_REGEX = "%";

    public String buildInFilteringQuery(String columnName, int amount) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(columnName).append(" IN(");
        for (int i = 0; i < amount; i++) {
            if (i != 0) {
                queryBuilder.append(",");
            }
            queryBuilder.append("?");
        }
        queryBuilder.append(") ");
        return queryBuilder.toString();
    }

    public String buildUpdateColumnsQuery(Set<String> columns) {
        StringBuilder queryBuilder = new StringBuilder();
        boolean isFirstElement = true;
        for (String column : columns) {
            if (!isFirstElement) {
                queryBuilder.append(", ");
            } else {
                isFirstElement = false;
            }
            queryBuilder.append(column);
            queryBuilder.append("=?");
        }
        return queryBuilder.toString();
    }

    public String buildSortingQuery(SortParamsContext sortParameters) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("ORDER BY ");
        List<String> sortColumns = convertToDBFields(sortParameters.getSortColumns());
        List<String> orderTypes = sortParameters.getOrderTypes();
        for (int i = 0; i < sortColumns.size(); i++) {
            if (i != 0) {
                queryBuilder.append(", ");
            }
            queryBuilder.append(sortColumns.get(i)).append(" ");
            queryBuilder.append(i < orderTypes.size() ? orderTypes.get(i) : "ASC");
        }
        return queryBuilder.toString();
    }

    private List<String> convertToDBFields(List<String> javaFields) {
        List<String> DBFields = new ArrayList<>();
        javaFields.forEach(fieldName -> {
            Matcher matcher = UPPER_CASE_SYMBOL_PATTERN.matcher(fieldName);
            while (matcher.find()) {
                String matchedSymbol = matcher.group();
                fieldName = fieldName.replaceAll(matchedSymbol, "_" + matchedSymbol.toLowerCase());
            }
            DBFields.add(fieldName);
        });
        return DBFields;
    }

    public String buildRegexValue(String value) {
        return String.format("%s%s%s", ANY_STRING_REGEX, value, ANY_STRING_REGEX);
    }
}
