
package paavohuh.sourcream.emulation;

import java.util.Optional;

public enum ExecutionState {
    RUNNING,
    PAUSED,
    WAITING_FOR_KEY;
    
    private Register register;

    public Optional<Register> getMetaRegister() {
        return Optional.ofNullable(register);
    }
    
    public static ExecutionState running() {
        return ExecutionState.RUNNING;
    }
    
    public static ExecutionState paused() {
        return ExecutionState.PAUSED;
    }
    
    public static ExecutionState waitingForKey(Register storeIn) {
        ExecutionState state = WAITING_FOR_KEY;
        state.register = storeIn;
        
        return state;
    }
}
