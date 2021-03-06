package ir.pmoslem.covid_19tracker.view.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ir.pmoslem.covid_19tracker.R
import ir.pmoslem.covid_19tracker.databinding.FragmentNewsBinding
import ir.pmoslem.covid_19tracker.view.showSnackBar
import ir.pmoslem.covid_19tracker.viewmodel.NewsViewModel

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var viewBinding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter

    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentNewsBinding.inflate(inflater, container, false)
        val root = viewBinding.root


        newsAdapter = NewsAdapter(requireContext())

        newsViewModel.getNewsFromDatabase().observe(requireActivity(),
            { newsList ->
                if (newsList != null) {
                    newsAdapter.setNews(newsList)
                    viewBinding.rvNews.layoutManager =
                        LinearLayoutManager(root.context, RecyclerView.HORIZONTAL, false)
                    viewBinding.rvNews.adapter = newsAdapter
                }
            })

        newsViewModel.getProgressBarStatus().observe(requireActivity(),
            { visibility ->
                if (visibility!!) {
                    viewBinding.pbLoadNews.visibility = View.VISIBLE
                    viewBinding.rvNews.visibility = View.GONE
                } else {
                    viewBinding.pbLoadNews.visibility = View.GONE
                    viewBinding.rvNews.visibility = View.VISIBLE
                }
            })

        newsViewModel.getErrorStatus().observe(requireActivity(),
            { isErrorOccurred ->
                if (isErrorOccurred == true) {
                    showSnackBar("Couldn't connect to the server", R.color.redColorSnackBar)
                }
            })

        return root
    }
}