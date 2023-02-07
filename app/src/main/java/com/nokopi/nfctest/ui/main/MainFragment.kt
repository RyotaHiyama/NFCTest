package com.nokopi.nfctest.ui.main

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nokopi.nfctest.NFCReader
import com.nokopi.nfctest.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding
//    private lateinit var viewModel: MainViewModel
    private val viewModel = MainViewModel()
    val nfcReader = NFCReader(requireActivity())

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
//        // TODO: Use the ViewModel
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nfcReader.setListener(nfcReaderListener)

    }

    private val nfcReaderListener = object : NFCReaderInterface{
        override fun onReadTag(tag: Tag) {
            val idm = tag.id
            tag.techList
            viewModel.updateNFCData(byteToHex(idm))
            Log.i("sample", byteToHex(idm))
        }

        override fun onConnect() {
            TODO("Not yet implemented")
        }
    }

    private fun byteToHex(b : ByteArray) : String{
        var s = ""
        for (i in 0..b.size-1){
            s += "[%02X]".format(b[i])
        }
        return s
    }

    interface NFCReaderInterface : NFCReader.Listener {
        fun onReadTag(tag: Tag)
        fun onConnect()
    }

    override fun onResume() {
        super.onResume()
        nfcReader.start()
    }

    override fun onPause() {
        super.onPause()
        nfcReader.stop()
    }

}