package net.senmori.simpleprotect.protection.utility;

import java.util.HashMap;
import java.util.Map;
import net.senmori.simpleprotect.protection.access.AccessManager;
import net.senmori.simpleprotect.protection.utility.function.DoorTimerFunction;
import net.senmori.simpleprotect.protection.utility.function.SignFunction;
import org.bukkit.block.Sign;

public final class SignFunctionManager {

    private static final Map<SignFunctionLabel, SignFunction> signFunctions;

    static {
        signFunctions = new HashMap<>();
        signFunctions.put(SignFunctionLabel.TIMER, new DoorTimerFunction());
    }

    public static SignFunction getUtility(String name) {
        return getUtility(SignFunctionLabel.getByName(name));
    }

    public static SignFunction getUtility(SignFunctionLabel label) {
        if(label == null) {
            return null;
        }
        return signFunctions.get(label);
    }

    public static boolean hasUtility(Sign sign) {
        for(String line : sign.getLines()) {
            if(line.startsWith(AccessManager.START_CHAR) && line.endsWith(AccessManager.END_CHAR)) {
                String top = line.substring(1, line.length() -1);
                SignFunctionLabel label = SignFunctionLabel.getByName(top);
                if(label != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static SignFunctionLabel getLabel(Sign sign, SignFunctionLabel label) {
        if(SignFunctionManager.hasUtility(sign)) {
            for(String line : sign.getLines()) {
                if(line.startsWith(AccessManager.START_CHAR) && line.endsWith(AccessManager.END_CHAR)) {
                    String top = line.substring(1, line.length() -1);
                    SignFunctionLabel util = SignFunctionLabel.getByName(top);
                    if(util != null && util == label) {
                        return util;
                    }
                }
            }
        }
        return null;
    }
}
