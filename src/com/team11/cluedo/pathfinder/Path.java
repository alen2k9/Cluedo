/*
    Code for the valid path returned by the pathfinder. Stores each of the moves required to get to the target position

    Authors Team11:    Jack Geraghty - 16384181
                       Conor Beenham - 16350851
                       Alen Thomas   - 16333003
*/

package com.team11.cluedo.pathfinder;

import java.util.ArrayList;

public class Path {
    private ArrayList steps = new ArrayList();

    public Path() {}

    public int getLength() {
        return steps.size();
    }

    public Step getStep(int index) {
        return (Step) steps.get(index);
    }

    public ArrayList getSteps() {
        return steps;
    }

    public int getX(int index) {
        return getStep(index).x;
    }

    public int getY(int index) {
        return getStep(index).y;
    }

    public void appendStep(int x, int y) {
        steps.add(new Step(x,y));
    }

    public void prependStep(int x, int y) {
        steps.add(0, new Step(x, y));
    }

    public boolean contains(int x, int y) {
        return steps.contains(new Step(x,y));
    }

    public class Step {
        // The x coordinate at the given step
        private int x;
        // The y coordinate at the given step
        private int y;

        public Step(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int hashCode() {
            return x*y;
        }

        public boolean equals(Object other) {
            if (other instanceof Step) {
                Step o = (Step) other;

                return (o.x == x) && (o.y == y);
            }
            return false;
        }

        public String toString(){
            return ("(" + this.getY() + ", " + this.getX() + ")");
        }
    }
}
