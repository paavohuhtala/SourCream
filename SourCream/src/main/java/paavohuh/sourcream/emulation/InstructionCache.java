/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paavohuh.sourcream.emulation;


public interface InstructionCache extends InstructionDecoder {

    void register(Instruction instr);
    
}
