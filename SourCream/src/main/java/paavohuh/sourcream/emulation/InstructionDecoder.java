/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paavohuh.sourcream.emulation;

import java.util.Optional;
import org.joou.UShort;

public interface InstructionDecoder {
    Optional<Instruction> decode(UShort code);
}
