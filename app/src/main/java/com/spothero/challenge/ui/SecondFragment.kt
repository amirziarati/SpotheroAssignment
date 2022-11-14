package com.spothero.challenge.ui

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.spothero.challenge.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {

    private val args: SecondFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentSecondBinding.inflate(inflater, container, false)

        setUpActionBar()
        setUpView(view)

        return view.root
    }

    private fun setUpView(view: FragmentSecondBinding) {
        view.tvAddress.text = args.spot.address.street
        view.tvDistance.text = args.spot.distance
        view.tvDescription.text = args.spot.description
        view.btnBook.text = "BOOK FOR $${args.spot.price}"
        Glide.with(requireContext())
            .load(Uri.parse("file:/" + args.spot.facilityPhoto))
            .into(view.imgSpot)
    }

    private fun setUpActionBar() {
        (requireActivity() as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity)
            .supportActionBar?.setDisplayShowHomeEnabled(true)
        (requireActivity() as AppCompatActivity)
            .supportActionBar?.title = args.spot.address.street
    }
}