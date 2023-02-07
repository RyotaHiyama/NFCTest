package com.nokopi.nfctest

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcF
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.nokopi.nfctest.ui.main.MainFragment

class NFCReader(private val activity: FragmentActivity): android.os.Handler(Looper.getMainLooper()) {
    private var nfcAdapter : NfcAdapter? = null
    private var callback : CustomReaderCallback? = null

    private var listener: MainFragment.NFCReaderInterface? = null
    interface Listener

    fun start(){
        callback = CustomReaderCallback()
        callback?.setHandler(this)
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
        nfcAdapter!!.enableReaderMode(activity,callback
            ,NfcAdapter.FLAG_READER_NFC_F or
                    NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_V or
                    NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,null)
    }
    fun stop(){
        nfcAdapter!!.disableReaderMode(activity)
        callback = null
    }

    override fun handleMessage(msg: Message) {                  // コールバックからのメッセージクラス
        if (msg.arg1 == 1){                                     // 読み取り終了
            listener?.onReadTag(msg.obj as Tag)                 // 拡張用
        }
        if (msg.arg1 == 2){                                     // 読み取り終了
            listener?.onConnect()                               // 拡張用
        }
    }

    fun setListener(listener: Listener?) {         // イベント受け取り先を設定
        if (listener is MainFragment.NFCReaderInterface) {
            this.listener = listener as MainFragment.NFCReaderInterface
        }
    }

    private class CustomReaderCallback : NfcAdapter.ReaderCallback {
        private var handler : android.os.Handler? = null
        override fun onTagDiscovered(tag: Tag) {
            Log.d("Sample", tag.id.toString())
            val msg = Message.obtain()
            msg.arg1 = 1
            msg.obj = tag
            if (handler != null) handler?.sendMessage(msg)
            val nfc : NfcF = NfcF.get(tag) ?: return
            try {
                nfc.connect()
                //nfc.transceive()
                nfc.close()
                msg.arg1 = 2
                msg.obj = tag
                if (handler != null) handler?.sendMessage(msg)
            }catch (e : Exception){
                nfc.close()
            }
        }
        fun setHandler(handler  : android.os.Handler){
            this.handler = handler
        }
    }
}