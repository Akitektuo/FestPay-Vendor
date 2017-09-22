package com.ariondan.vendor.nfc;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import static com.ariondan.vendor.nfc.CommonMethods.buildSelectApdu;

/**
 * Created by AoD Akitektuo on 22-Sep-17 at 17:57.
 */

public class CardReader implements NfcAdapter.ReaderCallback {

    private static final String SAMPLE_LOYALTY_CARD_AID = "F222222222";

    private static final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};

    private WeakReference<AccountCallback> accountCallback;

    public CardReader(AccountCallback accountCallback) {
        this.accountCallback = new WeakReference<>(accountCallback);
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        IsoDep isoDep = IsoDep.get(tag);
        if (isoDep != null) {
            try {
                isoDep.connect();
                byte[] command = buildSelectApdu(SAMPLE_LOYALTY_CARD_AID);
                byte[] result = isoDep.transceive(command);
                int resultLength = result.length;
                byte[] statusWord = {result[resultLength - 2], result[resultLength - 1]};
                byte[] payload = Arrays.copyOf(result, resultLength - 2);
                if (Arrays.equals(SELECT_OK_SW, statusWord)) {
                    String accountNumber = new String(payload, "UTF-8");
                    accountCallback.get().onAccountReceived(accountNumber);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface AccountCallback {
        void onAccountReceived(String data);
    }

}
