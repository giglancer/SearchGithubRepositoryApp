package com.searchgithubrepositoryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.searchgithubrepositoryapp.room.GithubRepositoryDAO
import com.searchgithubrepositoryapp.room.GithubRepositoryDatabase
import com.searchgithubrepositoryapp.room.GithubRepositoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultFragment : Fragment() {
    private lateinit var db: GithubRepositoryDatabase
    private lateinit var dao: GithubRepositoryDAO

    private lateinit var mAdapter: ResultKeyWordAdapter
    private lateinit var githubRepositoryList: MutableList<GithubRepositoryEntity>

    private val args: ResultFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.db = Room.databaseBuilder(
            requireContext(),
            GithubRepositoryDatabase::class.java,
            "GithubRepositories.db"
        ).build()
        this.dao = this.db.githubRepositoryDAO()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.resultKeyword).text = args.keyword
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycleScope.launch {
            settingRecyclerView()
        }
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    private suspend fun settingRecyclerView() {
        withContext(Dispatchers.Main) {
            dao.getAll().observe(viewLifecycleOwner, Observer {
                githubRepositoryList = it as MutableList<GithubRepositoryEntity>

                val recyclerView = view?.findViewById<RecyclerView>(R.id.githubRepositoryList)
                recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                mAdapter = ResultKeyWordAdapter(githubRepositoryList)
                recyclerView?.adapter = mAdapter
            })
        }
    }
}