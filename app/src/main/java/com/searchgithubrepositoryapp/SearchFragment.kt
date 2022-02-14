package com.searchgithubrepositoryapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.searchgithubrepositoryapp.databinding.FragmentSearchBinding
import com.searchgithubrepositoryapp.room.GithubRepositoryDAO
import com.searchgithubrepositoryapp.room.GithubRepositoryDatabase
import com.searchgithubrepositoryapp.room.GithubRepositoryEntity
import com.searchgithubrepositoryapp.service.GithubRepositoryNameInfo
import com.searchgithubrepositoryapp.service.GithubRepositoryService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.IllegalStateException

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: GithubRepositoryDatabase
    private lateinit var dao:GithubRepositoryDAO

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
        binding.searchButton.setOnClickListener {
            if (binding.editSearch.text.isEmpty()) {
                binding.editSearch.error = R.string.error_text.toString()
            } else {
                GlobalScope.launch {
                    getGithubRepository()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                dao.deleteAll()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun getGithubRepository() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        withContext(Dispatchers.IO) {
            val service = retrofit.create(GithubRepositoryService::class.java)
            val githubRepositoryResponse = service.fetchGithubRepository(
                binding.editSearch.text.toString()
            ).execute().body()?: throw IllegalStateException("body is null")
            Log.d("response", githubRepositoryResponse.items[0].full_name)

            insertDB(githubRepositoryResponse)

            withContext(Dispatchers.Main) {
                pushResult()
            }
        }
    }
    private fun insertDB(githubRepositoryResponse: GithubRepositoryNameInfo) {
        for (item in githubRepositoryResponse.items) {
            val githubRepositoryEntity = GithubRepositoryEntity(id = 0, repositoryName = item.full_name)
            dao.insert(githubRepositoryEntity)
            Log.d("data", githubRepositoryEntity.toString())
        }
    }
    private fun pushResult() {
        val action = SearchFragmentDirections.actionSearchFragmentToResultFragment(binding.editSearch.text.toString())
        findNavController().navigate(action)
    }
}