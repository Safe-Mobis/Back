package com.vroomvroom.safemobis.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "Invalid Input Value"),
    METHOD_NOT_FOUND(405, "Method Not Found"),
    ENTITY_NOT_FOUND(404, "Entity Not Found"),
    SERVER_ERROR(500, "Server Error"),
    INVALID_TYPE_VALUE(400, "Invalid Type Value"),
    INVALID_REQUEST_PARAMETER(400, "Invalid Request Parameter Given."),
    PLAN_ALREADY_EXIST(400, "Diet Already Exists."),
    ENTITY_ALREADY_EXIST(400, "Entity Already Exists."),
    TOO_MUCH_UNCHECKED_MEAL(500, "Too Much Unchecked Meals"),
    INTAKE_NEGATIVE_CALORIES(400, "Intake Negative Calories"),
    SPRINT_OVERLAP(400, "Sprint Overlap");

    private final int status;
    private final String error;
}
