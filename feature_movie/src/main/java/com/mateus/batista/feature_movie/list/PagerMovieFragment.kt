package com.mateus.batista.feature_movie.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mateus.batista.base_feature.BaseFragment
import com.mateus.batista.base_feature.listeners.OnItemClickListener
import com.mateus.batista.base_feature.util.observeEvent
import com.mateus.batista.feature_movie.R
import com.mateus.batista.feature_movie.list.adapter.MovieAdapter
import com.mateus.batista.feature_movie.list.viewModel.PagerMovieViewModel
import com.mateus.batista.feature_movie.model.MovieFeature
import kotlinx.android.synthetic.main.fragment_pager_movie.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class PagerMovieFragment : BaseFragment(), OnItemClickListener<MovieFeature> {

    companion object {
        private const val ID = "genreId"

        fun newInstance(genreId: Int) = PagerMovieFragment().apply {
            arguments = bundleOf(
                ID to genreId
            )
        }
    }

    private var genreId: Int? = 0
    private var isAlreadyScrolled = false
    private lateinit var adapter: MovieAdapter
    private val viewModel: PagerMovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genreId = arguments?.getInt(ID)
        genreId?.let { viewModel.getMovieGenreId(it) }
    }

    private fun subscribeViewModel() {
        viewModel.getListMovieGenreStatus().observeEvent(this,
            loading = { progressBar.visibility = View.VISIBLE },
            success = {
                progressBar.visibility = View.GONE
                adapter = MovieAdapter(
                    it.toMutableList(),
                    this
                )
                recycleView.adapter = adapter
                recycleView.layoutManager = GridLayoutManager(context, 2)
                recycleView.addOnScrollListener(scrollListener)
                adapter.notifyDataSetChanged()
            },
            error = {
                progressBar.visibility = View.GONE
                handleErrors(it) { genreId?.let { id -> viewModel.getMovieGenreId(id) } }
            })

        viewModel.getLoadMoreGenreState().observeEvent(this,
            loading = { paginationLoading.visibility = View.VISIBLE },
            success = {
                paginationLoading.visibility = View.GONE
                adapter.list.addAll(it)
                adapter.notifyItemRangeInserted(adapter.itemCount, it.size)
                isAlreadyScrolled = false
            },
            error = {
                paginationLoading.visibility = View.GONE
                handleErrors(it) { genreId?.let { id -> viewModel.loadMoreGenre(id) } }
            })
    }

    override fun onItemClick(item: MovieFeature, position: Int) {
        navController.navigate(
            ListMovieFragmentDirections.actionListMovieFragmentToDetailMovieFragment(
                item.posterPath,
                item.title,
                item.overview
            )
        )
    }

    private val scrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition() + 1
                if (adapter.itemCount == lastVisibleItem && !isAlreadyScrolled) {
                    genreId?.let { id -> viewModel.loadMoreGenre(id) }
                    isAlreadyScrolled = true
                }
            }
        }
}
