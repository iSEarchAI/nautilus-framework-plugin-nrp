package org.nautilus.plugin.nrp.encoding.instance;

import br.otimizes.isearchai.learning.MLElement;
import org.uma.jmetal.util.binarySet.BinarySet;

public class MLBinarySet extends BinarySet implements MLElement {
    /**
     * Constructor
     *
     * @param numberOfBits
     */
    public MLBinarySet(int numberOfBits) {
        super(numberOfBits);
    }

    @Override
    public boolean setFreezeFromDM(double v) {
        return false;
    }

    @Override
    public boolean setFreezeFromDM() {
        return false;
    }

    @Override
    public boolean isFreezeByDM() {
        return false;
    }

    @Override
    public void setFreezedByCluster() {

    }

    @Override
    public <E extends MLElement> boolean totalyEquals(E e) {
        return false;
    }

    @Override
    public float getNumberId() {
        return 0;
    }
}
