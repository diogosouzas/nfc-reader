package com.study.android.nfcreaderapllication

import android.nfc.NfcAdapter
import android.nfc.tech.Ndef
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.study.android.nfcreaderapllication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private lateinit var binding: ActivityMainBinding

    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtein a reference
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    override fun onResume() {
        super.onResume()

        // Register this actitivy for receive NFC tags
        nfcAdapter?.enableReaderMode(
            this,
            this,
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            null
        )
    }

    override fun onPause() {
        super.onPause()

        // Stop receive NFC tags when this activity don't foreground state
        nfcAdapter?.disableReaderMode(this)
    }

    override fun onTagDiscovered(tag: android.nfc.Tag?) {
        // Obtein a Ndef king object, if the NFC tag is Ndef kind
        val ndef = Ndef.get(tag)
        if (ndef != null) {
            // Obtein NFC tag value as a NdefMessage object
            val ndefMessage = ndef.ndefMessage

            // Convert value in a String
            val message = String(ndefMessage.records[0].payload)

            // Show value
            runOnUiThread {
                binding.textView.text = message
            }
        }
    }
}
