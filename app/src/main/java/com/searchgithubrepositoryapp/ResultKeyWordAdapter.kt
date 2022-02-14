package com.searchgithubrepositoryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.searchgithubrepositoryapp.room.GithubRepositoryEntity

class ResultKeyWordAdapter(private val  githubRepositoryList: MutableList<GithubRepositoryEntity>): RecyclerView.Adapter<ResultKeyWordAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val repositoryName: TextView = view.findViewById(R.id.repositoryName)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultKeyWordAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.result_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultKeyWordAdapter.ViewHolder, position: Int) {
        val updatedGithubRepositoryList = githubRepositoryList[position]
        holder.repositoryName.text = updatedGithubRepositoryList.repositoryName
    }

    override fun getItemCount(): Int {
        return githubRepositoryList.size
    }
}