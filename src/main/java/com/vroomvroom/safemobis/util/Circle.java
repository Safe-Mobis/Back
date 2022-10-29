package com.vroomvroom.safemobis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.lang.Math.*;
import static java.lang.Math.pow;

@Getter
@AllArgsConstructor
public class Circle {
    private double x;
    private double y;
    private double radius;

    public static Circle of(double x, double y, double radius) {
        return new Circle(x, y, radius);
    }

    public boolean isPositionInCircle(double x_prime, double y_prime) {
        return pow(radius, 2) >= (pow(x - x_prime, 2) + pow(y - y_prime, 2));
    }

    public boolean isOverlap(Circle circle) {
        double distanceBetweenCenter = sqrt(pow(x - circle.getX(), 2) + pow(y - circle.getY(), 2));
        double distanceRadius = radius + circle.getRadius();
        return !(distanceBetweenCenter > distanceRadius);
    }
}
