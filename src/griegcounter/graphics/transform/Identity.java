package griegcounter.graphics.transform;

import griegcounter.graphics.Point;

class Identity implements Transform {
    
    @Override
    public Point apply(Point p) {
        return p;
    }

}
