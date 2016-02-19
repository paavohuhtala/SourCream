
package paavohuh.sourcream.tests;

import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.emulation.State;

public abstract class TestWithState {
    protected final State initialState;

    public TestWithState() {
        this.initialState = new State(DeviceConfiguration.getDefault());
    }
}
