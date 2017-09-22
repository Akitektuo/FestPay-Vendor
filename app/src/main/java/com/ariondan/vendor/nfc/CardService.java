package com.ariondan.vendor.nfc;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

import com.ariondan.vendor.local.preference.Preference;

import java.util.Arrays;

import static com.ariondan.vendor.nfc.CommonMethods.buildSelectApdu;
import static com.ariondan.vendor.nfc.CommonMethods.hexStringToByteArray;


/**
 * Created by AoD Akitektuo on 22-Sep-17 at 19:13.
 */

public class CardService extends HostApduService {

    private static final String SAMPLE_LOYALTY_CARD_AID = "F222222222";
    private static final byte[] SELECT_OK_SW = hexStringToByteArray("9000");
    private static final byte[] UNKNOWN_CMD_SW = hexStringToByteArray("0000");
    private static final byte[] SELECT_APDU = buildSelectApdu(SAMPLE_LOYALTY_CARD_AID);

    public static byte[] concatArrays(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle bundle) {
        if (Arrays.equals(SELECT_APDU, commandApdu)) {
            String data = new Preference(this).getPreferenceString(Preference.KEY_NFC_DATA);
            byte[] dataBytes = data.getBytes();
            return concatArrays(dataBytes, SELECT_OK_SW);
        } else {
            return UNKNOWN_CMD_SW;
        }
    }

    @Override
    public void onDeactivated(int i) {

    }

}
