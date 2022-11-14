package com.spothero.challenge.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.spothero.challenge.data.SpotHeroApi
import com.spothero.challenge.data.model.Spot
import com.spothero.challenge.databinding.FragmentFirtstBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentFirtstBinding.inflate(inflater, container, false)

        setUpActionBar()

        callSpotsApi(
            onSuccess = { spots -> setupView(view, spots) },
            onFail = { throwable ->
                Log.e("FirstFragment", throwable.message ?: "onError")
            })

        return view.root
    }

    private fun callSpotsApi(
        onSuccess: (spots: List<Spot>) -> Unit,
        onFail: (throwable: Throwable) -> Unit
    ) {
        requireContext().let { context ->
            SpotHeroApi(context).getSpotsObservable()
                .subscribeOn(Schedulers.io())
                .map { unsorted ->
                    unsorted.sortedBy { it.price }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { spots ->
                        onSuccess(spots)
                    },
                    onFail
                )
        }
    }

    private fun setupView(
        view: FragmentFirtstBinding,
        spots: List<Spot>
    ) {
        view.rvSpots.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        view.rvSpots.addItemDecoration(
            DividerItemDecoration(
                this.context,
                LinearLayoutManager.VERTICAL
            )
        )
        view.rvSpots.adapter = SpotsAdapter(spots) {
            val action = FirstFragmentDirections.navigateToDetails(it)
            Navigation.findNavController(view.root).navigate(action)
        }
    }

    private fun setUpActionBar() {
        (requireActivity() as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (requireActivity() as AppCompatActivity)
            .supportActionBar?.setDisplayShowHomeEnabled(false)
        (requireActivity() as AppCompatActivity)
            .supportActionBar?.title = "Spots"
    }
}