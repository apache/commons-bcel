package org.jd.core.v1;

public class SealedDemo {

    sealed interface Shape permits Circle, Rectangle {
        double area();
    }

    static final class Circle implements Shape {
        private final double radius;

        Circle(double radius) {
            this.radius = radius;
        }

        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    static non-sealed class Rectangle implements Shape {
        private final double width;
        private final double height;

        Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public double area() {
            return width * height;
        }
    }

    double sealedSwitch(Shape shape) {
        return switch (shape) {
            case Circle circle -> circle.area();
            case Rectangle rectangle -> rectangle.area();
        };
    }
}
