package pl.edu.agh.ki.grieg.meta;

import java.util.Map.Entry;
import java.util.Set;

import pl.edu.agh.ki.grieg.utils.NotImplementedException;

public class SimpleTagContainer implements TagSet {

    @Override
    public String get(String tagId) {
        throw new NotImplementedException();
    }

    @Override
    public byte[] getImage(String tagId) {
        throw new NotImplementedException();
    }

    @Override
    public Set<String> tags() {
        throw new NotImplementedException();
    }

    @Override
    public Set<Entry<String, String>> entries() {
        throw new NotImplementedException();
    }

    @Override
    public Set<Entry<String, byte[]>> images() {
        throw new NotImplementedException();
    }

}
