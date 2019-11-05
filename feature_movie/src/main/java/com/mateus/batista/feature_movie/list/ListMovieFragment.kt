package com.mateus.batista.feature_movie.list


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mateus.batista.base_feature.BaseFragment
import com.mateus.batista.base_feature.listeners.OnItemClickListener
import com.mateus.batista.base_feature.util.observeEvent
import com.mateus.batista.feature_movie.R
import com.mateus.batista.feature_movie.list.adapter.MovieAdapter
import com.mateus.batista.feature_movie.list.adapter.TabsAdapter
import com.mateus.batista.feature_movie.list.viewModel.ListMovieViewModel
import com.mateus.batista.feature_movie.model.MovieFeature
import com.mateus.batista.feature_movie.util.GENRES.*
import kotlinx.android.synthetic.main.fragment_list_movie.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListMovieFragment : BaseFragment(), OnItemClickListener<MovieFeature> {

    private lateinit var adapter: MovieAdapter
    private lateinit var tabsAdapter: TabsAdapter
    private var name = ""

    private var isAlreadyScrolled = false
    private val viewModel: ListMovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
        setHasOptionsMenu(true)

        val fragments = listOf<Fragment>(
            PagerMovieFragment.newInstance(ACTION.id),
            PagerMovieFragment.newInstance(DRAMA.id),
            PagerMovieFragment.newInstance(FANTASY.id),
            PagerMovieFragment.newInstance(FICTION.id)
        )

        val titles = listOf(
            ACTION.nameG,
            DRAMA.nameG,
            FANTASY.nameG,
            FICTION.nameG
        )
        tabsAdapter = TabsAdapter(fragments, titles, childFragmentManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = tabsAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun subscribeViewModel() {
        viewModel.getListMovieNameStatus().observeEvent(this,
            loading = { progressBar.visibility = View.VISIBLE },
            success = {
                progressBar.visibility = View.GONE
                adapter = MovieAdapter(
                    it.toMutableList(),
                    this
                )
                gridRecycleView.adapter = adapter
                gridRecycleView.layoutManager = GridLayoutManager(context, 2)
                gridRecycleView.addOnScrollListener(scrollListener)
                adapter.notifyDataSetChanged()
            },
            error = {
                progressBar.visibility = View.GONE
                handleErrors(it) { viewModel.getMovieName(name) }
            })

        viewModel.getLoadMoreNameState().observeEvent(this,
            loading = { gridPaginationLoading.visibility = View.VISIBLE },
            success = {
                gridPaginationLoading.visibility = View.GONE
                adapter.list.addAll(it)
                adapter.notifyItemRangeInserted(adapter.itemCount, it.size)
                isAlreadyScrolled = false
            },
            error = {
                gridPaginationLoading.visibility = View.GONE
                handleErrors(it) { viewModel.loadMoreName(name) }
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
                    viewModel.loadMoreName(name)
                    isAlreadyScrolled = true
                }
            }
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    name = newText
                    if (name.isNotEmpty()) { viewModel.getMovieName(name) }
                    return false
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    name = query
                    viewModel.getMovieName(name)
                    return false
                }

            })

            setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    pagerLayout?.visibility = View.GONE
                    gridLayout?.visibility = View.VISIBLE
                } else {
                    pagerLayout?.visibility = View.VISIBLE
                    gridLayout?.visibility = View.GONE
                }
            }
        }
    }
}
