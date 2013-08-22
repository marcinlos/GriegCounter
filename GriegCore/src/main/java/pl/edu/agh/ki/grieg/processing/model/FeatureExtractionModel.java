package pl.edu.agh.ki.grieg.processing.model;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.features.FeaturesListener;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.model.Models;
import pl.edu.agh.ki.grieg.model.SimpleModel;
import pl.edu.agh.ki.grieg.util.ProgressAdapter;

public class FeatureExtractionModel extends ProgressAdapter implements
        FeaturesListener {

    private static final Logger logger = LoggerFactory
            .getLogger(FeatureExtractionModel.class);

    private final SimpleModel<Float> model = Models.simple(0.0f);

    private final ActionFilter filter;

    public FeatureExtractionModel(TimeUnit unit, long delta) {
        filter = new FixedRateActionFilter(unit, delta);
    }

    @Override
    public void extracted(String name, Object value) {
        logger.debug("Extracted: {} = {}", name, value);
    }

    @Override
    public void progress(float percent) {
        if (filter.perform()) {
            model.update(percent);
        }
    }

    public Model<Float> getModel() {
        return model;
    }

}
