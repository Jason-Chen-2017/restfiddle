package com.restfiddle.handler;

import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.restfiddle.controller.rest.AssertionController;
import com.restfiddle.dto.AssertionDTO;
import com.restfiddle.dto.BodyAssertDTO;
import com.restfiddle.dto.RfResponseDTO;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssertHandler {

    Logger logger = LoggerFactory.getLogger(AssertHandler.class);
    @Autowired
    private AssertionController assertionController;

    public void runAssert(RfResponseDTO rfResponseDTO, String nodeId) {
        AssertionDTO assertionDTO = rfResponseDTO.getAssertionDTO();
        if (assertionDTO == null) { return; }

        List<BodyAssertDTO> bodyAssertDTOs = assertionDTO.getBodyAssertDTOs();
        if (bodyAssertDTOs == null) { return; }

        try {
            BodyAssertTool tool = new BodyAssertTool(rfResponseDTO.getBody());
            for (BodyAssertDTO bodyAssertDTO : bodyAssertDTOs) {
                tool.doAssert(bodyAssertDTO);
            }
            assertionController.update(nodeId, assertionDTO);
        } catch (Exception e) {
            logger.debug("Body is not json");
        }

        return;
    }

    private class BodyAssertTool {
        Object body;

        public BodyAssertTool(String body) {
            this.body = Configuration.defaultConfiguration().jsonProvider().parse(body);
        }

        @SuppressWarnings("unchecked")
        public void doAssert(BodyAssertDTO bodyAssertDTO) {
            String propertyName = bodyAssertDTO.getPropertyName();
            if (propertyName.startsWith("[")) {
                propertyName = "$" + propertyName;
            } else {
                propertyName = "$." + propertyName;
            }

            boolean success = false;

            try {
                Object actualValue = JsonPath.read(body, propertyName);
                bodyAssertDTO.setActualValue(actualValue.toString());
                if (actualValue instanceof Number) { // 数字
                    success = evaluate(bodyAssertDTO.getExpectedValue(), bodyAssertDTO.getComparator(),
                        (Number)actualValue);
                } else if (actualValue instanceof String) {//字符串
                    success = evaluate(bodyAssertDTO.getExpectedValue(), bodyAssertDTO.getComparator(),
                        (String)actualValue);
                } else if (actualValue instanceof Map) {//Map
                    JSONObject json = new JSONObject((Map<String, ?>)actualValue);
                    bodyAssertDTO.setActualValue(json.toString());
                    success = evaluate(bodyAssertDTO.getExpectedValue(), bodyAssertDTO.getComparator(), json);
                } else if (actualValue instanceof List) {//数组
                    success = evaluate(bodyAssertDTO.getExpectedValue(), bodyAssertDTO.getComparator(),
                        (List<Object>)actualValue);
                }
            } catch (Exception e) {
                logger.debug("propery not found");
            }

            bodyAssertDTO.setSuccess(success);
        }

        private boolean evaluate(String expectedValue, String comparator, String actualValue) {
            boolean result = false;

            switch (comparator) {
                case "=":
                    result = actualValue.equals(expectedValue);
                    break;
                case "!=":
                    result = !actualValue.equals(expectedValue);
                    break;
                case "Contains":
                    result = actualValue.contains(expectedValue);
                    break;
                case "! Contains":
                    result = !actualValue.contains(expectedValue);
                    break;
                default:
                    break;
            }

            return result;
        }

        private boolean evaluate(String expectedValue, String comparator, Number actualValue) {
            boolean result = false;

            double expected = Double.parseDouble(expectedValue);
            double actual = actualValue.doubleValue();

            switch (comparator) {
                case "=":
                    result = actual == expected;
                    break;
                case "!=":
                    result = actual != expected;
                    break;
                case "<":
                    result = actual < expected;
                    break;
                case "<=":
                    result = actual <= expected;
                    break;
                case ">":
                    result = actual > expected;
                    break;
                case ">=":
                    result = actual >= expected;
                    break;
                default:
                    break;
            }

            return result;
        }

        private boolean evaluate(String expectedValue, String comparator, JSONObject actualValue) {
            boolean result = false;

            switch (comparator) {
                case "Contains Key":
                    result = actualValue.containsKey(expectedValue);
                    break;
                case "! Contains Key":
                    result = !actualValue.containsKey(expectedValue);
                    break;
                case "Contains Value":
                    result = actualValue.containsValue(expectedValue);
                    break;
                case "! Contains Value":
                    result = !actualValue.containsValue(expectedValue);
                    break;
                default:
                    break;

            }

            return result;
        }

        private boolean evaluate(String expectedValue, String comparator, List<Object> actualValue) {
            boolean result = false;
            if (comparator.equals("Contains Value")) {
                result = actualValue.contains(expectedValue);
            } else if (comparator.equals("! Contains Value")) {
                result = !actualValue.contains(expectedValue);
            }
            return result;
        }
    }
}
