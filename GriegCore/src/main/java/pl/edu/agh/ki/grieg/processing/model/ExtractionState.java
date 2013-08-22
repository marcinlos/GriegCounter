package pl.edu.agh.ki.grieg.processing.model;

import com.google.common.base.Objects;

public class ExtractionState {
    
    private final float percent;

    public ExtractionState(float percent) {
        this.percent = percent;
    }
    
    public float getPercent() {
        return percent;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof ExtractionState) {
            ExtractionState other = (ExtractionState) o;
            return percent == other.percent;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(percent);
    }

}
