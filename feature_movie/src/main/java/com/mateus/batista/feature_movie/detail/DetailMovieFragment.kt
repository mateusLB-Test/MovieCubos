package com.mateus.batista.feature_movie.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.mateus.batista.base_feature.BaseFragment
import com.mateus.batista.base_feature.util.setImgByUrl
import com.mateus.batista.feature_movie.R
import com.mateus.batista.feature_movie.util.ToolbarTitleListener
import kotlinx.android.synthetic.main.fragment_detail_movie.*


class DetailMovieFragment : BaseFragment() {

    private val detailsArgs: DetailMovieFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        (activity as ToolbarTitleListener).updateTitle(detailsArgs.title)
        imgDetail.setImgByUrl(detailsArgs.urlImg)
        descrition.text = detailsArgs.description
    }
}
