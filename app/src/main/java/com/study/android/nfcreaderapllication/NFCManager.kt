package com.study.android.nfcreaderapllication

import android.app.Activity
import android.nfc.*
import android.nfc.tech.Ndef
import android.widget.Toast

class NFCManager(
    private val activity: Activity
) : NfcAdapter.ReaderCallback, NfcAdapter.OnNdefPushCompleteCallback {

    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(activity)
    private var ndefMessage: NdefMessage? = null

    //Configure the message to write on tag NFC
    init {
        ndefMessage = NdefMessage(
            arrayOf(
                NdefRecord.createTextRecord(
                    "en",
                    "Hello Word!"
                )
            )
        )
    }

    fun enable() {
        //Allow the detection NFC
        nfcAdapter?.enableReaderMode(activity, this, NfcAdapter.FLAG_READER_NFC_A, null)
    }

    fun disable() {
        //Turn off the detection NFC
        nfcAdapter?.disableReaderMode(activity)
    }

    fun writeTag(tag: Tag) {
        val ndef = Ndef.get(tag)
        ndef?.connect()
        ndef.writeNdefMessage(ndefMessage)
        ndef?.close()
    }

    fun readTag(tag: Tag): String {
        val ndef = Ndef.get(tag)
        ndef?.connect()
        val message = ndef?.ndefMessage
        ndef?.close()

        message?.let {
            return String(it.records[0].payload)
        }
        return ""
    }

    override fun onTagDiscovered(tag: Tag?) {
        //Read the message of NFC tag
        val ndef = Ndef.get(tag)
        ndef?.connect()
        val message = ndef?.ndefMessage
        ndef.close()

        //Show the read message
        message?.let {
            val payload = String(it.records[0].payload)
            Toast.makeText(activity, payload, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNdefPushComplete(event: NfcEvent?) {
        //Warning for user which writer is done
        Toast.makeText(activity, "NFC tag written", Toast.LENGTH_SHORT).show()
    }
}